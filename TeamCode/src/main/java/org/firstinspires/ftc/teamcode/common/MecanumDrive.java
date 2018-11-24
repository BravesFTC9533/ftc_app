package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by 9533 on 2/3/2018.
 */

public class MecanumDrive implements IDrive {

    private final FtcGamePad driverGamepad;
    private final Robot robot;

    private static final double MIN_SPEED = 0.2;

    boolean reverse = false;

    public MecanumDrive(Robot robot, FtcGamePad driveGamepad){
        this.driverGamepad = driveGamepad;
        this.robot = robot;
    }

    public boolean getIsReverse(){
        return reverse;
    }

    public void setIsReverse(boolean value){
        reverse = value;
    }

    public void handle(){

        //mechDrive.Drive(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        double h, v, r;

        h = -driverGamepad.getLeftStickX();
        v = driverGamepad.getLeftStickY();
        r = driverGamepad.getRightStickX();


        if(Math.abs(h) < MIN_SPEED) {
            h = 0;
        }
        if(Math.abs(v) < MIN_SPEED) {
            v = 0;
        }
        if(Math.abs(r) < MIN_SPEED){
            r = 0;
        }

        if(getIsReverse()) {
            h *= -1;
            v *= -1;
        }


        h = clipMotorPower(h);
        v = clipMotorPower(v);
        r = clipMotorPower(r);

        // add vectors
        double frontLeft =  v-h+r;
        double frontRight = v+h-r;
        double backRight =  v-h-r;
        double backLeft =   v+h+r;

        // since adding vectors can go over 1, figure out max to scale other wheels
        double max = Math.max(
                Math.abs(backLeft),
                Math.max(
                        Math.abs(backRight),
                        Math.max(
                                Math.abs(frontLeft), Math.abs(frontRight)
                        )
                )
        );
        // only need to scale power if max > 1
        if(max > 1){
            frontLeft = scalePower(frontLeft, max);
            frontRight = scalePower(frontRight, max);
            backLeft = scalePower(backLeft, max);
            backRight = scalePower(backRight, max);
        }


        //robot.Drive(frontLeft, frontRight, backLeft, backRight);




    }

    @Override
    public void drive(double ly, double lx, double rx) {

    }

    @Override
    public void drive(double left, double right) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setMode(DcMotor.RunMode runMode) {

    }

    @Override
    public void driveToPosition(int leftPosition, int rightPosition) {

    }


    // Scale motor power based on the max for all wheels
    // 1, 1, 1, 3 will become .33, .33, .33, 1
    public static double scalePower(double value, double max){
        if(max == 0){return  0;}
        return  value / max;
    }

    // motor power clipping helper
    public static double clipMotorPower(double value){
        return Range.clip(value, -1, 1);
    }

    public static double scale(double power){
        int modifier = 1;

        if (power == 0 )
        {
            return 0;
        }

        if(power < 0){
            modifier *= -1;
        }

        return  (power * power * modifier);
    }
}

