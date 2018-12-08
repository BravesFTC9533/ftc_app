package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.FtcGamePad;
import org.firstinspires.ftc.teamcode.common.IDrive;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;

@TeleOp(name = "Practice Mech bot", group = "Practice")
@Disabled
public class PracticeBot_Teleop extends LinearOpMode implements FtcGamePad.ButtonHandler {


    IDrive drive;

    DcMotor bl = null;
    DcMotor fl = null;
    DcMotor br = null;
    DcMotor fr = null;

    FtcGamePad driverGamepad = null;

    @Override
    public void runOpMode() throws InterruptedException {

        driverGamepad = new FtcGamePad("driver", gamepad1, this);

        DcMotor fl = hardwareMap.dcMotor.get("fl");
        DcMotor fr = hardwareMap.dcMotor.get("fr");
        DcMotor bl = hardwareMap.dcMotor.get("bl");
        DcMotor br = hardwareMap.dcMotor.get("br");

        drive = new MecanumDrive(fl, fr, bl, fr, driverGamepad);


        waitForStart();

        while(opModeIsActive()){
            drive.handle();
        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    @Override
    public void gamepadButtonEvent(FtcGamePad gamepad, int button, boolean pressed) {

    }
}
