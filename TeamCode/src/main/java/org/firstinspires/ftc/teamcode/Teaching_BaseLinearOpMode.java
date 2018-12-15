package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.common.Easing;
import org.firstinspires.ftc.teamcode.common.FtcGamePad;
import org.firstinspires.ftc.teamcode.common.IDrive;
import org.firstinspires.ftc.teamcode.common.Quad;
import org.firstinspires.ftc.teamcode.common.Robot;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public abstract class Teaching_BaseLinearOpMode extends LinearOpMode {


    public Robot robot;
    public IDrive drive;
    public FtcGamePad driverGamePad;
    public FtcGamePad operatorGamePad;

    private boolean fourWheelDrive;

    protected static final String VUFORIA_KEY = "AeWceoD/////AAAAGWvk7AQGLUiTsyU4mSW7gfldjSCDQHX76lt9iPO5D8zaboG428r" +
            "dS9WN0+AFpAlc/g4McLRAQIb5+ijFCPJJkLc+ynXYdhljdI2k9R4KL8t3MYk/tbmQ75st9VI7//2vNkp0JHV6oy4HXltxVFcEbtBYeT" +
            "BJ9CFbMW+0cMNhLBPwHV7RYeNPZRgxf27J0oO8VoHOlj70OYdNYos5wvDM+ZbfWrOad/cpo4qbAw5iB95T5I9D2/KRf1HQHygtDl8/O" +
            "tDFlOfqK6v2PTvnEbNnW1aW3vPglGXknX+rm0k8b0S7GFJkgl7SLq/HFNl0VEIVJGVQe9wt9PB6bJuxOMMxN4asy4rW5PRRBqasSM7O" +
            "Ll4W";

    protected static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    protected static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    protected static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    protected TFObjectDetector tfod;


    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    protected static final float mmPerInch        = 25.4f;
    protected static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    protected static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor

    // Select which camera you want use.  The FRONT camera is the one on the same side as the screen.
    // Valid choices are:  BACK or FRONT
    protected static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

    protected OpenGLMatrix lastLocation = null;
    protected boolean targetVisible = false;

    protected VuforiaLocalizer vuforia;

    public void Initialize(HardwareMap hardwareMap, boolean fourWheelDrive) {

        this.fourWheelDrive = fourWheelDrive;
        robot = new Robot(hardwareMap, fourWheelDrive);

//        driverGamePad = new FtcGamePad("DriverGamepad", gamepad1, this);
//        operatorGamePad = new FtcGamePad("OperatorGamepad", gamepad2, this);


//        driverGamePad = new FtcGamePad("driver", gamepad1, this);
//        operatorGamePad = new FtcGamePad("operator", gamepad2);

    }

    List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
    protected VuforiaTrackables targetsRoverRuckus;

    protected void initializeVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets that for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables.addAll(targetsRoverRuckus);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * This Rover Ruckus sample places a specific target in the middle of each perimeter wall.
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        /**
         * To place the BlueRover target in the middle of the blue perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Then, we translate it along the Y axis to the blue perimeter wall.
         */
        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        /**
         * To place the RedFootprint target in the middle of the red perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 180 around the field's Z axis so the image is flat against the red perimeter wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative Y axis to the red perimeter wall.
         */
        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        /**
         * To place the FrontCraters target in the middle of the front perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 90 around the field's Z axis so the image is flat against the front wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative X axis to the front perimeter wall.
         */
        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        /**
         * To place the BackSpace target in the middle of the back perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it -90 around the field's Z axis so the image is flat against the back wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the X axis to the back perimeter wall.
         */
        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        /**
         * Create a transformation matrix describing where the phone is on the robot.
         *
         * The coordinate frame for the robot looks the same as the field.
         * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
         * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
         *
         * The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
         * pointing to the LEFT side of the Robot.  It's very important when you test this code that the top of the
         * camera is pointing to the left side of the  robot.  The rotation angles don't work if you flip the phone.
         *
         * If using the rear (High Res) camera:
         * We need to rotate the camera around it's long axis to bring the rear camera forward.
         * This requires a negative 90 degree rotation on the Y axis
         *
         * If using the Front (Low Res) camera
         * We need to rotate the camera around it's long axis to bring the FRONT camera forward.
         * This requires a Positive 90 degree rotation on the Y axis
         *
         * Next, translate the camera lens to where it is on the robot.
         * In this example, it is centered (left to right), but 110 mm forward of the middle of the robot, and 200 mm above ground level.
         */

        final int CAMERA_FORWARD_DISPLACEMENT  = 154;   // eg: Camera is 110 mm in front of robot center
        final int CAMERA_VERTICAL_DISPLACEMENT = 100;   // eg: Camera is 200 mm above ground
        final int CAMERA_LEFT_DISPLACEMENT     = 129;     // eg: Camera is ON the robot's center line

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }
    }
    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    protected void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void setDrive(IDrive drive){
        this.drive = drive;
    }


    public void turnDegrees(Autonomous_Teaching.TurnDirection direction, double degrees, double speed) {


        //robot.COUNTS_PER_INCH
        final double onedegreeticks = 3.04;
        final double WHEEL_DIAMETER_INCHES_FOR_TURNING = 3.51;
        final double DESIRED_MOVEMENT_TICKS = onedegreeticks * degrees;

        //x = (312.993*90)/103

        //3.4777 * 90 = 312.993 == 103
        //     y * 90 =   273.5 == 90


        double turnInches = (WHEEL_DIAMETER_INCHES_FOR_TURNING * Math.PI) * (DESIRED_MOVEMENT_TICKS / robot.REV_COUNTS_PER_MOTOR_REV );
        if(direction == Autonomous_Teaching.TurnDirection.CLOCKWISE) {
            turnInches = -turnInches;
        }
        PIDencoderDrive(speed, -turnInches, turnInches, 5.0);

        //encoderDrive(speed, -turnInches, turnInches, 5.0, false);
    }
    public void turnDegrees(Autonomous_Teaching.TurnDirection direction, double degrees) {
        turnDegrees(direction, degrees, 0.75);
    }


    public void turn90(Autonomous_Teaching.TurnDirection direction, double speed){
        turnDegrees(direction, 90, speed);
    }
    public void turn90(Autonomous_Teaching.TurnDirection direction) {
        turn90(direction, 0.75);
    }

    public void turn45(Autonomous_Teaching.TurnDirection direction, double speed) {
        turnDegrees(direction, 45, speed);
    }

    public void turn45(Autonomous_Teaching.TurnDirection direction) {
        turn45(direction, 0.75);
    }





    protected boolean locateVuforiaTarget() {
        // check all the trackable target to see which one (if any) is visible.
        targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telemetry.addData("Visible Target", trackable.getName());
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }
        return  targetVisible;
    }


    private void setVelocity(double tps) {
        ((DcMotorEx)robot.motorFrontLeft).setVelocity(tps);
        ((DcMotorEx)robot.motorFrontRight).setVelocity(tps);
        ((DcMotorEx)robot.motorBackLeft).setVelocity(tps);
        ((DcMotorEx)robot.motorBackRight).setVelocity(tps);
    }


    public void PIDencoderDrive(double targetSpeed, double leftInches, double rightInches, double timeoutSeconds){
        Quad<Integer, Integer, Integer, Integer> positions = robot.setNewPositionFourWheel(leftInches, rightInches);

        robot.setRunToPosition();
        robot.setPower(targetSpeed , targetSpeed);

        ElapsedTime timer = new ElapsedTime();
        while(opModeIsActive() && robot.isBusy() && timer.seconds() < timeoutSeconds) {
            idle();
        }

        robot.stop();
        robot.setRunUsingEncoders();
    }


    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void  encoderDrive_old(double speed,
                             double leftInches, double rightInches,
                             double timeoutS, boolean holdPosition) {


        int targetLeft, targetRight, currentRight, currentLeft;
        int differenceLeft, differenceRight;

        boolean maxed = false;
        ElapsedTime runtime = new ElapsedTime();

        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            if(fourWheelDrive){
                robot.setNewPositionFourWheel(leftInches, rightInches, leftInches, rightInches);
            }
            else{
                robot.setNewPosition(leftInches, rightInches);
            }


            currentLeft = robot.motorFrontLeft.getCurrentPosition();
            int newLeftTarget = currentLeft + (int)(leftInches * robot.COUNTS_PER_INCH);

            int scale = newLeftTarget - currentLeft;


            // reset the timeout time and start motion.
            runtime.reset();

            double currentSpeed = 0;
            double multiplier = 0;


            robot.setPower(currentSpeed , currentSpeed);


            int lastPosition = 0;
            double lastRuntime = 0;

            int slowdownTick = 150;
            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.isBusy())) {
                targetLeft = robot.motorFrontLeft.getTargetPosition();
                targetRight =robot.motorFrontRight.getTargetPosition();
                currentLeft = robot.motorFrontLeft.getCurrentPosition();
                currentRight = robot.motorFrontRight.getCurrentPosition();
                differenceLeft = Math.abs(Math.abs(targetLeft) - Math.abs(currentLeft));

                if(maxed) {
                    //double newSpeed = Easing.Interpolate(1 - (differenceLeft / scale), Easing.Functions.CubicEaseIn);

//                    double ease = Easing.Interpolate((float)differenceLeft / (float)scale, Easing.Functions.SineEaseIn);
//

                    //double newSpeed = (1 - ease) * 600; //  (Easing.Interpolate(1f - ((float)differenceLeft / (float)scale), Easing.Functions.QuinticEaseIn)) * 600;

                    double newSpeed = 0;
                    if( ((float)differenceLeft / (float)scale) < .5f) {
                        newSpeed = (600 * speed) * .25;
                    }
                    else
                    {
                        newSpeed = (600 * speed);
                    }

                    //if(newSpeed < 100) { newSpeed = 100; }
                    setVelocity(newSpeed);

//                    currentSpeed = newSpeed;
//                    if(currentSpeed < 0.4) {
//                        currentSpeed = 0.4;
//                    }
                }
                else if(currentSpeed < speed) {
                    multiplier = Easing.Interpolate(runtime.seconds() *2, Easing.Functions.CubicEaseOut);
                    if(multiplier>=1) { maxed = true; }

                    currentSpeed = speed * multiplier;
                    if(currentSpeed > speed) {
                        currentSpeed = speed;
                    }
                    setVelocity(currentSpeed * 600);
                    //robot.setPower(currentSpeed, currentSpeed);
                }


                telemetry.addLine()
                        .addData("Multiplier", "%7f", multiplier)
                        .addData("Speed", "%7f", currentSpeed);

                //telemetry.update();

                if(currentSpeed >= speed) {
                    currentSpeed = speed;
                    maxed = true;


                }
//                if(!maxed) {
//                    robot.setPower(currentSpeed, currentSpeed);
//                }

                //600 tps
                //((125)*288) / 60 = tps



                // Display it for the driver.
                telemetry.addLine().addData("Target",  "Running to %7d :%7d",
                        targetLeft,
                        targetRight);
                telemetry.addLine().addData("Current",  "Running at %7d :%7d",
                        currentLeft,
                        currentRight);

            }

            if(holdPosition==false) {
                // Stop all motion;
                robot.stop();

                // Turn off RUN_TO_POSITION
                robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
    }

}
