package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Config;

public class LiftHandler {

    private DcMotor motor;
    private Config config;


    public enum LiftDirections {
        UP, DOWN, NONE
    }

    private LiftDirections liftDirection;

    public LiftHandler(DcMotor motor, Config config) {
        this.motor = motor;
        this.config = config;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void Stop() {
        this.motor.setPower(0);
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void PullLiftDown (){
        this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(0);
        this.motor.setPower(1);
    }

    public void PushLiftUp (){
        this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(config.getMaxLiftTicks());
        this.motor.setPower(1);
    }



}
