package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.GTADrive;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

@Autonomous(name="Main: Autonomous", group="Main")

public class Autonomous_Main extends Teaching_BaseLinearOpMode {

    private int goldBlockPosition = 0;

    enum TurnDirection {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }

    enum GoldPosition {
        LEFT,
        RIGHT,
        CENTER,
        UNKNOWN
    }

    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private String currentStatus; //The current status of the robot, what position it should be doing.
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final long pauseTimeBetweenSteps = 250; //The pause between every steps is 1 second


    @Override
    public void runOpMode() throws InterruptedException {

        // We can control the number of lines shown in the log
        telemetry.log().setCapacity(10);


        telemetry.log().add("Setting up hardware");
        telemetry.update();
        Initialize(hardwareMap, false); //Four wheel drive is true meaning that it will run a four wheel drive
        setDrive(new GTADrive(robot, driverGamePad)); //That sets the robot into GTA mode

        //ComposeTelemetryPreStart();

        telemetry.log().add("Setting up vuforia");
        telemetry.update();
        initializeVuforia();
        telemetry.log().add("Setting up tfod");
        telemetry.update();
        initTfod();

        double speed = 0.65;

        telemetry.log().add("Completed Initialization. Lets win the Championship!!");
        telemetry.update();

        waitForStart();

        Silver(speed);


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
            turnDegrees(Autonomous_Teaching.TurnDirection.CLOCKWISE, 10);
            //It means that when it has landed the robot will start turning clockwise 10 degrees

            totalMove += 10;
            if(this.locateVuforiaTarget())
            {
                foundImage = true;

                currentStatus = "Found Image and Is Moving";

                break;
            }
            if(totalMove > 50) {
                break; //This means that if the total movement of the robot is more than 50, then break the program
            }
        }


        drive.stop();
        if(foundImage == false) {
            currentStatus = "Could not find image";
            return 0;
        }

        VectorF translation = lastLocation.getTranslation();

        float opposite = Math.abs(translation.get(0) / mmPerInch); //x

        float hypotenuse = Math.abs(translation.get (2) / mmPerInch);
        // express the rotation of the robot in degrees.
        Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
        double currentHeading = Math.abs(rotation.thirdAngle);

        double desiredAngle = 50;
        double movementAngle = desiredAngle;

        turnDegrees((movementAngle < 0) ? Autonomous_Teaching.TurnDirection.CLOCKWISE : Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, movementAngle);

        double adjacent = Math.sqrt(hypotenuse*hypotenuse - opposite*opposite);


        targetsRoverRuckus.deactivate();
        return adjacent;

    }



    // Main Programming Chunk

    void Silver(double speed) {
        //TODO Start off with Tensor Flow and check to see where gold object is
        telemetry.log().add("Turning on lights");
        pause();
        robot.toggleLights();
        pause();
        //telemetry.log().add("Lights on (%0.2f)", robot.lights.getPower());
        GoldPosition gp = DetectObjects(speed);
        pause();
        telemetry.log().add("Turning off lights");
        robot.toggleLights();
        pause();

        //TODO Move towards the object and push it off the red square
        goldPosition(gp, speed);


        while(opModeIsActive()){
            idle();
        }

        //TODO Drive into the depot making sure to be compatible with what side of the field we are on
    }

    //Call Later

    void goldPosition(GoldPosition gp, double speed) {
        driveStraight(speed, 4, 2);
        if(gp == GoldPosition.LEFT) {
            turnDegrees(Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, robot.config.get_initialTurnDegreesCounterClockwise());
            pause();
            driveStraight(speed, 28, 4);
            pause();
            turnDegrees(Autonomous_Teaching.TurnDirection.CLOCKWISE, 45);
            pause();
            driveStraight(speed, 10, 2);
        } else if (gp == GoldPosition.RIGHT) {
            turnDegrees(Autonomous_Teaching.TurnDirection.CLOCKWISE, robot.config.get_initialTurnDegreesClockwise());
            pause();
            driveStraight(speed, 28, 4);
            pause();
            turnDegrees(Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, 45);
            pause();
            driveStraight(speed, 10, 2);
        } else if (gp == GoldPosition.CENTER) {
            turnDegrees(Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, 21);
            pause();
            driveStraight(speed, 25, 4);
            pause();
        }
    }

    private GoldPosition DetectObjects (double speed) {
        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            int missingDetectionCounter = 0;

            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    //List<Recognition> ourCollection = new ArrayList<>();

                    if (updatedRecognitions != null) {
                        telemetry.addData("# Total Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
                            missingDetectionCounter = 0;
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
                                if (goldMineralY < silverMineral1Y && goldMineralX < silverMineral2Y) {
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
                        } else if(updatedRecognitions.size() == 2) {
                            missingDetectionCounter++;
                            telemetry.log().add("Missing Detection Count: %d", missingDetectionCounter);
                            if(missingDetectionCounter > 10) {

                                missingDetectionCounter = 0;
                                int total = 0;
                                //if the 2 are closer to left than right, turn clockwise
                                for (Recognition recognition : updatedRecognitions) {
                                    total += recognition.getTop();
                                }
                                float avg = (float)total / 2.0f;
                                telemetry.log().add("Avg obj pos: %d", (int)avg);
                                if(avg <= 640) {
                                    //move clockwise a bit
                                    telemetry.log().add("Turning Clockwise -->>>>");
                                    turnDegrees(Autonomous_Teaching.TurnDirection.CLOCKWISE, 15, 1);
                                } else {
                                    //else turn counterclockwise
                                    telemetry.log().add("Turning CounterClockwise <<<<--");
                                    turnDegrees(Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, 15, 1);
                                }
                            }
                        }
                    }
                    telemetry.update();
                }
            }
            if(tfod != null) {
                tfod.deactivate();
            }
        }
        return GoldPosition.UNKNOWN;
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


    private void ComposeTelemetryPreStart() {

        telemetry.clearAll();


        telemetry.addLine()
                .addData("Status", currentStatus);
        telemetry.addLine()
                .addData("Angle", angle);

//        telemetry.addLine()
//                .addData("VuMark", new Func<String>() {
//                    @Override
//                    public String value() {
//                        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
//                            return String.format("%s visible", vuMark);
//                        } else {
//                            return "not visible";
//                        }
//                    }
//                });


    }
}
