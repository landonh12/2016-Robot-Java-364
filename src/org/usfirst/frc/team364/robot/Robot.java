package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

public class Inp

    static final Joystick leftStick  = new Joystick(0);
    static final Joystick rightStick = new Joystick(1);
    static final Joystick controller = new Joystick(2);

}

public class DriveSystem {

    static final int lf  = 0;
    static final int lr  = 1;
    static final int rf = 2;
    static final int rr  = 3;

    static final int ag = 0;

    static double ls = Input.leftStick.getY();
    static double rs = Input.rightStick.getY();

    static final VictorSP leftFront  = new VictorSP(lf);
    static final VictorSP leftRear   = new VictorSP(lr);
    static final VictorSP rightFront = new VictorSP(rf);
    static final VictorSP rightRear  = new VictorSP(rr);

    static final AnalogGyro gyro = new AnalogGyro(ag);

    static final RobotDrive rd = new RobotDrive(leftFront, leftRear, rightFront, rightRear);

    static PIDOutput pidTurn;
    static PIDController pidDrive = new PIDController(0.5, 0.1, 0.1, gyro, pidTurn);

    static void drive() {
        rd.tankDrive(ls, rs);
    }

    static void driveWithGyro(double angle) {
        pidDrive.setSetpoint(angle);
        rd.arcadeDrive(ls, pidTurn);
    }

    static void autoDriveWithGyro(double speed, double angle) {
        pidDrive.setSetpoint(angle);
        rd.arcadeDrive(speed, pidTurn);
    }

}

class ShootSystem {

    static final int sm1 = 0;
    static final int sm2 = 0;

    static final VictorSP shootMotor1 = new VictorSP(sm1);
    static final VictorSP shootMotor2 = new VictorSP(sm2);

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

    static final int im = 0;
    static final int pm = 0;
    static final int bs = 0;

    static final VictorSP intakeMotor = new VictorSP(im);
    static final VictorSP pulleyMotor = new VictorSP(pm);
    
    static final DigitalInput banner  = new DigitalInput(bs);

    static final boolean ballInQueue;

    static void pulleyControl(double power) {
        pulleyMotor.set(power);
    }

    static void intake() {
        if(banner.get() == FALSE) {
            intakeMotor.set(1);
            ballInQueue = FALSE;
        } else {
            intakeMotor.set(0);
            ballInQueue = TRUE;
        }
    }

    static void outTakeForShoot() {
        if(banner.get() == TRUE) {
            intakeMotor.set(-0.3);
            ballInQueue = TRUE;
        } else {
            intakeMotor.set(0);
            ballInQueue = FALSE;
        }
    }

    static void manualIntake(double power) {
        intakeMotor.set(power);
    }
}

public class Robot extends IterativeRobot {
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	DriveSystem.drive();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
