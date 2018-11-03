package org.firstinspires.ftc.teamcode.Hot_Mess;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

// @Autonomous(name="HotMess: Forward", group="Linear OpMode")
//@Disabled
public class Forward extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    DcMotor leftMotor;
    DcMotor rightMotor;
    double power = 0.5;

    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("Left_Motor");
        rightMotor = hardwareMap.dcMotor.get("Right_Motor");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        leftMotor.setPower(power);
        rightMotor.setPower(power);

        while (opModeIsActive() && (runtime.seconds() < 2.0)){
        }

        power = (0.0);
        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }
}