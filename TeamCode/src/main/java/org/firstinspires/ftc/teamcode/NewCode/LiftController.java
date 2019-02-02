package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Config;

public class LiftController {

    private final Robot robot;
    private final int MAX_LIFT_TICKS = 2000;
    private Config config;

    public enum LiftDirection {
        UP, DOWN
    }

    public LiftController(Robot robot, Config config) {
        this.robot = robot;
        this.config = config;
    }

    public void LiftRobotUsingEncoder(int encoderTicks) {
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(robot.lift.getCurrentPosition() + encoderTicks >= config.getMaxLiftTicks()) {
            encoderTicks = config.getMaxLiftTicks();
        }
        robot.lift.setTargetPosition(encoderTicks);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void LiftMineralLift(LiftDirection liftDirection) {
        if(liftDirection == LiftDirection.UP) {
            if(robot.mineralLift.getCurrentPosition() < config.getMaxMineralLiftTicks()) {
                robot.mineralLift.setPower(1);
            }
        } else if(liftDirection == LiftDirection.DOWN) {
            if(robot.mineralLift.getCurrentPosition() > 0 ) {
                robot.mineralLift.setPower(-1);
            }
        }
    }

    public void SetLiftToTop() {
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(config.getMaxLiftTicks());
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void SetMineralLiftToTop() {
        robot.mineralLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.mineralLift.setTargetPosition(config.getMaxMineralLiftTicks());
        robot.mineralLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void SetLiftToBottom() {
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(0);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void SetMineralLiftToBottom() {
        robot.mineralLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.mineralLift.setTargetPosition(0);
        robot.mineralLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void LiftMineralLiftUsingEncoder(int encoderTicks) {
        robot.mineralLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(robot.mineralLift.getCurrentPosition() + encoderTicks >= config.getMaxMineralLiftTicks()) {
            encoderTicks = config.getMaxMineralLiftTicks();
        }
        robot.mineralLift.setTargetPosition(encoderTicks);
        robot.mineralLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void LiftRobot(LiftDirection liftDirection) {
        if(liftDirection == LiftDirection.UP) {
            if(robot.lift.getCurrentPosition() < config.getMaxLiftTicks()) {
                robot.lift.setPower(1);
            }
        } else if(liftDirection == LiftDirection.DOWN) {
            if(robot.lift.getCurrentPosition() > 0) {
                robot.lift.setPower(-1);
            }
        }
    }

    public void ToggleMineralBox() {
        if(robot.mineralBox.getPosition() == 0) {
            robot.mineralBox.setPosition(1);
        } else {
            robot.mineralBox.setPosition(0);
        }
    }

    public void StopRobotLift() {
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setPower(0);
    }

    public void StopMineralLift() {
        robot.mineralLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mineralLift.setPower(0);
    }

}
