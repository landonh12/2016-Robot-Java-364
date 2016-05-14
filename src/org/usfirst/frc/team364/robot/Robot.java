/*
 * 2016-Java-Robot-364
 * Written by Landon Haugh
 * A complete rewrite of the 2016 robot in Java.
 * For educational purposes
 */

package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team364.StateControllers;
/*
 * Class: IOMap
 * Desc: THIS CLASS HOLDS ALL OF THE PORT NUMBERS FOR PWM, ANALOG, AND DIGITAL INPUTS AND OUTPUTS.
 */
class IOMap {

    /*
     * PWM
     */
    static final int lfdm = 0; //Side, Position, Drive Motor
    static final int rfdm = 0; 
    static final int lrdm = 0;
    static final int rrdm = 0;
    static final int sm1  = 0; //Shooter Motor 1/2
    static final int sm2  = 0;
    static final int im   = 0; //Intake, Pulley Motor
    static final int pm   = 0;
    static final int fm   = 0; //Flip, Winch Motor
    static final int wm   = 0;

    /*
     * DIO
     */
    static final int ball = 0; //Banner Sensor
    static final int laenc= 0; //Side, Channel, Encoder
    static final int lbenc= 0;
    static final int raenc= 0;
    static final int rbenc= 0;

    /*
     * ANALOG
     */
    static final int gyro = 0; //Analog Gyro

}


/*
 * Class: PIDControl
 * Desc: PIDCONTROL IS A CLASS CONTAINING A CUSTOM PID METHOD THAT DOESN'T REQUIRE CREATING AN EXTENDED CLASS OF PIDSUBSYSTEM.
 */
class PIDControl {
	
	/*
	 * Initialization values
	 */
	private double previous_error = 0;
	private double error = 0;
	private double integral = 0;
	private double derivative = 0;
	private double output = 0;
	
	/*
	 * Resets all PID values.
	 */
	public void resetPIDControl() {
		previous_error = 0;
		error = 0;
		integral = 0;
		derivative = 0;
		output = 0;
	}
	
	/*
	 * Runs a PID Loop with the given parameters and returns an output.
	 */
	public double PIDController(double Kp, double Ki, double Kd, double setpoint, double process_var) {
		error = setpoint - process_var;
		integral = integral + error;
		derivative = error - previous_error;
		output = Kp*error + Ki*integral + Kd*derivative;
		previous_error = error;
		return output;
	}
}

/*
 * Class: Input
 * Desc: INPUT IS A CLASS THAT HOLDS DECLARATIONS FOR JOYSTICKS AT A USB PORT NUMBER.
 */
class Input {

    public final Joystick leftStick  = new Joystick(0);
    public final Joystick rightStick = new Joystick(1);
    public final Joystick controller = new Joystick(2);

}

/*
 * Class: HangSystem
 * Desc: HANGSYSTEM IS A CLASS THAT CONTROLS MOTOR OUTPUT TO THE HANGING MECHANISM.
 */
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

/*
 * Class: DriveSystem
 * Desc: DRIVESYSTEM IS A CLASS THAT CONTROLS THE DRIVETRAIN MOTORS. IT INCLUDES METHODS FOR GYRO PID CONTROL AND MANUAL JOYSTICK CONTROL.
 * 		 IT ALSO CONTROLS ALL GYRO OPERATION.
 */
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
    
    public void stopDriveMotors() {
        drive(0, 0);
    }
}

/*
 * Class: ShootSystem
 * Desc: SHOOTSYSTEM IS A CLASS THAT CONTROLS ALL MOTOR OUTPUT TO THE SHOOTER.
 */
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

/*
 * Class: Robot
 * Desc: THIS IS THE MAIN CLASS FOR THE ROBOT. IT CONTROLS GAME STATE AND USES JOYSTICK INPUT TO CONTROL SUBSYSTEM STATES.
 */
public class Robot extends IterativeRobot {
	
    //Class initialization
    public Input 		    input;
    public DriveSystem      driveSystem;
    public IntakeSystem     intakeSystem;
    public HangSystem       hangSystem;
    public ShootSystem      shootSystem;
    public StateControllers stateControllers;
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();
    int    gyroAngle;
    int    gyroDriveSpeed;

    //Switch statement variables
    public int     driveMode         = 2;
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
        stateControllers = new StateControllers(); 
    }
    
    public void autonomousInit() {
        stateControllers.resetStates();
    }

    public void autonomousPeriodic() {
        stateControllers();
    }

    public void teleopPeriodic() {
        //Run the drive() method of DriveSystem during teleop. Reset the gyro for driveWithGyro.
        //Call the driveWithGyro method if a button is pressed.
    	if(!input.leftStick.getRawButton(0)) {
            driveMode = 0;
        } else {
            driveMode = 1;
            gyroDriveSpeed = ls;
            gyroAngle = 0;
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
        
        StateControllers.run();
    }

    public void testPeriodic() {
    }
    
}
