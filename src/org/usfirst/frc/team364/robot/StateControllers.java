package org.usfirst.frc.team364.robot;

import org.usfirst.frc.team364.robot.subsystems.DriveSystem;
import org.usfirst.frc.team364.robot.subsystems.HangSystem;
import org.usfirst.frc.team364.robot.subsystems.IntakeSystem;
import org.usfirst.frc.team364.robot.subsystems.ShootSystem;

import edu.wpi.first.wpilibj.Timer;

public class StateControllers {
	
    public Input input = new Input();
    public DriveSystem driveSystem = new DriveSystem();
    public IntakeSystem intakeSystem = new IntakeSystem();
    public HangSystem hangSystem = new HangSystem();
    public ShootSystem shootSystem = new ShootSystem();

    public int pulleyMode = 0;
    public int driveMode = 0;
    public int winchMode = 0;
    public int flipMode = 0;
    public int intakeMode = 0;
    public int shootMode = 0;
    
    public double ls = 0;
    public double rs = 0;
    public double gyroDriveSpeed = 0;
    public double gyroAngle = 0;
    public int encoderTicks = 0;
    public double ps = 0;
    public double mi = 0;
    
    int time = 0;
    
    public StateControllers() {
    	//Blank constructor
    }
    
    /*
     * Default States: Stop state is 0
     */
    public void resetStates() {
        driveMode  = 0;
        winchMode  = 0;
        flipMode   = 0;
        intakeMode = 0;
        shootMode  = 0;
        pulleyMode = 0;
    }

    public void updateStates() { 
        //Drive Controller
        switch(driveMode) {
            case 0:
                driveSystem.stopDriveMotors();
                break;
            case 1:
                driveSystem.drive(ls, rs);
                driveSystem.resetGyro();
                break;
            case 2:
                driveSystem.driveWithGyro(gyroDriveSpeed, gyroAngle);
                break;
            case 3:
                driveSystem.driveToEncoderCount(encoderTicks, gyroAngle);
        }

        //Hang Controllers
        switch(winchMode) {
            case 0:
                hangSystem.stopWinchMotor();
                break;
            case 1:
                hangSystem.manualWinchControl(1);
                break;
            case 2:
            	hangSystem.manualWinchControl(-1);
            	break;
        }

        switch(flipMode) {
            case 0:
                hangSystem.stopFlipMotor();
                break;
            case 1:
                hangSystem.manualFlipControl(1);
                break;
            case 2:
            	hangSystem.manualFlipControl(-1);
            	break;
        }

        //Pulley Controller
        switch(pulleyMode) {
            case 0:
                intakeSystem.stopPulley();
                break;
            case 1:
                intakeSystem.pulleyControl(ps);
                break;
        }

        //Intake Controller
        switch(intakeMode) {
            case 0:
                intakeSystem.stopIntakeMotors();
                break;
            case 1:
            	//Get controller axis for intake
                intakeSystem.manualIntake(mi);
                break;
            case 2:
            	//Intake ball until sensor
                intakeSystem.intake();
                break;
            case 3:
            	//Outtake ball for shooter rev up
                intakeSystem.outTakeForShoot();
                break;
            case 4:
            	//Feed ball into shooter
                intakeSystem.manualIntake(1);
                break;
        }
        
        //Shoot Controller
        switch(shootMode) {
            case 0:
               shootSystem.stopShooterMotors();
               time = 0;
               break;
            case 1:
               shootSystem.speedControl(0.9);
               intakeMode = 2;
               Timer.delay(0.01);
               time = time + 1;
               if(!intakeSystem.ballInQueue && time > 100) 
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
