package org.usfirst.frc.team364.robot;

public class StateControllers {
	
    public Input 		input        = new Input();
    public DriveSystem  driveSystem  = new DriveSystem();
    public IntakeSystem intakeSystem = new IntakeSystem();
    public HangSystem   hangSystem   = new HangSystem();
    public ShootSystem  shootSystem  = new ShootSystem();

    public int pulleyMode;
    public int driveMode;
    public int winchMode;
    public int flipMode;
    public int intakeMode;
    public int shootMode;
    
    public double gyroDriveSpeed;
    public double gyroAngle;
    public int encoderTicks;
    
    double ls = input.leftStick.getY();
    double rs = input.rightStick.getY();
    double pulleyPower = input.controller.getRawAxis(0);
    
    public StateControllers() {
    	//Blank constructor
    }
    
    /*
     * Default States:
     * driveMode  = 2
     * winchMode  = 1
     * flipMode   = 1
     * intakeMode = 4
     * shootMode  = 0
     * pulleyMode = 0
     */
    public void resetStates() {
        driveMode  = 2;
        winchMode  = 2;
        flipMode   = 2;
        intakeMode = 4;
        shootMode  = 0;
        pulleyMode = 0;
    }

    public void updateStates() { 
        //Drive Controller
        switch(driveMode) {
            case 0:
                driveSystem.drive(ls, rs);
                driveSystem.resetGyro();
                break;
            case 1:
                driveSystem.driveWithGyro(gyroDriveSpeed, gyroAngle);
                break;
            case 2:
                driveSystem.stopDriveMotors();
                break;
            case 3:
                driveSystem.driveToEncoderCount(encoderTicks, gyroAngle);
        }

        //Hang Controllers
        switch(winchMode) {
            case 0:
                hangSystem.manualWinchControl(1);
                break;
            case 1:
            	hangSystem.manualWinchControl(-1);
            	break;
            case 2:
                hangSystem.stopWinchMotor();
                break;
        }

        switch(flipMode) {
            case 0:
                hangSystem.manualFlipControl(1);
                break;
            case 1:
            	hangSystem.manualFlipControl(-1);
            	break;
            case 2:
                hangSystem.stopFlipMotor();
                break;
        }

        switch(pulleyMode) {
            case 0:
                intakeSystem.stopPulley();
                break;
            case 1:
                intakeSystem.pulleyControl(pulleyPower);
                break;
        }

        //Intake Controller
        switch(intakeMode) {
            case 0:
            	//Get controller axis for intake
                intakeSystem.manualIntake(input.controller.getRawAxis(0));
                break;
            case 1:
            	//Intake ball until sensor
                intakeSystem.intake();
                break;
            case 2:
            	//Outtake ball for shooter rev up
                intakeSystem.outTakeForShoot();
                break;
            case 3:
            	//Feed ball into shooter
                intakeSystem.manualIntake(1);
                break;
            case 4:
                intakeSystem.stopIntakeMotors();
                break;
        }
        
        //Shoot Controller
        switch(shootMode) {
            case 0:
               shootSystem.stopShooterMotors();
               break;
            case 1:
               shootSystem.speedControl(0.9);
               intakeMode = 2;
               if(!intakeSystem.ballInQueue) 
            	   shootMode = 2;
               break;
            case 2:
               shootSystem.speedControl(0.9);
               intakeMode = 3;
               if(intakeSystem.ballInQueue) 
            	   shootMode = 0;
               break;
        }

    }

}
