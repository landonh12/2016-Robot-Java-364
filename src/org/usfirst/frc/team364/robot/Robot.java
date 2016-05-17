/*
 * 2016-Java-Robot-364
 * Written by Landon Haugh
 * A complete rewrite of the 2016 robot in Java.
 * For educational purposes
 */

package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	
    //Class initialization
    public Input            input;
    public DriveSystem      driveSystem;
    public IntakeSystem     intakeSystem;
    public HangSystem       hangSystem;
    public ShootSystem      shootSystem;
    public StateControllers sc;
    public Autonomous       auto;
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();
    int    gyroAngle;
    double gyroDriveSpeed;

    public void robotInit() {
        input        = new Input();
        driveSystem  = new DriveSystem();
        intakeSystem = new IntakeSystem();
        hangSystem   = new HangSystem();
        shootSystem  = new ShootSystem();
        sc           = new StateControllers(); 
    }
    
    public void autonomousInit() {
        sc.resetStates();
    }

    public void autonomousPeriodic() {
        auto.twoball();	
	    sc.updateStates();
    }

    public void teleopInit() {
        sc.resetStates();
    }

    public void teleopPeriodic() {
        //Run the drive() method of DriveSystem during teleop. Reset the gyro for driveWithGyro.
        //Call the driveWithGyro method if a button is pressed.
    	if(!input.leftStick.getRawButton(0)) {
            sc.driveMode = 0;
        } else {
            sc.driveMode = 1;
            sc.gyroDriveSpeed = ls;
            sc.gyroAngle = 0;
        }

        //Hang Logic
        if(input.controller.getRawButton(0)) 
            sc.winchMode = 0;
        else if(input.controller.getRawButton(0)) 
            sc.winchMode = 1;
        else 
            sc.winchMode = 2;

        if(input.controller.getRawButton(0)) 
            sc.flipMode  = 0;
        else if(input.controller.getRawButton(0)) 
            sc.flipMode  = 1;
        else 
            sc.flipMode = 2;

        //Shoot Logic
        if(sc.shootMode == 1) {
            if(input.controller.getRawButton(0))
                sc.shootMode = 2;
        }

        //Intake Logic
        if(sc.shootMode == 0) {
            if(input.controller.getRawButton(0)) {
                sc.intakeMode = 1;
            } else {
                sc.intakeMode = 0;
            }
        }
        
        sc.updateStates();
        printStates();
    }

    public void printStates() {
        System.out.println("Drive Mode: " + sc.driveMode);
        System.out.println("Winch Mode: " + sc.winchMode);
        System.out.println("Flip Mode: " + sc.flipMode);
        System.out.println("Shoot Mode: " + sc.shootMode);
        System.out.println("Intake Mode: " + sc.intakeMode);
    }

    public void testPeriodic() {
    }
}
