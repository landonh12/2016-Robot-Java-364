package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.Joystick;

class Input {

    public final Joystick leftStick  = new Joystick(0);
    public final Joystick rightStick = new Joystick(1);
    public final Joystick controller = new Joystick(2);

    public void updateControls() {
        //Run the drive() method of DriveSystem during teleop. Reset the gyro for driveWithGyro.
        //Call the driveWithGyro method if a button is pressed.
    	if(!leftStick.getRawButton(0)) {
            sc.driveMode = 0;
        } else {
            sc.driveMode = 1;
            sc.gyroDriveSpeed = ls;
            sc.gyroAngle = 0;
        }

        //Hang Logic
        if(controller.getRawButton(0)) 
            sc.winchMode = 0;
        else if(controller.getRawButton(0)) 
            sc.winchMode = 1;
        else 
            sc.winchMode = 2;

        if(controller.getRawButton(0)) 
            sc.flipMode  = 0;
        else if(controller.getRawButton(0)) 
            sc.flipMode  = 1;
        else 
            sc.flipMode = 2;

        //Shoot Logic
        if(sc.shootMode == 0) {
            if(controller.getRawButton(0))
                sc.shootMode = 1;
        }

        //Intake Logic
        if(sc.shootMode == 0) {
            if(controller.getRawButton(0)) {
                sc.intakeMode = 1;
            } else {
                sc.intakeMode = 0;
            }
        }
        
    }
}
