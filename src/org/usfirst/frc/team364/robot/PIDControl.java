package org.usfirst.frc.team364.robot;

public class PIDControl {
	
	// Initialization values
	private double previous_error = 0;
	private double error = 0;
	private double integral = 0;
	private double derivative = 0;
	private double output = 0;
	
	public PIDControl() {
		//Blank constructor
	}
	

	// Resets all PID values.
	public void resetPIDControl() {
		previous_error = 0;
		error = 0;
		integral = 0;
		derivative = 0;
		output = 0;
	}
	
	// Runs a PID Loop with the given parameters and returns an output.
	public double PIDController(double Kp, double Ki, double Kd, double setpoint, double process_var) {
		error = setpoint - process_var;
		integral = integral + error;
		derivative = error - previous_error;
		output = Kp*error + Ki*integral + Kd*derivative;
		previous_error = error;
		return output;
	}
}
