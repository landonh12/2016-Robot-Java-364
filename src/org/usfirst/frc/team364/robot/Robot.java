package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Joystick;

class IOMap {

    /*
     * PWM
     */
    static final int lfdm = 0;
    static final int rfdm = 0;
    static final int lrdm = 0;
    static final int rrdm = 0;
    static final int sm1  = 0;
    static final int sm2  = 0;
    static final int im   = 0;
    static final int pm   = 0;
    static final int fm   = 0;
    static final int wm   = 0;

    /*
     * DIO
     */
    static final int ball = 0;
    static final int lenca= 0;
    static final int lencb= 0;
    static final int renca= 0;
    static final int rencb= 0;

    /*
     * ANALOG
     */
    static final int gyro = 0;

}

class PIDControl {

   /*
	* Pseudo code (source Wikipedia)
	* 
	*  previous_error = 0
	*  integral = 0 
	*  start:
	*  error = setpoint – PV [actual_position]
	*  integral = integral + error*dt
	*  derivative = (error - previous_error)/dt
	*	 output = Kp*error + Ki*integral + Kd*derivative
	*  previous_error = error
	*  wait(dt)
	*  goto start
	*  
	*/
	
	static double previous_error = 0;
	static double error = 0;
	static double integral = 0;
	static double derivative = 0;
	static double output = 0;
	
	public void resetPIDControl() {
		previous_error = 0;
		integral = 0;
	}
	
	public double PIDController(double Kp, double Ki, double Kd, double setpoint, double process_var) {
		error = setpoint - process_var;
		integral = integral + error;
		derivative = error - previous_error;
		output = Kp*error + Ki*integral + Kd*derivative;
		previous_error = error;
		return output;
	}
}

class Input {

    static final Joystick leftStick  = new Joystick(0);
    static final Joystick rightStick = new Joystick(1);
    static final Joystick controller = new Joystick(2);

}

class HangSystem {

    static final VictorSP flipMotor  = new VictorSP(IOMap.fm);
    static final VictorSP winchMotor = new VictorSP(IOMap.wm);

    static void manualFlipControl(double power) {
        flipMotor.set(power);	
    }

    static void manualWinchControl(double power) {
        winchMotor.set(power);
    }

    static void stopFlipMotor() {
        flipMotor.set(0);
    }

    static void stopWinchMotor() {
        winchMotor.set(0);
    }

}

class DriveSystem {

    static double ls = Input.leftStick.getY();
    static double rs = Input.rightStick.getY();

    static final VictorSP leftFront  = new VictorSP(IOMap.lfdm);
    static final VictorSP leftRear   = new VictorSP(IOMap.lrdm);
    static final VictorSP rightFront = new VictorSP(IOMap.rfdm);
    static final VictorSP rightRear  = new VictorSP(IOMap.rrdm);

    static final AnalogGyro gyro = new AnalogGyro(IOMap.gyro);

    static final RobotDrive rd = new RobotDrive(leftFront, leftRear, rightFront, rightRear);

    static PIDControl pid = new PIDControl();
    static double output;
    
    static void drive() {
        rd.tankDrive(ls, rs);
    }

    static void driveWithGyro(double angle) {
    	output = pid.PIDController(0.5, 0.1, 0.1, angle, gyro.getAngle());
        rd.arcadeDrive(ls, output);
    }

    static void autoDriveWithGyro(double speed, double angle) {
    	output = pid.PIDController(0.5, 0.1, 0.1, angle, gyro.getAngle());
        rd.arcadeDrive(speed, output);
    }

}

class ShootSystem {

    static final VictorSP shootMotor1 = new VictorSP(IOMap.sm1);
    static final VictorSP shootMotor2 = new VictorSP(IOMap.sm2);

    static void speedControl(double power) {
        shootMotor1.set(power);
        shootMotor2.set(power);
    }

    static void stopShooterMotors() {
        shootMotor1.set(0);
        shootMotor2.set(0);
    }

}

class IntakeSystem {

    static final VictorSP intakeMotor = new VictorSP(IOMap.im);
    static final VictorSP pulleyMotor = new VictorSP(IOMap.pm);
    
    static final DigitalInput banner  = new DigitalInput(IOMap.ball);

    static boolean ballInQueue;

    static void pulleyControl(double power) {
        pulleyMotor.set(power);
    }

    static void intake() {
        if(banner.get() == false) {
            manualIntake(1);
            ballInQueue = false;
        } else {
            stopIntakeMotors();
            ballInQueue = true;
        }
    }

    static void outTakeForShoot() {
        if(banner.get() == true) {
            manualIntake(-0.3);
            ballInQueue = true;
        } else {
            stopIntakeMotors()
            ballInQueue = false;
        }
    }

    static void manualIntake(double power) {
        intakeMotor.set(power);
    }

    static void stopIntakeMotors() {
        intakeMotor.set(0);
    }
}

public class Robot extends IterativeRobot {
	
    //Class initialization
    public static Input        input;
    public static DriveSystem  driveSystem;
    public static IntakeSystem intakeSystem;
    public static HangSystem   hangSystem;
    public static ShootSystem  shootSystem;

    //Switch statement variables
    public int     intakeMode        = 0;
    public boolean shootMode         = false;
    public double  manualIntakePower = 0;
    public int     winchMode         = 2;
    public int     flipMode          = 2;

    public void robotInit() {
        input        = new Input();
        driveSystem  = new DriveSystem();
        intakeSystem = new IntakeSystem();
        hangSystem   = new HangSystem();
        shootSystem  = new ShootSystem();
    }
    
    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
    }

    public void teleopPeriodic() {
    	
        //Run the drive() method of DriveSystem during teleop. Reset the gyro for driveWithGyro.
        //Call the driveWithGyro method if a button is pressed.
    	if(!input.leftStick.getRawButton()) {
            driveSystem.drive();
            driveSystem.gyro.reset();
        } else {
            driveSystem.driveWithGyro(0);
        }

        //Hang Logic
        if(input.controller.getRawButton()) winchMode = 0;
        else if(input.controller.getRawButton()) winchMode = 1;
        else winchMode = 2;

        if(input.controller.getRawButton()) flipMode  = 0;
        else if(input.controller.getRawButton()) flipMode  = 1;
        else flipMode = 2;

        //Shoot Logic
        if(shootMode = 1) {
            if(input.controller.getRawButton()) {
                shootMode = 2;
            } else {
                shootMode = shootMode;
            }
        } else {
            shootMode = shootMode;
        }

        //Intake Logic
        if(shootMode == 0) {
            if(input.controller.getRawButton(0)) {
                intakeMode = 1;
            } else {
                intakeMode = 0;
            }
        }
        
        //Hang Controllers
        switch(winchMode) {
            case 0:
                hangSystem.manualWinchControl();
                break;
            case 1:
                hangSystem.manualWinchControl();
                break;
            case 2:
                hangSystem.stopWinchMotor();
                break;
        }

        switch(flipMode) {
            case 0:
                hangSystem.manualFlipControl();
                break;
            case 1:
                hangSystem.manualFlipControl();
                break;
            case 2:
                hangSystem.stopFlipMotor();
                break;
        }

        //Intake Controller
        switch(intakeMode) {
            case 0:
                intakeSystem.manualIntake(Input.controller.getRawAxis(0);
                break;
            case 1:
                intakeSystem.intake();
                break;
            case 2:
                intakeSystem.outTakeForShoot();
                break;
            case 3:
                intakeSystem.manualIntake(1);
                break;
        }
        
        //Shoot Controller
        switch(shootMode) {
            case 0:
               shootSystem.stopShooterMotors();
               break;
            case 1:
               shootSystem.speedControl(0.9);
               intakeMode = 2;
               break;
            case 2:
               shootSystem.speedControl(0.9);
               intakeMode = 3;
               if(intakeSystem.banner.get()) shootMode = 0;
               break;
        }

	}
    
    public void testPeriodic() {
    }
    
}
