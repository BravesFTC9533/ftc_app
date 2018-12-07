package org.firstinspires.ftc.teamcode;

import android.sax.TextElementListener;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.GTADrive;

import java.util.List;
import java.util.Random;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

@Autonomous(name="Main: Autonomous", group="Competition Code" +
        "" +
        "")
//@Disabled
public class Autonomous_Teaching extends Teaching_BaseLinearOpMode {

    enum TurnDirection {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }

    enum GoldPosition {
        CENTER,
        LEFT,
        RIGHT,
        UNKNOWN
    }

    Config config;

    private boolean loop;
    private GoldPosition state;

    private GoldPosition gpCenter = GoldPosition.CENTER;
    private GoldPosition gpRight = GoldPosition.RIGHT;
    private GoldPosition gpLeft = GoldPosition.LEFT;
    private GoldPosition gpUnknown = GoldPosition.UNKNOWN;

    private static final long pauseTimeBetweenSteps = 1000;

    int max = 3;
    int min = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        // We can control the number of lines shown in the log
        telemetry.log().setCapacity(10);


        telemetry.log().add("Setting up hardware");
        telemetry.update();
        Initialize(hardwareMap, true); //Four wheel drive is true meaning that it will run a four wheel drive
        setDrive(new GTADrive(robot, driverGamePad)); //That sets the robot into GTA mode

        config = new Config(hardwareMap.appContext);
        //ComposeTelemetryPreStart();


        telemetry.log().add("Setting up vuforia");
        telemetry.update();
        initializeVuforia();
        telemetry.log().add("Setting up tfod");
        telemetry.update();
        initTfod();

        double speed = config.getSpeed();

        telemetry.log().add("Completed Initialization. Good Luck!!!");
        telemetry.update();


        holdStartingPosition();

        waitForStart();

        waitForDelayStart();

        dropToGround();

        state = DetectObjectsNonStop();

        PushOffGoldObject(speed, state);

        //Silver(speed);


    }


    private GoldPosition DetectObjectsNonStop() {
        loop = false;
        robot.toggleLights();

        ElapsedTime timer = new ElapsedTime();
        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            int missingDetectionCounter = 0;

            while (opModeIsActive() && timer.seconds() < 2) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getRecognitions();
                    //List<Recognition> ourCollection = new ArrayList<>();

                    if (updatedRecognitions != null) {
                        telemetry.addData("# Total Object Detected", updatedRecognitions.size());
                        //if (updatedRecognitions.size() == 3) {
                        //missingDetectionCounter = 0;
                        int goldMineralX = -1;
                        int goldMineralY = -1;
                        int silverMineral1X = -1;
                        int silverMineral1Y = -1;
                        int silverMineral2X = -1;
                        int silverMineral2Y = -1;

                        for (Recognition recognition : updatedRecognitions) {

                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                                goldMineralY = (int) recognition.getTop();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                                silverMineral1Y = (int) recognition.getTop();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                                silverMineral2Y = (int) recognition.getTop();
                            }
                        }
                        telemetry.log().add("Gold     - %d / %d", goldMineralX, goldMineralY);
                        telemetry.log().add("Silver 1 - %d / %d", silverMineral1X, silverMineral1Y);
                        telemetry.log().add("Silver 2 - %d / %d", silverMineral2X, silverMineral2Y);


                        if (goldMineralY != -1 && silverMineral1Y != -1 && silverMineral2Y != -1) {
                            if (goldMineralY < silverMineral1Y && goldMineralY < silverMineral2Y) {
                                telemetry.log().add("Gold Mineral Position - Left");
                                return GoldPosition.LEFT;
                            } else if (goldMineralY > silverMineral1Y && goldMineralY > silverMineral2Y) {
                                telemetry.log().add("Gold Mineral Position - Right");
                                return GoldPosition.RIGHT;
                            } else {
                                telemetry.log().add("Gold Mineral Position - Center");
                                return GoldPosition.CENTER;
                            }
                        }
//                        } else if(updatedRecognitions.size() == 2) {
//                            missingDetectionCounter++;
//                            telemetry.log().add("Missing Detection Count: %d", missingDetectionCounter);
//                            if(missingDetectionCounter > 10) {
//
//                                missingDetectionCounter = 0;
//                                int total = 0;
//                                //if the 2 are closer to left than right, turn clockwise
//                                for (Recognition recognition : updatedRecognitions) {
//                                    total += recognition.getTop();
//                                }
//                                float avg = (float)total / 2.0f;
//                                telemetry.log().add("Avg obj pos: %d", (int)avg);
//                                if(avg <= 640) {
//                                    //move clockwise a bit
//                                    telemetry.log().add("Turning Clockwise -->>>>");
//                                    //turnDegrees(Autonomous_Teaching.TurnDirection.CLOCKWISE, 15, 1);
//                                } else {
//                                    //else turn counterclockwise
//                                    telemetry.log().add("Turning CounterClockwise <<<<--");
//                                    //turnDegrees(Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, 15, 1);
//                                }
//                            }
                        //}
                    }
                    telemetry.update();
                }
            }
            if (tfod != null) {
                tfod.deactivate();
            }
        }
        return GoldPosition.UNKNOWN;
    }

    void holdStartingPosition(){
        robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.motorLift.setTargetPosition(0);
        robot.motorLift.setPower(1);
    }

    void waitForDelayStart(){
        double delay = config.getDelayStart();
        ElapsedTime time = new ElapsedTime();
        if(delay > 0 ) {

            while(opModeIsActive() && time.seconds() < delay) {
                idle();
            }
        }
    }

    void dropToGround() {
        robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.motorLift.setTargetPosition(config.getMaxLiftTicks());
        robot.motorLift.setPower(1);
        while(opModeIsActive() && robot.motorLift.isBusy()) {
            idle();
        }
    }






    double angle = 0;

    double silver_turn_to_image() {
        //start turning counterclockwise
        //drive.drive(-0.25, 0.25);


        ElapsedTime runtime = new ElapsedTime();
        boolean foundImage = false;

        double totalMove = 0;

        /** Start tracking the data sets we care about. */
        targetsRoverRuckus.activate();
        while(opModeIsActive() )
        {
            turnDegrees(TurnDirection.CLOCKWISE, 10);
            totalMove += 10;
            if(this.locateVuforiaTarget())
            {
                foundImage = true;

                emitStatus("Found Image and Is Moving");

                break;
            }
            if(totalMove > 50) {
                break;
            }
        }

        drive.stop();
        if(foundImage == false) {
            emitStatus("Could not find image");
            return 0;
        }

        VectorF translation = lastLocation.getTranslation();

        float opposite = Math.abs(translation.get(0) / mmPerInch); //x
        float adjacent = Math.abs(translation.get(1) / mmPerInch); //y
        // express the rotation of the robot in degrees.
        Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
        double currentHeading = Math.abs(rotation.thirdAngle);
        angle = Math.atan(opposite / adjacent);
        double desiredAngle = 90-angle;
        double movementAngle = currentHeading - desiredAngle;

        turnDegrees((movementAngle < 0) ? TurnDirection.CLOCKWISE : TurnDirection.COUNTERCLOCKWISE, movementAngle);

        double hypotenuse = Math.sqrt(opposite*opposite + adjacent*adjacent);

        targetsRoverRuckus.deactivate();
        return hypotenuse;

    }

    void Silver2(double speed){
        //driveStraight(speed, 24, 10);

        turnDegrees(TurnDirection.COUNTERCLOCKWISE, 90);
    }

    void PushOffGoldObject(double speed, GoldPosition gp) {
        robot.toggleLights();
        if(gp == gpCenter) {
            telemetry.log().add("Gold Position Is In The Center Position");
            driveStraight(speed, 20, 1);
            driveStraight(speed, -20, 1);
        } else if(gp == gpLeft) {
            telemetry.log().add("Gold Position Is In The Left Position");
            turnDegrees (TurnDirection.COUNTERCLOCKWISE, config.get_initialTurnDegreesCounterClockwise());
            driveStraight(speed, 20, 1);
            driveStraight(speed, -20, 1);
            turnDegrees(TurnDirection.CLOCKWISE, config.get_initialTurnDegreesCounterClockwise());
        } else if(gp == gpRight) {
            telemetry.log().add("Gold Position Is In The Right Position");
            turnDegrees(TurnDirection.CLOCKWISE, config.get_initialTurnDegreesClockwise());
            driveStraight(speed, 20, 1);
            driveStraight(speed, -20, 1);
            turnDegrees(TurnDirection.COUNTERCLOCKWISE, config.get_initialTurnDegreesClockwise());
        } else if(gp == gpUnknown) {
            telemetry.log().add("Could Not Find Gold Block Driving Straight! :(");
            driveStraight(speed, 20, 1);
            driveStraight(speed, -20, 1);
        }

        if(config.getPosition() == Config.Positions.GOLD) {
            Gold(speed);
        } else if(config.getPosition() == Config.Positions.SILVER) {
            Silver(speed);
        }
    }

    void Gold(double speed) {
        driveStraight(speed, 35, 1);
    }

    void Silver(double speed) {

    }

    void driveStraight(double speed, double inches, double timeoutSeconds) {
        encoderDrive(speed, inches, inches, timeoutSeconds, false);
    }

    void pause() {
        waitForTick(pauseTimeBetweenSteps);
    }

    public void waitForTick(long periodMs) {
        sleep(periodMs);
    }

    private void emitStatus(String status)
    {
        telemetry.log().add(status);
    }

    private void detectParticles() {
        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Left");
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                } else {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                }
                            }
                        }
                        telemetry.update();
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

}
