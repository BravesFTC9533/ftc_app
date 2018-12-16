package org.firstinspires.ftc.teamcode.common;

import android.content.Context;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous_Teaching;


public class AutoHelper {


    public Robot robot;





    public enum TurnDirection {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }

    public enum GoldPosition {
        CENTER,
        LEFT,
        RIGHT,
        UNKNOWN
    }


    public AutoHelper(Robot robot) {
        this.robot = robot;

    }

    public void turnDegrees(TurnDirection direction, double degrees, double speed) {


        //330 = 3/4 of 360
        //robot.COUNTS_PER_INCH
        final double onedegreeticks = 1.207*2;
        final double WHEEL_DIAMETER_INCHES_FOR_TURNING = 3.543;
        final double DESIRED_MOVEMENT_TICKS = onedegreeticks * degrees;

        //x = (312.993*90)/103

        //3.4777 * 90 = 312.993 == 103
        //     y * 90 =   273.5 == 90


        double turnInches = (WHEEL_DIAMETER_INCHES_FOR_TURNING * Math.PI) * (DESIRED_MOVEMENT_TICKS / robot.REV_COUNTS_PER_MOTOR_REV );
        if(direction == TurnDirection.CLOCKWISE) {
            turnInches = -turnInches;
        }
        PIDencoderDrive(speed, -turnInches, turnInches);

        //encoderDrive(speed, -turnInches, turnInches, 5.0, false);
    }
    public void turnDegrees(TurnDirection direction, double degrees) {
        turnDegrees(direction, degrees, 0.75);
    }


    public void turn90(TurnDirection direction, double speed){
        turnDegrees(direction, 90, speed);
    }
    public void turn90(TurnDirection direction) {
        turn90(direction, 0.75);
    }

    public void turn45(TurnDirection direction, double speed) {
        turnDegrees(direction, 45, speed);
    }

    public void turn45(TurnDirection direction) {
        turn45(direction, 0.75);
    }


    public void PIDencoderDrive(double targetSpeed, double inches) {
        PIDencoderDrive(targetSpeed, inches, inches);
    }
    public void PIDencoderDrive(double targetSpeed, double leftInches, double rightInches){
        robot.setNewPositionFourWheel(leftInches, rightInches);
        robot.setRunToPosition();
        robot.setPower(targetSpeed , targetSpeed);

    }


    public void StopDrive() {
        robot.stop();
        robot.setRunUsingEncoders();
    }




}
