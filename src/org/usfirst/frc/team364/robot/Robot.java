/*
 * 2016-Java-Robot-364
 * Written by Landon Haugh
 * A complete rewrite of the 2016 robot in Java.
 * For educational purposes
 */

package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	
    public Input      input = new Input();
    public Autonomous auton = new Autonomous();

    public void robotInit() {
    	input.resetStates();
    }
    
    public void autonomousInit() {
    	input.resetStates();
    }

    public void autonomousPeriodic() {
        auton.twoball();	
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
