package org.usfirst.frc.team364.robot;

class IntakeSystem {

    private final VictorSP intakeMotor = new VictorSP(IOMap.im);
    private final VictorSP pulleyMotor = new VictorSP(IOMap.pm);
    
    private final DigitalInput banner  = new DigitalInput(IOMap.ball);

    public boolean ballInQueue;

    public void pulleyControl(double power) {
        pulleyMotor.set(power);
    }

    public void intake() {
        if(banner.get() == false) {
            manualIntake(1);
            ballInQueue = false;
        } else {
            stopIntakeMotors();
            ballInQueue = true;
        }
    }

    public void outTakeForShoot() {
        if(banner.get() == true) {
            manualIntake(-0.3);
            ballInQueue = true;
        } else {
            stopIntakeMotors();
            ballInQueue = false;
        }
    }

    public void manualIntake(double power) {
        intakeMotor.set(power);
    }

    public void stopIntakeMotors() {
        intakeMotor.set(0);
    }
    
    public boolean getBanner() {
    	return banner.get();
    }
}
