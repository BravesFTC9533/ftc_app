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

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot = new Robot();
        robot.init(hardwareMap);

        double leftPower;
        double rightPower;

        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        robot.bl.setPower(leftPower);
        robot.bl.setPower(rightPower);

        swinglift = new SwingAndLiftController(robot);
        config = new Config(hardwareMap.appContext);

        driverGamePad = new FtcGamePad("driver", gamepad1, this);
        operatorGamePad = new FtcGamePad("operator", gamepad2, this);

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        //Init servos

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {


            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Lift Encoder", robot.lift.getCurrentPosition());
            telemetry.addData("Mineral Lift Encoder", robot.mineralLift.getCurrentPosition());
            telemetry.update();
        }
    }


    private void handleDriverGamepad(FtcGamePad gamepad, int button, boolean pressed) {
        switch (button) {
            case FtcGamePad.GAMEPAD_DPAD_UP:
                if (pressed) {
                    robot.lift.setPower(1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_DPAD_DOWN:
                if (pressed) {
                    robot.lift.setPower(-1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_Y:
                if (pressed) {
                    robot.mineralLift.setPower(1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_A:
                if (pressed) {
                    robot.mineralLift.setPower(-1);
                } else {
                    robot.lift.setPower(0);
                }
                break;
            case FtcGamePad.GAMEPAD_RBUMPER:
                if (pressed) {
                    if (robot.mineralBox.getPosition() == 0) {
                        robot.mineralBox.setPosition(1);
                    } else {
                        robot.mineralBox.setPosition(0);
                    }

                }
        }
    }

    private void handleOperatorGamePad(FtcGamePad gamepad, int button, boolean pressed){
        switch(button) {

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