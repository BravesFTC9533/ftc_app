package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.FtcGamePad;


@TeleOp(name="New: Linear OpMode", group="New")

public class NewLinearOpMode extends LinearOpMode implements FtcGamePad.ButtonHandler {

    enum Box {
        LEFT, RIGHT
    }

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private SwingAndLiftController swinglift;

    public FtcGamePad driverGamePad;
    public FtcGamePad operatorGamePad;

    private boolean reverse;


    private Robot robot;
    private Config config;

    private Utils utils;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot = new Robot();
        robot.init(hardwareMap);


        swinglift = new SwingAndLiftController(robot);
        config = new Config(hardwareMap.appContext);
        utils = new Utils(robot);

        driverGamePad = new FtcGamePad("driver", gamepad1, this);
        operatorGamePad = new FtcGamePad("operator", gamepad2, this);

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).



        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        //Init servos
        robot.boot.setPosition(0);
        robot.boxLeft.setPosition(0);
        robot.boxRight.setPosition(0);


        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double leftPower;
            double rightPower;

            driverGamePad.update();
            operatorGamePad.update();

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;

            //Check to see if the robot is in reverse
            if(!reverse) {
                leftPower = -gamepad1.left_stick_y;
                rightPower = -gamepad1.right_stick_y;
            } else {
                rightPower = gamepad1.left_stick_y;
                leftPower = gamepad1.right_stick_y;
            }

            robot.fl.setPower(leftPower);
            robot.bl.setPower(leftPower);
            robot.br.setPower(rightPower);
            robot.fr.setPower(rightPower);



            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Lift Encoder", robot.lift.getCurrentPosition());
            telemetry.addData("Swing Encoder", robot.swing.getCurrentPosition());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }

    private void controller1(Gamepad gamepad, Robot robot) {

    }



    private void handleDriverGamepad(FtcGamePad gamepad, int button, boolean pressed) {
        switch (button) {
            case FtcGamePad.GAMEPAD_DPAD_UP:
                if (pressed) {
                    robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.lift.setPower(1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_DPAD_DOWN:
                if(pressed) {
                    robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.lift.setPower(-1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_B:
                if(pressed) {
                    if(robot.lights.getPower() != 0) {
                        robot.lights.setPower(config.getMaxLightBrightness());
                    } else {
                        robot.lights.setPower(0);
                    }
                }
                break;

            //======================================================================================
            case FtcGamePad.GAMEPAD_RBUMPER:
                if(pressed) {
                    reverse = !reverse;
                }
                break;
            case FtcGamePad.GAMEPAD_Y:
                if(pressed) {
                    robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.lift.setTargetPosition(0);
                    robot.lift.setPower(1);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_A:
                if(pressed) {
                    if(robot.lift.getCurrentPosition() > 0) {
                        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.lift.setTargetPosition(0);
                        robot.lift.setPower(1);
                    } else {
                        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.lift.setTargetPosition(3500);
                        robot.lift.setPower(1);
                    }
                }
        }
    }

    private void handleOperatorGamePad(FtcGamePad gamepad, int button, boolean pressed){
        switch(button) {
            case FtcGamePad.GAMEPAD_DPAD_UP:
                if (pressed) {
                    robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.lift.setPower(1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_DPAD_DOWN:
                if(pressed) {
                    robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.lift.setPower(-1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_DPAD_LEFT:
                if(pressed) {
                    robot.swing.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.swing.setPower(1);
                } else {
                    robot.swing.setPower(0);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_DPAD_RIGHT:
                if(pressed) {
                    robot.swing.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.swing.setPower(-1);
                } else {
                    robot.swing.setPower(0);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_LBUMPER:
                if(pressed) {
                    utils.toggleBox(Box.LEFT);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_RBUMPER:
                if(pressed) {
                    utils.toggleBox(Box.RIGHT);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_Y:
                if(pressed) {
                    robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.lift.setTargetPosition(3600);
                    robot.lift.setPower(1);

                    while(opModeIsActive() && robot.lift.isBusy()){
                        idle();
                    }
                    robot.lift.setPower(0);

                    robot.swing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.swing.setTargetPosition(config.getMaxSwingTicks());
                    robot.swing.setPower(config.getSwingArmPower());
                    while(opModeIsActive() && robot.swing.isBusy()){
                        idle();
                    }
                    robot.swing.setPower(0);
                }

                else {
                    //robot.swing.setPower(0);
                }
                break;
            //======================================================================================
            case FtcGamePad.GAMEPAD_A:
                if(pressed) {
                    robot.boxLeft.setPosition(0);
                    robot.boxRight.setPosition(0);

                    robot.swing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.swing.setTargetPosition(5);
                    robot.swing.setPower(config.getSwingArmPower());
                    while(opModeIsActive() && robot.swing.isBusy()){
                        idle();
                    }

                    robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robot.lift.setTargetPosition(50);
                    robot.lift.setPower(1);

                    while(opModeIsActive() && robot.lift.isBusy()){
                        idle();
                    }
                    robot.lift.setPower(0);

                } else {
                    //robot.swing.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_X:
                if(pressed){
                    robot.intake.setPower(1);
                } else {
                    robot.intake.setPower(0);
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
