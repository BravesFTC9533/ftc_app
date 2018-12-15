/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor bl = null;
    private DcMotor br = null;

    private DcMotor intake = null;

    private DcMotor lift = null;
    private DcMotor swing = null;

    private Servo boot;
    private Servo boxLeft;
    private Servo boxRight;

    private boolean reverse;
    private boolean boxLeftPositon = false;
    private boolean boxRightPosition = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        fl = hardwareMap.dcMotor.get("frontleft");
        fr = hardwareMap.dcMotor.get("frontright");
        bl = hardwareMap.dcMotor.get("backleft");
        br = hardwareMap.dcMotor.get("backright");

        lift = hardwareMap.dcMotor.get("lift");
        swing = hardwareMap.dcMotor.get("swing");

        boxLeft = hardwareMap.servo.get("boxleft");
        boxRight = hardwareMap.servo.get("boxright");

        intake = hardwareMap.dcMotor.get("intake");

        boot = hardwareMap.servo.get("boot");

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        swing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        swing.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        fl.setDirection(DcMotor.Direction.FORWARD);
        bl.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        //Init servos
        boot.setPosition(0);
        boxLeft.setPosition(0);
        boxRight.setPosition(0);


        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double leftPower;
            double rightPower;

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;

            //Call the conrollers
            controller1(gamepad1);
            controller2(gamepad2);

            //Check to see if the robot is in reverse
            if(!reverse) {
                leftPower = -gamepad1.left_stick_y;
                rightPower = -gamepad1.right_stick_y;
            } else {
                rightPower = -gamepad1.left_stick_y;
                leftPower = -gamepad1.right_stick_y;
            }

            fl.setPower(leftPower);
            bl.setPower(leftPower);
            br.setPower(rightPower);
            fr.setPower(rightPower);

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Lift Encoder", lift.getCurrentPosition());
            telemetry.addData("Swing Encoder", swing.getCurrentPosition());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }

    private void controller1(Gamepad gamepad) {
        if(gamepad.dpad_up) {
            if(lift.getCurrentPosition() <= -6905) {
                telemetry.log().add("Maximum Amount The Lift Motor Can Go");
            } else {
                lift.setPower(-1);
            }
        } else {
            lift.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_down) {
            if(lift.getCurrentPosition() >= 0) {
                telemetry.log().add("Minimum Amount The Lif Motor Can Go");
            } else {
                lift.setPower(1);
            }
        } else {
            lift.setPower(0);
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

    private void controller2(Gamepad gamepad) {
        if(gamepad.dpad_up) {
            if(lift.getCurrentPosition() <= -6905) {
                telemetry.log().add("Maximum Amount The Lift Motor Can Go");
            } else {
                lift.setPower(-1);
            }
        } else {
            lift.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_down) {
            if(lift.getCurrentPosition() >= 0) {
                telemetry.log().add("Minimum Amount The Lif Motor Can Go");
            } else {
                lift.setPower(1);
            }
        } else {
            lift.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_left) {
            if(swing.getCurrentPosition() >= 824) {}
            else {
                swing.setPower(1);
            }
        } else {
            swing.setPower(0);
        }
        //==========================================================================================
        if(gamepad.dpad_right) {
            if(swing.getCurrentPosition() <= 0) {}
            else {
                swing.setPower(-1);
            }
        } else {
            swing.setPower(0);
        }
        //==========================================================================================
        if(gamepad.right_bumper) {
            toggleBox(Box.RIGHT);
        }
        //==========================================================================================
        if(gamepad.left_bumper) {
            toggleBox(Box.LEFT);
        }
    }

    private void toggleBox(Box box) {
        if(box == Box.LEFT) {
            if(!boxLeftPositon) {
                boxLeft.setPosition(1);
                boxLeftPositon = true;
            } else {
                boxLeft.setPosition(0);
                boxLeftPositon = false;
            }
        } else {
            if(!boxRightPosition) {
                boxRight.setPosition(1);
                boxRightPosition = true;
            } else {
                boxRight.setPosition(0);
                boxRightPosition = false;
            }
        }
    }
}
