package org.usfirst.frc.team364.robot.subsystems;

import org.usfirst.frc.team364.robot.IOMap;

import edu.wpi.first.wpilibj.VictorSP;

public class ShootSystem {

    private final VictorSP shootMotor1 = new VictorSP(IOMap.sm1);
    private final VictorSP shootMotor2 = new VictorSP(IOMap.sm2);

    public ShootSystem() {
    	//Blank constructor.. don't know if this does anything.
    }
    
    public void speedControl(double power) {
        shootMotor1.set(power);
        shootMotor2.set(power);
    }

    public void stopShooterMotors() {
        shootMotor1.set(0);
        shootMotor2.set(0);
    }
}
