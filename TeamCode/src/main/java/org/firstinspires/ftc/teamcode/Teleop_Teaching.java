package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.FtcGamePad;
import org.firstinspires.ftc.teamcode.common.GTADrive;

@TeleOp(name = "TeleOp Teaching", group = "Tutorials")
public class Teleop_Teaching extends Teaching_BaseLinearOpMode implements FtcGamePad.ButtonHandler {

    Config config;

    @Override
    public void runOpMode() throws InterruptedException {

        config = new Config(hardwareMap.appContext);

        driverGamePad = new FtcGamePad("driver", gamepad1, this);
        operatorGamePad = new FtcGamePad("operator", gamepad2, this);

        Initialize(hardwareMap, false);
        setDrive(new GTADrive(robot, driverGamePad));


        waitForStart();

        robot.motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




        while(opModeIsActive())
        {
            driverGamePad.update();
            operatorGamePad.update();

            drive.handle();


            telemetry.addLine("Controls:");
            telemetry.addLine("A)  Lift motor +");
            telemetry.addLine("Y)  Lift motor -");
            telemetry.addLine("X)  Intake +");
            telemetry.addLine("B)  ");
            telemetry.addLine("RB) Swingarm +");
            telemetry.addLine("LB) Swingarm -");
            telemetry.addLine();


            telemetry.addData("Lift Ticks", "%d", robot.motorLift.getCurrentPosition());
            telemetry.addData("Swing Ticks", "%d", robot.motorSwing.getCurrentPosition());

            telemetry.update();
        }

        robot.stop();

    }


    private void handleOperatorGamePad(FtcGamePad gamepad, int button, boolean pressed) {
        switch (button) {

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

            case FtcGamePad.GAMEPAD_X:
                if(pressed) {
                    //robot.togglePicker();
                    robot.motorIntake.setPower(config.getIntakePower());
                } else {
                    robot.motorIntake.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_DPAD_UP:
                if(pressed) {

                } else {

                }
                break;
            case FtcGamePad.GAMEPAD_DPAD_DOWN:
                if(pressed) {

                } else {

                }
                break;
            case FtcGamePad.GAMEPAD_RBUMPER:
                if(pressed) {
                    robot.motorSwing.setPower(config.getSwingArmPower());
                } else {
                    robot.motorSwing.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_LBUMPER:
                if(pressed) {
                    robot.motorSwing.setPower(-config.getSwingArmPower());
                } else {
                    robot.motorSwing.setPower(0);
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
