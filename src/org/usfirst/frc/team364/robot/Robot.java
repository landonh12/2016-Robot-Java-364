package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Joystick;

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

    static final int hm = 0;
    static final int wm = 0;

    static final VictorSP hangMotor  = new VictorSP(hm);
    static final VictorSP winchMotor = new VictorSP(wm);

    static void manualHangControl(double power) {
        hangMotor.set(power);	
    }

    static void manualWinchControl(double power) {
        winchMotor.set(power);
    }

}

class DriveSystem {

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

    static boolean ballInQueue;

    static void pulleyControl(double power) {
        pulleyMotor.set(power);
    }

    static void intake() {
        if(banner.get() == false) {
            intakeMotor.set(1);
            ballInQueue = false;
        } else {
            intakeMotor.set(0);
            ballInQueue = true;
        }
    }

    static void outTakeForShoot() {
        if(banner.get() == true) {
            intakeMotor.set(-0.3);
            ballInQueue = true;
        } else {
            intakeMotor.set(0);
            ballInQueue = false;
        }
    }

    static void manualIntake(double power) {
        intakeMotor.set(power);
    }
}

public class Robot extends IterativeRobot {
	
    public void robotInit() {
    }
    
    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
    }

    public void teleopPeriodic() {
    	//Run the drive() method of DriveSystem during teleop.
    	DriveSystem.drive();
	}
    
    public void testPeriodic() {
    }
    
}
