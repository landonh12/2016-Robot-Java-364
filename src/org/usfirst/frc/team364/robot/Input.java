package org.usfirst.frc.team364.robot;

public class Input {

	public StateControllers sc = new StateControllers();
	public InputMap im = new InputMap();
    
    public Input() {
    	//Blank constructor
    }
    
    public void updateControls() {
        //Run the drive() method of DriveSystem during teleop. Reset the gyro for driveWithGyro.
        //Call the driveWithGyro method if a button is pressed.
    	if(!im.driveButton) {
            sc.ls = im.lsy;
            sc.rs = im.rsy;
            sc.driveMode = 0;
        } else {
            sc.gyroDriveSpeed = im.lsy;
            sc.gyroAngle = 0;
            sc.driveMode = 1;
        }

        //Hang Logic
        if(im.winchUpButton) 
            sc.winchMode = 0;
        else if(im.winchDownButton) 
            sc.winchMode = 1;
        else 
            sc.winchMode = 2;

        if(im.flipUpButton) 
            sc.flipMode  = 0;
        else if(im.flipDownButton) 
            sc.flipMode  = 1;
        else 
            sc.flipMode = 2;

        //Shoot Logic
        if(sc.shootMode == 0) {
            if(im.shootButton)
                sc.shootMode = 1;
        }

        //Intake Logic
        if(sc.shootMode == 0) {
            if(im.intakeButton) {
                sc.intakeMode = 2;
            } else {
            	sc.mi = im.intakeSpeed;
                sc.intakeMode = 1;
            }
        }
        
        //Pulley Logic
        sc.ps = im.pulleySpeed;
        sc.pulleyMode = 1;
        
    }
    
    public void resetStates() {
    	sc.resetStates();
    }
    
    public void printStates() {
        System.out.println("Drive Mode: " + sc.driveMode);
        System.out.println("Winch Mode: " + sc.winchMode);
        System.out.println("Flip Mode: " + sc.flipMode);
        System.out.println("Shoot Mode: " + sc.shootMode);
        System.out.println("Intake Mode: " + sc.intakeMode);
    }
     
}
