package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
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
	
	private double previous_error = 0;
	private double error = 0;
	private double integral = 0;
	private double derivative = 0;
	private double output = 0;
	
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

    public final Joystick leftStick  = new Joystick(0);
    public final Joystick rightStick = new Joystick(1);
    public final Joystick controller = new Joystick(2);

}

class HangSystem {

    private final VictorSP flipMotor  = new VictorSP(IOMap.fm);
    private final VictorSP winchMotor = new VictorSP(IOMap.wm);

    public void manualFlipControl(double power) {
        flipMotor.set(power);	
    }

    public void manualWinchControl(double power) {
        winchMotor.set(power);
    }

    public void stopFlipMotor() {
        flipMotor.set(0);
    }

    public void stopWinchMotor() {
        winchMotor.set(0);
    }

}

class DriveSystem {

    private final VictorSP leftFront  = new VictorSP(IOMap.lfdm);
    private final VictorSP leftRear   = new VictorSP(IOMap.lrdm);
    private final VictorSP rightFront = new VictorSP(IOMap.rfdm);
    private final VictorSP rightRear  = new VictorSP(IOMap.rrdm);

    private final AnalogGyro gyro = new AnalogGyro(IOMap.gyro);

    private final RobotDrive rd = new RobotDrive(leftFront, leftRear, rightFront, rightRear);

    private PIDControl pid = new PIDControl();
    private double output;
    
    public void drive(double leftPower, double rightPower) {
        rd.tankDrive(leftPower, rightPower);
    }

    public void driveWithGyro(double speed, double angle) {
    	output = pid.PIDController(0.5, 0.1, 0.1, angle, gyro.getAngle());
        rd.arcadeDrive(speed, output);
    }
    
    public void resetGyro() {
    	gyro.reset();
    }

}

class ShootSystem {

    private final VictorSP shootMotor1 = new VictorSP(IOMap.sm1);
    private final VictorSP shootMotor2 = new VictorSP(IOMap.sm2);

    public void speedControl(double power) {
        shootMotor1.set(power);
        shootMotor2.set(power);
    }

    public void stopShooterMotors() {
        shootMotor1.set(0);
        shootMotor2.set(0);
    }

}

class IntakeSystem {

    private final VictorSP intakeMotor = new VictorSP(IOMap.im);
    private final VictorSP pulleyMotor = new VictorSP(IOMap.pm);
    
    private final DigitalInput banner  = new DigitalInput(IOMap.ball);

    public boolean ballInQueue;

    public void pulleyControl(double power) {
        pulleyMotor.set(power);
    }

    public void intake() {
        if(banner.get() == false) {
            manualIntake(1);
            ballInQueue = false;
        } else {
            stopIntakeMotors();
            ballInQueue = true;
        }
    }

    public void outTakeForShoot() {
        if(banner.get() == true) {
            manualIntake(-0.3);
            ballInQueue = true;
        } else {
            stopIntakeMotors();
            ballInQueue = false;
        }
    }

    public void manualIntake(double power) {
        intakeMotor.set(power);
    }

    public void stopIntakeMotors() {
        intakeMotor.set(0);
    }
    
    public boolean getBanner() {
    	return banner.get();
    }
}

public class Robot extends IterativeRobot {
	
    //Class initialization
    public Input 		input;
    public DriveSystem  driveSystem;
    public IntakeSystem intakeSystem;
    public HangSystem   hangSystem;
    public ShootSystem  shootSystem;
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();

    //Switch statement variables
    public int     intakeMode        = 0;
    public int     shootMode         = 0;
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
    	if(!input.leftStick.getRawButton(0)) {
            driveSystem.drive(ls, rs);
            driveSystem.resetGyro();
        } else {
            driveSystem.driveWithGyro(ls, 0);
        }

        //Hang Logic
        if(input.controller.getRawButton(0)) winchMode = 0;
        else if(input.controller.getRawButton(0)) winchMode = 1;
        else winchMode = 2;

        if(input.controller.getRawButton(0)) flipMode  = 0;
        else if(input.controller.getRawButton(0)) flipMode  = 1;
        else flipMode = 2;

        //Shoot Logic
        if(shootMode == 1) {
            if(input.controller.getRawButton(0))
                shootMode = 2;
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
                hangSystem.manualWinchControl(0);
                break;
            case 1:
                hangSystem.manualWinchControl(0);
                break;
            case 2:
                hangSystem.stopWinchMotor();
                break;
        }

        switch(flipMode) {
            case 0:
                hangSystem.manualFlipControl(0);
                break;
            case 1:
                hangSystem.manualFlipControl(0);
                break;
            case 2:
                hangSystem.stopFlipMotor();
                break;
        }

        //Intake Controller
        switch(intakeMode) {
            case 0:
                intakeSystem.manualIntake(input.controller.getRawAxis(0));
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
               if(intakeSystem.getBanner()) shootMode = 0;
               break;
        }

	}
    
    public void testPeriodic() {
    }
    
}
