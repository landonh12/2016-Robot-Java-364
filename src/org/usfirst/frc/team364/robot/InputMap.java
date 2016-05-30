package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.Joystick;

public class InputMap {

	//Create new joystick objects
    public Joystick leftStick  = new Joystick(0);
    public Joystick rightStick = new Joystick(1);
    public Joystick controller = new Joystick(2);
    
    /*
     * Create variables that return the Y axis values of the joysticks
     * for driving, and a button to control drive states
     */
    public double lsy = leftStick.getY();
    public double rsy = rightStick.getY();
    public boolean driveButton = leftStick.getRawButton(0);
    
    // Get buttons for controlling the scaling mechanism. 
    public boolean winchUpButton = controller.getRawButton(0);
    public boolean winchDownButton = controller.getRawButton(0);
    public boolean flipUpButton = controller.getRawButton(0);
    public boolean flipDownButton = controller.getRawButton(0);
    
    // Button for controlling the shoot code. 
    public boolean shootButton = controller.getRawButton(0);

    // Button and manual axis value for controlling the intake motor.
    public boolean intakeButton = controller.getRawButton(0);
    public double intakeSpeed = controller.getRawAxis(0);
    
    // Get the axis for manual control of the pulley.
    public double pulleySpeed = controller.getRawAxis(0);

	
}
