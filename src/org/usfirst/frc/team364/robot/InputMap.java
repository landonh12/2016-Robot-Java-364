package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.Joystick;

public class InputMap {

    public Joystick leftStick  = new Joystick(0);
    public Joystick rightStick = new Joystick(1);
    public Joystick controller = new Joystick(2);
    
    public double lsy = leftStick.getY();
    public double rsy = rightStick.getY();
    
    public boolean driveButton = leftStick.getRawButton(0);
    
    public boolean winchUpButton = controller.getRawButton(0);
    public boolean winchDownButton = controller.getRawButton(0);
    
    public boolean flipUpButton = controller.getRawButton(0);
    public boolean flipDownButton = controller.getRawButton(0);
    
    public boolean shootButton = controller.getRawButton(0);

    public boolean intakeButton = controller.getRawButton(0);
    
    public double pulleySpeed = controller.getRawAxis(0);
    public double intakeSpeed = controller.getRawAxis(0);
	
}
