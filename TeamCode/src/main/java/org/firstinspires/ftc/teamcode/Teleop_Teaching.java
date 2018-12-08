package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
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

        Initialize(hardwareMap, true);
        setDrive(new GTADrive(robot, driverGamePad));



        robot.motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorSwing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        waitForStart();






        while(opModeIsActive())
        {
            driverGamePad.update();
            operatorGamePad.update();

            drive.handle();


            telemetry.addData("Drive: ", "%s", new Func<String>() {
                @Override public String value() {
                    if(drive.getIsReverse()) {
                        return  "Reverse";
                    } else {
                        return  "Normal";
                    }
                }
            });
            telemetry.addLine("Controls:");
            telemetry.addLine("A)  Lift motor up (auto)");
            telemetry.addLine("Y)  Lift motor down (auto)");
            telemetry.addLine("X)  Intake +");
            telemetry.addLine("B)  Stop all motors");
            telemetry.addLine("RB) Swingarm up (auto)");
            telemetry.addLine("LB) Swingarm down (auto)");
            telemetry.addLine("DU) Swingarm +");
            telemetry.addLine("DD) Swingarm -");
            telemetry.addLine("DL) Lift motor -");
            telemetry.addLine("DR) Lift motor +");
            telemetry.addLine();


            telemetry.addData("Lift Ticks", "%d", robot.motorLift.getCurrentPosition());
            telemetry.addData("Swing Ticks", "%d", robot.motorSwing.getCurrentPosition());

            telemetry.update();
        }

        robot.stop();

    }


    private void handleOperatorGamePad(FtcGamePad gamepad, int button, boolean pressed) {
        switch (button) {
            case FtcGamePad.GAMEPAD_B:
                if (pressed) {
                    robot.motorSwing.setPower(0);
                    robot.motorSwing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                    robot.motorLift.setPower(0);
                    robot.motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
                    robot.motorSwing.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.motorSwing.setPower(config.getSwingArmPower());
                } else {
                    robot.motorSwing.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_DPAD_DOWN:
                if(pressed) {
                    robot.motorSwing.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.motorSwing.setPower(-config.getSwingArmPower());
                } else {
                    robot.motorSwing.setPower(0);
                }
                break;

            case FtcGamePad.GAMEPAD_DPAD_LEFT:
                if(pressed) {
                    robot.motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.motorLift.setPower(-1);
                } else {
                    robot.motorLift.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_DPAD_RIGHT:
                if(pressed) {
                    robot.motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.motorLift.setPower(1);
                } else {
                    robot.motorLift.setPower(0);
                }
                break;


            case FtcGamePad.GAMEPAD_RBUMPER:
                if(pressed) {

                    robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.motorLift.setTargetPosition(3500);
                    robot.motorLift.setPower(1);

                    while(opModeIsActive() && robot.motorLift.isBusy()){
                        idle();
                    }
                    robot.motorLift.setPower(0);

                    robot.motorSwing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.motorSwing.setTargetPosition(config.getMaxSwingTicks());
                    robot.motorSwing.setPower(config.getSwingArmPower());
                    while(opModeIsActive() && robot.motorSwing.isBusy()){
                        idle();
                    }
                    robot.motorSwing.setPower(0);
                    //robot.motorSwing.setPower(config.getSwingArmPower());
                } else {
                    //robot.motorSwing.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_LBUMPER:
                if(pressed) {


                    robot.motorSwing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.motorSwing.setTargetPosition(0);
                    robot.motorSwing.setPower(config.getSwingArmPower());
                    while(opModeIsActive() && robot.motorSwing.isBusy()){
                        idle();
                    }
                    robot.motorSwing.setPower(0);

                    robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.motorLift.setTargetPosition(0);
                    robot.motorLift.setPower(1);

                    while(opModeIsActive() && robot.motorLift.isBusy()){
                        idle();
                    }
                    robot.motorLift.setPower(0);

                } else {
                    //robot.motorSwing.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_BACK:
                if(pressed) {
                    drive.setIsReverse(!drive.getIsReverse());
                } else {

                }
                break;
        }
    }

    private void handleDriverGamepad(FtcGamePad gamepad, int button, boolean pressed){
        switch (button) {
            case FtcGamePad.GAMEPAD_Y:
                if(pressed) {

                    robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.motorLift.setTargetPosition(config.getMaxLiftTicks());
                    robot.motorLift.setPower(1);
                    //robot.motorLift.setPower(-1);
                }
                else {
                    //robot.motorLift.setPower(0);
                }

                break;
            case FtcGamePad.GAMEPAD_A:
                if(pressed) {
                    robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.motorLift.setTargetPosition(50);
                    robot.motorLift.setPower(1);
                } else {
                    //robot.motorLift.setPower(0);
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
