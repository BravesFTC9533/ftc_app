package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Config;

public class Robot {


    public DcMotor bl = null;
    public DcMotor br = null;

    public DcMotor intake = null;
    public DcMotor lift = null;
    public DcMotor lights = null;
    public DcMotor mineralLift = null;

    public Servo mineralBox = null;
    public Config config;

    public void init(HardwareMap hardwareMap) {

        config = new Config(hardwareMap.appContext);

        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        intake = hardwareMap.dcMotor.get("intake");
        lift = hardwareMap.dcMotor.get("lift");
        lights = hardwareMap.dcMotor.get("lights");
        mineralLift = hardwareMap.dcMotor.get("minerallift");

        mineralBox = hardwareMap.servo.get("mineralbox");

        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.FORWARD);
        mineralLift.setDirection(DcMotor.Direction.REVERSE);
    }

    public void updatePID(double p, double i, double d) {

        ((DcMotorEx) bl).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx) br).setVelocityPIDFCoefficients(p, i, d, 0);

        ((DcMotorEx) bl).setPositionPIDFCoefficients(p);
        ((DcMotorEx) br).setPositionPIDFCoefficients(p);
    }

}
