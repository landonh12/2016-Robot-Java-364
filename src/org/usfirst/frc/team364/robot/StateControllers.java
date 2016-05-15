package org.usfirst.frc.team364.robot;

class StateControllers {
	
    public Input 		input        = new Input();
    public DriveSystem  driveSystem  = new DriveSystem();
    public IntakeSystem intakeSystem = new IntakeSystem();
    public HangSystem   hangSystem   = new HangSystem();
    public ShootSystem  shootSystem  = new ShootSystem();

    public enum Mode {
        MANUAL, GYRO, IN, OUT, OFF, WAIT, SHOOT
    }

    public Mode driveMode;
    public Mode winchMode;
    public Mode flipMode;
    public Mode intakeMode;
    public Mode shootMode;
    public double gyroDriveSpeed;
    public double gyroAngle;
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();

    /*
     * Default States:
     * driveMode  = 2
     * winchMode  = 2
     * flipMode   = 2
     * intakeMode = 0
     * shootMode  = 0
     */
    public void resetStates() {
        driveMode  = OFF;
        winchMode  = OFF;
        flipMode   = OFF;
        intakeMode = OFF;
        shootMode  = OFF;
    }

    public void updateStates() { 
        //Drive Controller
        switch(driveMode) {
            case MANUAL:
                driveSystem.drive(ls, rs);
                driveSystem.resetGyro();
                break;
            case GYRO:
                driveSystem.driveWithGyro(gyroDriveSpeed, gyroAngle);
                break;
            case OFF:
                driveSystem.stopDriveMotors();
                break;
        }

        //Hang Controllers
        switch(winchMode) {
            case MANUAL:
                hangSystem.manualWinchControl(0);
                break;
            case OFF:
                hangSystem.stopWinchMotor();
                break;
        }

        switch(flipMode) {
            case MANUAL:
                hangSystem.manualFlipControl(0);
                break;
            case OFF:
                hangSystem.stopFlipMotor();
                break;
        }

        //Intake Controller
        switch(intakeMode) {
            case MANUAL:
                intakeSystem.manualIntake(input.controller.getRawAxis(0));
                break;
            case IN:
                intakeSystem.intake();
                break;
            case OUT:
                intakeSystem.outTakeForShoot();
                break;
            case SHOOT:
                intakeSystem.manualIntake(1);
                break;
            case OFF:
                intakeSystem.stop();
                break;
        }
        
        //Shoot Controller
        switch(shootMode) {
            case OFF:
               shootSystem.stopShooterMotors();
               break;
            case WAIT:
               shootSystem.speedControl(0.9);
               intakeMode = OUT;
               if(!intakeSystem.ballInQueue) shootMode = 2;
               break;
            case SHOOT:
               shootSystem.speedControl(0.9);
               intakeMode = SHOOT;
               if(intakeSystem.ballInQueue) shootMode = 0;
               break;
        }

    }

}
