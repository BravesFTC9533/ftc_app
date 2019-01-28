package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {

    public DcMotor fl = null;
    public DcMotor fr = null;
    public DcMotor bl = null;
    public DcMotor br = null;

    public DcMotor lift = null;
    public DcMotor mineralDrop = null;
    public DcMotor swing = null;
    public DcMotor intake = null;
    public DcMotor lights = null;

    public Servo objectDrop = null;
    public Servo boot = null;

    public void init(HardwareMap hardwareMap) {

        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        lift = hardwareMap.dcMotor.get("lift");
        swing = hardwareMap.dcMotor.get("swingarm");
        intake = hardwareMap.dcMotor.get("intake");
        lights = hardwareMap.dcMotor.get("lights");
        mineralDrop = hardwareMap.dcMotor.get("mineral_drop");

        objectDrop = hardwareMap.servo.get("object_drop");
        boot = hardwareMap.servo.get("boot");

        fl.setDirection(DcMotor.Direction.FORWARD);
        bl.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void updatePID(double p, double i, double d) {

        ((DcMotorEx)fl).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx)fr).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx)bl).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx)br).setVelocityPIDFCoefficients(p, i, d, 0);

        ((DcMotorEx)fl).setPositionPIDFCoefficients(p);
        ((DcMotorEx)fr).setPositionPIDFCoefficients(p);
        ((DcMotorEx)bl).setPositionPIDFCoefficients(p);
        ((DcMotorEx)br).setPositionPIDFCoefficients(p);

    }

}
