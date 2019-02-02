package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {


    public DcMotor bl = null;
    public DcMotor br = null;

    public DcMotor intake = null;
    public DcMotor lift = null;
    public DcMotor mineralLift = null;

    public Servo mineralBox = null;

    public void init(HardwareMap hardwareMap) {

        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        intake = hardwareMap.dcMotor.get("intake");
        lift = hardwareMap.dcMotor.get("lift");
        mineralLift = hardwareMap.dcMotor.get("mineral_lift");

        mineralBox = hardwareMap.servo.get("mineral_box");


        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.FORWARD);
        mineralLift.setDirection(DcMotor.Direction.REVERSE);
    }

    public void updatePID(double p, double i, double d) {

        ((DcMotorEx)bl).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx)br).setVelocityPIDFCoefficients(p, i, d, 0);

        ((DcMotorEx)bl).setPositionPIDFCoefficients(p);
        ((DcMotorEx)br).setPositionPIDFCoefficients(p);

    }

}
