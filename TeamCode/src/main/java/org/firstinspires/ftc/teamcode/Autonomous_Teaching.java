package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.common.GTADrive;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

//@Autonomous(name="Teaching: Autonomous", group="Tutorials")
//@Disabled
public class Autonomous_Teaching extends Teaching_BaseLinearOpMode {

    enum TurnDirection {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }


    private String currentStatus;
    private static final long pauseTimeBetweenSteps = 1000;


    @Override
    public void runOpMode() throws InterruptedException {
        Initialize(hardwareMap, true);
        setDrive(new GTADrive(robot, driverGamePad));

        ComposeTelemetryPreStart();

        initializeVuforia();

        double speed = 0.85;

//        Context context = hardwareMap.appContext;
//
//        Config config = new Config(context);

        waitForStart();

        Silver(speed);

    }

    void Blue(double speed) {

    }
    void Red(double speed) {

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

                currentStatus = "Found Image and Is Moving";

                break;
            }
            if(totalMove > 50) {
                break;
            }
        }

        drive.stop();
        if(foundImage == false) {
            currentStatus = "Could not find image";
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

    // Main Programming Chunk

    void Silver(double speed) {

        currentStatus = "Move out from lander";
        //move out from lander 2 inches
        driveStraight(speed, 12, 3);
        pause();


        currentStatus = "Turn some";
        turnDegrees(TurnDirection.COUNTERCLOCKWISE, 90);
        pause();

        currentStatus="Get closer to image";
        driveStraight(speed, 19, 3);
        pause();

        turnDegrees(TurnDirection.CLOCKWISE, 20);
        pause();

        currentStatus = "Turn to face image";
        //detect image and turn to face image
        double hypotenuse = silver_turn_to_image();
        pause();

        if(hypotenuse == 0)
        {
            currentStatus = "Could not find image";
            return;
        }

        turnDegrees(TurnDirection.COUNTERCLOCKWISE, hypotenuse - 10);
//        double distaneToDriveToImage = hypotenuse - 40;
//        driveStraight(speed, distaneToDriveToImage, 5);

        //drive to image staying off wall about 10 inches
//        double distanceToDriveToImage = hypotenuse - 20;
//        currentStatus = "Drive to image wall";
//        driveStraight(speed, distanceToDriveToImage, 5);
//
//
//        currentStatus = "Turn towards depot";
//        //turn towards depot
//        double new_angle = 90 - angle;
//        turnDegrees(TurnDirection.COUNTERCLOCKWISE, new_angle);
//
//
//        currentStatus = "Move to depot";
//        //calculate new movement and move to depot
//        double new_x = 20*Math.sin(angle);
//        double distance_to_depot_wall = (6*12) + new_x;
//        double move_to_depot_wall = distance_to_depot_wall - 15;
//        driveStraight(speed, move_to_depot_wall, 5);
//


//
//        driveStraight(speed, 14, 5);
//        pause();
//
//        //turn left
//        turn90(TurnDirection.COUNTERCLOCKWISE, speed);
//        pause();
//
//        driveStraight(speed, 44, 5);
//        pause();
//
//        turn45(TurnDirection.COUNTERCLOCKWISE, speed);
//        pause();
//
//        driveStraight(speed, 57, 5);
//        pause();
//
//        driveStraight(1, -86, 20);
//        pause();


    }

    //Call Later

    void Gold(double speed) {

//        //drive out from lander
//        driveStraight(speed, 14, 5);
//        pause();
//
//        //turn left
//        turn90(TurnDirection.COUNTERCLOCKWISE, speed);
//        pause();
//
//        //drive forward 24 inches
//        driveStraight(speed, 24, 5);
//        pause();
//
//        //turn to face wall
//        turn45(TurnDirection.CLOCKWISE, speed);
//        pause();
//
//        //drive to wall, stopping at least 10 inches short
//        driveStraight(speed, 15, 5);
//        pause();
//
//        turn90(TurnDirection.CLOCKWISE, speed);
//        pause();
//
//        driveStraight(1, 48, 20);
//        pause();
//
//        driveStraight(1, -76, 20);
//        pause();

//        turn90(TurnDirection.CLOCKWISE, speed);
//        pause();
//

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
