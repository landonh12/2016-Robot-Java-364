package org.usfirst.frc.team364.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Autonomous {

    private StateControllers sc = new StateControllers();
    private NetworkTable nt = NetworkTable.getTable("/");

    private double visionCenterX = nt.getNumber("visionCenterX", 0);
    private final int TURN_ANGLE = 40;
    private final int GYRO_ZERO = 0;
    private final int AIM_MULTIPLIER = 0;
    private final int ENCODER_DISTANCE = 0;

    private int sequenceState;
    private double aim_angle = visionCenterX * AIM_MULTIPLIER;

    public Autonomous() {
    	//Blank constructor
    }
    
    public void twoball() {
    
        switch(sequenceState) {
            case 0:
                //Intake down
                sc.ps = 1;
                sc.pulleyMode = 1;
                Timer.delay(1000);
                sc.pulleyMode = 0;
                sequenceState = 1;
                break;
            case 1:
                //Go through low bar
                sc.encoderTicks = 1;
                sc.gyroAngle = GYRO_ZERO;
                sc.driveMode = 3;
                /*
                if(driveSystem.encoderTicks >= sc.encoderTicks) {
                    sc.driveMode = 2;
                    sequenceState = 2;
                }
                */
                break;
            case 2:
                //Turn
                sc.gyroDriveSpeed = 0;
                sc.gyroAngle = TURN_ANGLE;
                sc.driveMode = 1;
                /*
                if(driveSystem.getAngle == 40) {
                    sc.driveMode = 2;
                    sequenceState = 3;
                }
                */
                break;
            case 3:
                //Aim
                sc.gyroDriveSpeed = 0;
                sc.gyroAngle = aim_angle; //Get angle from NT
                sc.driveMode = 1;
                /*
                if(driveSystem.getAngle == 0) {
                    sc.driveMode = 2;
                    sequenceState = 4;
                }
                */
                break;
            case 4:
                //Shoot
                sc.shootMode = 1;
                break;
            case 5:
                //Turn back
                break;
            case 6:
                //Come back through low bar
                break;
            case 7:
                //Wait for ball
                break;
            case 8:
                //Go back through low bar
                break;
            case 9:
                //Turn
                break;
            case 10:
                //Aim
                break;
            case 11:
                //Shoot
                break;
        }

    }

    public void oneball() {
        
        switch(sequenceState) {
            case 0:
                //Intake down
                break;
            case 1:
                //Go through low bar
                break;
            case 2:
                //Turn
                break;
            case 3:
                //Aim
                break;
            case 4:
                //Shoot
                break;
        }

    }

    public void cross() {
        
        switch(sequenceState) {
            case 0:
                //Intake down
                break;
            case 1:
                //Go 
                break;
            case 2:
                //Stop
                break;
        }

    }

}
