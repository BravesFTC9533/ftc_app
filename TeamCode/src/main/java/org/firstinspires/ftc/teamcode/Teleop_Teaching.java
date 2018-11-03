package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.FtcGamePad;
import org.firstinspires.ftc.teamcode.common.GTADrive;

@TeleOp(name = "TeleOp Teaching", group = "Tutorials")
public class Teleop_Teaching extends Teaching_BaseLinearOpMode implements FtcGamePad.ButtonHandler {

    @Override
    public void runOpMode() throws InterruptedException {
        Initialize(hardwareMap, true);
        setDrive(new GTADrive(robot, driverGamePad));

        DcMotor lights = hardwareMap.dcMotor.get("lights");

        waitForStart();

        while(opModeIsActive())
        {
            drive.handle();

            double value = operatorGamePad.getRightTrigger();
            value = Math.min(robot.config.getMaxLightBrightness(), value);
            lights.setPower(value);
        }

        robot.stop();

    }

    @Override
    public void gamepadButtonEvent(FtcGamePad gamepad, int button, boolean pressed) {




    }
}
