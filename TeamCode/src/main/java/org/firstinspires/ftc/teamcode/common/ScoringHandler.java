package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class ScoringHandler {

    private final DcMotor intakeWheel;
    private final DcMotor lift;
    private final DcMotor swing;
    private final Config config;


    private ScoringPositions scoringPosition;

    public enum ScoringPositions {
        DOWN, SILVER, GOLD
    }


    public enum States {
        START, LIFTING_SILVER, LIFTING_GOLD, SWINGING_UP, SWINGING_DOWN, DUMPING_SILVER, DUMPING_GOLD, DROPPING_DOWN
    }


    public States currentState;

    public ScoringHandler(DcMotor intakeWheel, DcMotor lift, DcMotor swing, Config config) {

        this.intakeWheel = intakeWheel;
        this.lift = lift;
        this.swing = swing;
        this.config = config;

        scoringPosition = ScoringPositions.DOWN;
        currentState = States.START;

    }


    public void ToggleIntake() {
        if(intakeWheel.getPower() == 0) {
            intakeWheel.setPower(config.getIntakePower());
        } else {
            intakeWheel.setPower(0);
        }

    }

    public void SetMode(ScoringPositions position) {
        this.scoringPosition = position;
    }




    public void Handle() {
        switch(scoringPosition){
            case DOWN:

                break;
            case GOLD:

                break;
            case SILVER:

                break;
        }

    }


    public void START_TO_LIFTING() {

    }




}
