package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.FtcGamePad;
import org.firstinspires.ftc.teamcode.common.GTADrive;

@TeleOp(name = "TeleOp Teaching", group = "Tutorials")
public class Teleop_Teaching extends Teaching_BaseLinearOpMode implements FtcGamePad.ButtonHandler {

    @Override
    public void runOpMode() throws InterruptedException {

        driverGamePad = new FtcGamePad("driver", gamepad1, this);
        operatorGamePad = new FtcGamePad("operator", gamepad2, this);

        Initialize(hardwareMap, false);
        setDrive(new GTADrive(robot, driverGamePad));


        waitForStart();

        while(opModeIsActive())
        {
            driverGamePad.update();
            operatorGamePad.update();

            drive.handle();

        }

        robot.stop();

    }


    private void handleOperatorGamePad(FtcGamePad gamepad, int button, boolean pressed) {
        switch (button) {
            case FtcGamePad.GAMEPAD_X:
                if(pressed) {
                    //robot.togglePicker();
                    robot.motorPicker.setPower(1);
                } else {
                    robot.motorPicker.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_DPAD_UP:
                if(pressed) {
                    robot.motorExtender.setPower(1);
                } else {
                    robot.motorExtender.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_DPAD_DOWN:
                if(pressed) {
                    robot.motorExtender.setPower(-1);
                } else {
                    robot.motorExtender.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_RBUMPER:
                if(pressed) {
                    robot.motorFlipper.setPower(1);
                } else {
                    robot.motorFlipper.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_LBUMPER:
                if(pressed) {
                    robot.motorFlipper.setPower(-1);
                } else {
                    robot.motorFlipper.setPower(0);
                }
                break;
        }
    }

    private void handleDriverGamepad(FtcGamePad gamepad, int button, boolean pressed){
        switch (button) {
            case FtcGamePad.GAMEPAD_A:
                if(pressed) {
                    robot.motorLift.setPower(1);
                }
                else {
                    robot.motorLift.setPower(0);
                }

                break;
            case FtcGamePad.GAMEPAD_Y:
                if(pressed) {
                    robot.motorLift.setPower(-1);
                } else {
                    robot.motorLift.setPower(0);
                }
                break;
        }
    }
    @Override
    public void gamepadButtonEvent(FtcGamePad gamepad, int button, boolean pressed) {

        if(gamepad == operatorGamePad) {
            handleOperatorGamePad(gamepad, button, pressed);
        }
        else if (gamepad == driverGamePad) {
            handleDriverGamepad(gamepad, button, pressed);
        }

    }
}
