package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="New: Linear OpMode", group="New")

public class NewLinearOpMode extends LinearOpMode {

    enum Box {
        LEFT, RIGHT
    }

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private boolean reverse;
    private boolean boxLeftPositon = false;
    private boolean boxRightPosition = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Robot robot = new Robot();
        robot.init(hardwareMap);

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

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;

            //Call the conrollers
            controller1(gamepad1, robot);
            controller2(gamepad2, robot);

            //Check to see if the robot is in reverse
            if(!reverse) {
                leftPower = -gamepad1.left_stick_y;
                rightPower = -gamepad1.right_stick_y;
            } else {
                rightPower = -gamepad1.left_stick_y;
                leftPower = -gamepad1.right_stick_y;
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
        if(gamepad.dpad_up) {
            if(robot.lift.getCurrentPosition() <= -6905) {
                telemetry.log().add("Maximum Amount The Lift Motor Can Go");
            } else {
                robot.lift.setPower(-1);
            }
        } else {
            robot.lift.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_down) {
            if(robot.lift.getCurrentPosition() >= 0) {
                telemetry.log().add("Minimum Amount The Lif Motor Can Go");
            } else {
                robot.lift.setPower(1);
            }
        } else {
            robot.lift.setPower(0);
        }
        //==========================================================================================
        if(gamepad.right_bumper) {
            if(reverse) {
                reverse = false;
            } else {
                reverse = true;
            }
        }
    }

    private void controller2(Gamepad gamepad, Robot robot) {
        if(gamepad.dpad_up) {
            if(robot.lift.getCurrentPosition() <= -6905) {
                telemetry.log().add("Maximum Amount The Lift Motor Can Go");
            } else {
                robot.lift.setPower(-1);
            }
        } else {
            robot.lift.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_down) {
            if(robot.lift.getCurrentPosition() >= 0) {
                telemetry.log().add("Minimum Amount The Lif Motor Can Go");
            } else {
                robot.lift.setPower(1);
            }
        } else {
            robot.lift.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_left) {
            if(robot.swing.getCurrentPosition() >= 824) {}
            else {
                robot.swing.setPower(1);
            }
        } else {
            robot.swing.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_right) {
            if(robot.swing.getCurrentPosition() <= 0) {}
            else {
                robot.swing.setPower(-1);
            }
        } else {
            robot.swing.setPower(0);
        }
        //==========================================================================================
        if(gamepad.right_bumper) {
            toggleBox(Box.RIGHT, robot);
        }
        //==========================================================================================
        if(gamepad.left_bumper) {
            toggleBox(Box.LEFT, robot);
        }
    }

    private void toggleBox(Box box, Robot robot) {
        if(box == Box.LEFT) {
            if(!boxLeftPositon) {
                robot.boxLeft.setPosition(1);
                boxLeftPositon = true;
            } else {
                robot.boxLeft.setPosition(0);
                boxLeftPositon = false;
            }
        } else {
            if(!boxRightPosition) {
                robot.boxRight.setPosition(1);
                boxRightPosition = true;
            } else {
                robot.boxRight.setPosition(0);
                boxRightPosition = false;
            }
        }
    }
}
