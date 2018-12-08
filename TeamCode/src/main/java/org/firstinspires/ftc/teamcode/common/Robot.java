package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {

    public static final double     REV_COUNTS_PER_MOTOR_REV = 288;      // eg: Rev Side motor
    static final double            DRIVE_GEAR_REDUCTION    = 60.0 / 125.0 ;             // This is < 1.0 if geared UP
    static final double            WHEEL_DIAMETER_INCHES   = 3.543 ;           // For figuring circumference
    public static final double     COUNTS_PER_INCH = (REV_COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    public DcMotor motorFrontLeft = null;
    public DcMotor motorFrontRight = null;

    public DcMotor motorBackLeft = null;
    public DcMotor motorBackRight = null;


    public DcMotor motorSwing = null;
    public DcMotor lights = null;
    public DcMotor motorIntake = null;
    public DcMotor motorLift = null;

    public Servo boxLeft;
    public Servo boxRight;
    public Servo boot;

    private final boolean fourWheelDrive;

    public Config config;


    public Robot(HardwareMap hardwareMap, boolean fourWheelDrive) {
        this.config = new Config(hardwareMap.appContext);

        motorFrontLeft = hardwareMap.dcMotor.get("fl");
        motorFrontRight = hardwareMap.dcMotor.get("fr");

        motorLift = hardwareMap.dcMotor.get("lift");
        motorIntake = hardwareMap.dcMotor.get("intake");
        motorSwing = hardwareMap.dcMotor.get("swingarm");

        boxLeft = hardwareMap.servo.get("boxleft");
        boxRight = hardwareMap.servo.get("boxright");
        boot = hardwareMap.servo.get("boot");



        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorSwing.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        lights = hardwareMap.dcMotor.get("lights");


        motorIntake.setDirection(DcMotorSimple.Direction.REVERSE);

       // motorLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if(config.getLiftReverse()) {
            motorLift.setDirection(DcMotorSimple.Direction.REVERSE);

        }


        this.fourWheelDrive = fourWheelDrive;

        if(fourWheelDrive) {

            motorBackLeft = hardwareMap.dcMotor.get("bl");
            motorBackRight = hardwareMap.dcMotor.get("br");
            motorBackRight.setDirection(DcMotor.Direction.REVERSE);

            motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }



    }



    public Pair<Integer, Integer> getCurrentPosition() {
        return new Pair<>(motorFrontLeft.getCurrentPosition(), motorFrontRight.getCurrentPosition());
    }
    public Pair<Integer, Integer> calculateNewPositions(double leftInches, double rightInches ){
        int leftTarget = motorFrontLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
        int rightTarget = motorFrontRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
        return new Pair<>(leftTarget, rightTarget);
    }
    public Quad<Integer, Integer, Integer, Integer> calculateNewPositionsFourWheel(double fl, double fr, double rl, double rr) {
        int flTarget = motorFrontLeft.getCurrentPosition() + (int)(fl * COUNTS_PER_INCH);
        int frTarget = motorFrontRight.getCurrentPosition() + (int)(fr * COUNTS_PER_INCH);
        int rlTarget = motorBackLeft.getCurrentPosition() + (int)(rl * COUNTS_PER_INCH);
        int rrTarget = motorBackRight.getCurrentPosition() + (int)(rr * COUNTS_PER_INCH);
        return new Quad<>(flTarget, frTarget, rlTarget, rrTarget);
    }

    public enum Box  {
        LEFT,
        RIGHT
    }

    public void toggleLights() {
        if (Math.abs(lights.getPower()) > 0) {

            lights.setPower(0);
        } else {
            lights.setPower(config.getMaxLightBrightness());
        }

    }

    //TODO Fix toggle for Left and Right
    public void toggleBoxLeft () {
        if(boxLeft.getPosition() >= 0.0) {
            boxLeft.setPosition(0);
        } else if(boxLeft.getPosition() < 0.5) {
            boxLeft.setPosition(0.5);
        }
    }

    public void toggleBoxRight() {
        if(boxRight.getPosition() >= 0.0) {
            boxRight.setPosition(0);
        } else if(boxRight.getPosition() < 0.5) {
            boxRight.setPosition(0.5);
        }
    }

    public Quad<Integer, Integer, Integer, Integer> setNewPositionFourWheel(double inches){
        return  setNewPositionFourWheel(inches, inches, inches, inches);
    }

    public Quad<Integer, Integer, Integer, Integer> setNewPositionFourWheel(double fl, double fr, double rl, double rr) {
        Quad<Integer, Integer, Integer, Integer> target = calculateNewPositionsFourWheel(fl, fr, rl, rr);
        motorFrontLeft.setTargetPosition(target.getA());
        motorFrontRight.setTargetPosition(target.getB());

        motorBackLeft.setTargetPosition(target.getC());
        motorBackRight.setTargetPosition(target.getD());

        setRunToPosition();
        return  target;
    }

    public Pair<Integer, Integer> setNewPosition(double inches) {
        return setNewPosition(inches, inches);
    }

    public Pair<Integer, Integer> setNewPosition(double leftInches, double rightInches) {
        Pair<Integer, Integer> target = calculateNewPositions(leftInches, rightInches);
        motorFrontLeft.setTargetPosition(target.getLeft());
        motorFrontRight.setTargetPosition(target.getRight());


        // Turn On RUN_TO_POSITION
        setRunToPosition();
        return target;
    }


    public boolean isBusy() {
        return  isBusy(true);
    }
    public boolean isBusy(boolean allMotors){
        if(fourWheelDrive) {
            return isBusyFourWheelDrive(allMotors);
        } else {
            return  isBusyTwoWheelDrive(allMotors);
        }
    }

    private boolean isBusyTwoWheelDrive(boolean allMotors){
        if(allMotors) {
            return motorFrontLeft.isBusy() || motorFrontRight.isBusy();
        } else {
            return motorFrontLeft.isBusy() && motorFrontRight.isBusy();
        }
    }
    private boolean isBusyFourWheelDrive(boolean allMotors){
        if(allMotors) {
            return motorFrontLeft.isBusy() ||
                    motorFrontRight.isBusy() ||
                    motorBackLeft.isBusy() ||
                    motorBackRight.isBusy();// ||
        } else {
            return motorFrontLeft.isBusy() &&
                    motorFrontRight.isBusy() &&
                    motorBackLeft.isBusy() &&
                    motorBackRight.isBusy();
        }
    }


    public void SetPIDCoefficients(DcMotor.RunMode runMode, double new_p, double new_i, double new_d) {
        PIDCoefficients new_pid = new PIDCoefficients(new_p, new_i, new_d);

        ((DcMotorEx)motorFrontLeft).setPIDCoefficients(runMode, new_pid);
        ((DcMotorEx)motorFrontRight).setPIDCoefficients(runMode, new_pid);
        if(fourWheelDrive)
        {
            ((DcMotorEx)motorBackLeft).setPIDCoefficients(runMode, new_pid);
            ((DcMotorEx)motorBackRight).setPIDCoefficients(runMode, new_pid);

        }
    }

    public void updatePID(double p, double i, double d) {
        PIDCoefficients pidNew = new PIDCoefficients(p, i, d);


        ((DcMotorEx)motorFrontLeft).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx)motorFrontRight).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx)motorBackLeft).setVelocityPIDFCoefficients(p, i, d, 0);
        ((DcMotorEx)motorBackRight).setVelocityPIDFCoefficients(p, i, d, 0);

        ((DcMotorEx)motorFrontLeft).setPositionPIDFCoefficients(p);
        ((DcMotorEx)motorFrontRight).setPositionPIDFCoefficients(p);
        ((DcMotorEx)motorBackLeft).setPositionPIDFCoefficients(p);
        ((DcMotorEx)motorBackRight).setPositionPIDFCoefficients(p);
//
//        ((DcMotorEx)motorFrontLeft).setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);
//        ((DcMotorEx)motorFrontRight).setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);
//        ((DcMotorEx)motorBackLeft).setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);
//        ((DcMotorEx)motorBackRight).setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);

//
//        ((DcMotorEx)motorFrontLeft).setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);
//        ((DcMotorEx)motorFrontRight).setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);
//        ((DcMotorEx)motorBackLeft).setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);
//        ((DcMotorEx)motorBackRight).setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);

    }


    public void setPower(double left, double right) {
        motorFrontLeft.setPower(left);
        motorFrontRight.setPower(right);
        if(fourWheelDrive)
        {
            motorBackLeft.setPower(left);
            motorBackRight.setPower(right);
        }
    }

    public void setRunUsingEncoders(){
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void setRunToPosition() {
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void setRunWithoutEncoders() {
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setMode(DcMotor.RunMode mode) {
        motorFrontLeft.setMode(mode);
        motorFrontRight.setMode(mode);
        if(this.fourWheelDrive){
            motorBackLeft.setMode(mode);
            motorBackRight.setMode(mode);
        }
    }

    public void stop() {
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        if(this.fourWheelDrive) {
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
        }
    }
}
