package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;

class DriveSystem {

    private final VictorSP leftFront  = new VictorSP(IOMap.lfdm);
    private final VictorSP leftRear   = new VictorSP(IOMap.lrdm);
    private final VictorSP rightFront = new VictorSP(IOMap.rfdm);
    private final VictorSP rightRear  = new VictorSP(IOMap.rrdm);

    private final AnalogGyro gyro = new AnalogGyro(IOMap.gyro);

    private final RobotDrive rd = new RobotDrive(leftFront, leftRear, rightFront, rightRear);

    private PIDControl pid = new PIDControl();
    private double output;
    
    public void drive(double leftPower, double rightPower) {
        rd.tankDrive(leftPower, rightPower);
    }

    public void driveWithGyro(double speed, double angle) {
    	output = pid.PIDController(0.5, 0.1, 0.1, angle, gyro.getAngle());
        rd.arcadeDrive(speed, output);
    }
    
    public void resetGyro() {
    	gyro.reset();
    }
    
    public void stopDriveMotors() {
        drive(0, 0);
    }
}