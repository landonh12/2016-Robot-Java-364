package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class ShootSystem {

    private final VictorSP shootMotor1 = new VictorSP(IOMap.sm1);
    private final VictorSP shootMotor2 = new VictorSP(IOMap.sm2);

    public void speedControl(double power) {
        shootMotor1.set(power);
        shootMotor2.set(power);
    }

    public void stopShooterMotors() {
        shootMotor1.set(0);
        shootMotor2.set(0);
    }
}
