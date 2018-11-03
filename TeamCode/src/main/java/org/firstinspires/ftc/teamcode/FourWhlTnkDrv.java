package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="4_Wheel_Tank_Drive", group = "Linear OpMode")
//Disabled
public class FourWhlTnkDrv extends LinearOpMode {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor liftMotor;
    DcMotor mineralGrabber;

    //double power = 1.0;
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        frontLeft = hardwareMap.dcMotor.get("Front_Left");
        frontRight = hardwareMap.dcMotor.get("Front_Right");
        backLeft = hardwareMap.dcMotor.get("Back_Left");
        backRight = hardwareMap.dcMotor.get("Back_Right");
        liftMotor = hardwareMap.dcMotor.get("Lift_Motor");
        mineralGrabber = hardwareMap.dcMotor.get("Mineral_Grabber");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();

        while (opModeIsActive()) {
            frontLeft.setPower(-gamepad1.left_stick_y);
            frontRight.setPower(-gamepad1.right_stick_y);
            backLeft.setPower(-gamepad1.left_stick_y);
            backRight.setPower(-gamepad1.right_stick_y);

            if (gamepad2.a) {
                liftMotor.setPower(0.5);

            } else {
                liftMotor.setPower(0.0);
            }

            if (gamepad2.y) {
                liftMotor.setPower(-0.5);
            } else {
                liftMotor.setPower(0.0);
            }

            if (gamepad2.left_trigger > 0.5) {
                mineralGrabber.setPower(0.5);
            } else {
                mineralGrabber.setPower(0.0);
            }
            if (gamepad2.right_trigger > 0.5) {
                mineralGrabber.setPower(-0.5);
            } else {
                mineralGrabber.setPower(0.0);
            }
        }
    }
}