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
    public Autonomous       auto;
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();
    double gyroAngle;
    double gyroDriveSpeed;

    public void robotInit() {
    }
    
    public void autonomousInit() {
    	input.resetStates();
    }

    public void autonomousPeriodic() {
        auto.twoball();	
    }

    public void teleopInit() {
    	input.resetStates();
    }

    public void teleopPeriodic() {
        input.updateControls();   
        input.printStates();
    }

    public void testPeriodic() {
    }
}
