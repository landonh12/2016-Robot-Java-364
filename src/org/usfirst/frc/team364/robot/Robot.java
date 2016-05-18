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
    public StateControllers sc;
    public Autonomous       auto;
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();
    double gyroAngle;
    double gyroDriveSpeed;

    public void robotInit() {
        sc = new StateControllers(); 
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
        input.updateControls();   
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
