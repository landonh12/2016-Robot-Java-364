package org.usfirst.frc.team364.robot;

public class Autonomous {

    private StateControllers sc = new StateControllers();

    private final int TURN_ANGLE = 0;
    private final int AIM_MULTIPLIER = 0;
    private final int ENCODER_DISTANCE = 0;

    private int sequenceState;

    public void twoball() {
    
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
