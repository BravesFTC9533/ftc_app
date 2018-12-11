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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="New: OpMode", group="New")


public class MainOpMode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private Utils utils;

    private DcMotor fl= null;
    private DcMotor fr = null;
    private DcMotor bl = null;
    private DcMotor br = null;

    private DcMotor swing = null;
    private DcMotor lift = null;

    private DcMotor intake = null;
    private DcMotor lights = null;

    public Servo boxLeft = null;
    public Servo boxRight = null;

    private boolean reverse = false;

    //TODO Fix because numbers are just placeholders
    private final int TICKS_FOR_LIFT_ENCODER = 2240 / 2;
    private final int TICKS_FOR_SWING_ENCODER = 2240 / 2;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    enum Box {
        LEFT, RIGHT
    }

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized, Good Luck");
        telemetry.update();

        //Init our motors
        fl = hardwareMap.dcMotor.get("frontleft");
        fr = hardwareMap.dcMotor.get("frontright");
        bl = hardwareMap.dcMotor.get("backleft");
        br = hardwareMap.dcMotor.get("backright");

        intake = hardwareMap.dcMotor.get("intake");
        lights = hardwareMap.dcMotor.get("lights");

        boxLeft = hardwareMap.servo.get("boxleft");
        boxRight = hardwareMap.servo.get("boxright");

        lift = hardwareMap.dcMotor.get("lift");
        swing = hardwareMap.dcMotor.get("swing");

        //Init Servos

        //TODO Might have to switch one of the boxes 0 to 1
        boxRight.setPosition(0);
        boxLeft.setPosition(0);

        //Reset Encoders To 0
        //Encoders Do 2240 ticks per revelation

        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Set motor encoders
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double leftPower;
        double rightPower;

        double drive = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        if(!reverse) {
            leftPower = -gamepad1.left_stick_y;
            rightPower = gamepad1.right_stick_y;
        } else {
            rightPower = gamepad1.left_stick_y;
            leftPower = -gamepad1.right_stick_y;
        }

        controller1();
        controller2();


        fl.setPower(leftPower);
        fr.setPower(rightPower);
        bl.setPower(leftPower);
        br.setPower(rightPower);


        // Show the elapsed game time and wheel power.
        //Remove when ready for real competition
        telemetry.addData("Lift Motor Encoder", lift.getCurrentPosition());
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Reverse", reverse);
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        //Stop All Motors To Be Safe
        stopAll();

        telemetry.log().add("Stopped Robot");
    }

    private void controller1() {
        if(gamepad1.b) {
            lights.setPower(0.8);
        } else {
            lights.setPower(0);
        }
        if(gamepad1.right_bumper) {
            telemetry.log().add("Reversed Robot");
            reverse = false;
        }
        if(gamepad1.left_bumper) {
            telemetry.log().add("Reversed Robot To Normal");
            reverse = true;
        }
        if(gamepad1.dpad_up) {
            lift.setPower(1);
        } else {
            lift.setPower(0);
        }
        if(gamepad1.dpad_down) {
            //Check to make sure the arm isn't already down
            lift.setPower(-1);
        } else {
            lift.setPower(0);
        }
    }

    private void controller2() {
        if(gamepad2.a) {
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }
        if(gamepad2.dpad_up) {
            //Add a stop for maximum height
            lift.setPower(1);

        } else {
            lift.setPower(0);
        }
        if(gamepad2.dpad_down) {
            //Check to make sure the arm isn't already down
            lift.setPower(-1);

        } else {
            lift.setPower(0);
        }
        if(gamepad2.dpad_right) {
            //Check to make sure that the swing arm isn't already out
            swing.setPower(1);
        } else {
            swing.setPower(0);
        }
        if(gamepad2.dpad_left) {
            //Check to make sure the arm isn't already down
            swing.setPower(-1);
        } else {
            swing.setPower(0);
        }
        if(gamepad2.y) {
            intake.setPower(1);
        } else {
            intake.setPower(0);
        }
    }

    public void stopAll() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        swing.setPower(0);
        lift.setPower(0);
        intake.setPower(0);
        lights.setPower(0);
    }

}
