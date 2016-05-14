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
    public Input 		    input;
    public DriveSystem      driveSystem;
    public IntakeSystem     intakeSystem;
    public HangSystem       hangSystem;
    public ShootSystem      shootSystem;
    public StateControllers stateControllers;
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();
    int    gyroAngle;
    double gyroDriveSpeed;

    //Switch statement variables
    public int     driveMode         = 0;
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
        stateControllers.updateStates();
    }

    public void teleopPeriodic() {
        //Run the drive() method of DriveSystem during teleop. Reset the gyro for driveWithGyro.
        //Call the driveWithGyro method if a button is pressed.
    	if(!input.leftStick.getRawButton(0)) {
            stateControllers.driveMode =  0;
        } else {
            stateControllers.driveMode = 1;
            stateControllers.gyroDriveSpeed = ls;
            stateControllers.gyroAngle = 0;
        }

        //Hang Logic
        if(input.controller.getRawButton(0)) 
            stateControllers.winchMode = 0;
        else if(input.controller.getRawButton(0)) 
            stateControllers.winchMode = 1;
        else 
            stateControllers.winchMode = 2;

        if(input.controller.getRawButton(0)) 
            stateControllers.flipMode  = 0;
        else if(input.controller.getRawButton(0)) 
            stateControllers.flipMode  = 1;
        else 
            stateControllers.flipMode = 2;

        //Shoot Logic
        if(shootMode == 1) {
            if(input.controller.getRawButton(0))
                stateControllers.shootMode = 2;
        }

        //Intake Logic
        if(shootMode == 0) {
            if(input.controller.getRawButton(0)) {
                stateControllers.intakeMode = 1;
            } else {
                stateControllers.intakeMode = 0;
            }
        }
        
        stateControllers.updateStates();
    }

    public void testPeriodic() {
    }
    
}
