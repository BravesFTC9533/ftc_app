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


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="New: Linear OpMode", group="New")

public class NewLinearOpMode extends LinearOpMode {

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

    private boolean reverse;

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

        boot.setPosition(0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
//            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
//            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            //Call the conrollers
            controller1(gamepad1);
            controller2(gamepad2);

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.

            // Send calculated power to wheels

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

            // Show the elapsed game time and wheel power.
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
        if(gamepad.y) {
           boot.setPosition(1);
        }
        if(gamepad.a) {
            boot.setPosition(0);
        }
    }
}
