package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

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

    public DcMotor lights = null;

    private boolean lightsOn;

    public DcMotor motorIntake = null;

    public DcMotor motorLift = null;

    private final boolean fourWheelDrive;

    public Config config;


//    public Servo servoIntakeLeft = null;
//    public Servo servoIntakeRight = null;
//
//    public DcMotor motorArmLift = null;
//    public DcMotor motorArmSlide = null;

    public Robot(HardwareMap hardwareMap, boolean fourWheelDrive) {
        this.config = new Config(hardwareMap.appContext);

        motorFrontLeft = hardwareMap.dcMotor.get("Front_Left");
        motorFrontRight = hardwareMap.dcMotor.get("Front_Right");
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if(config.getROBOTSPECS() == Config.RobotSpecs.REAL) {
            lights = hardwareMap.dcMotor.get("lights");
        }

        motorLift = hardwareMap.dcMotor.get("Lift_Motor");
       // motorLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if(config.getLiftReverse()) {
            motorLift.setDirection(DcMotorSimple.Direction.REVERSE);

        }


        this.fourWheelDrive = fourWheelDrive;

        if(fourWheelDrive) {

            motorBackLeft = hardwareMap.dcMotor.get("Back_Left");
            motorBackRight = hardwareMap.dcMotor.get("Back_Right");
            motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

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

    public void toggleLights() {
        if (Math.abs(lights.getPower()) > 0) {

            lights.setPower(0);
        } else {
            lights.setPower(config.getMaxLightBrightness());
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
