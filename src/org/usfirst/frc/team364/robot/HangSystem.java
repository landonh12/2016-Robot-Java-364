package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class HangSystem {

    private final VictorSP flipMotor  = new VictorSP(IOMap.fm);
    private final VictorSP winchMotor = new VictorSP(IOMap.wm);

    public HangSystem() {
    	//Blank constructor
    }
    
    public void manualFlipControl(double power) {
        flipMotor.set(power);	
    }

    public void manualWinchControl(double power) {
        winchMotor.set(power);
    }

    public void stopFlipMotor() {
        flipMotor.set(0);
    }

    public void stopWinchMotor() {
        winchMotor.set(0);
    }

}
