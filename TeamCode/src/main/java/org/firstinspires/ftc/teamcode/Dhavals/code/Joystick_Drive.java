package org.firstinspires.ftc.teamcode.Dhavals.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Joystick_Drive", group = "Linear Opmode")

public class Joystick_Drive extends LinearOpMode {

    DcMotor leftMotor;
    DcMotor rightMotor;

    double power = 0.5;
    public void runOpMode () throws InterruptedException {

        //Object telemetry;
        telemetry.addData ("Status", "Initialized");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("Left_Motor");
        rightMotor = hardwareMap.dcMotor.get("Right_Motor");

        waitForStart();
       // runtime.reset();

        while (opModeIsActive()){

            leftMotor.setPower(-gamepad1.left_stick_y);
            rightMotor.setPower(-gamepad1.right_stick_y);
        }
    }

}
