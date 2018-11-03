package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.common.GTADrive;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;


@Autonomous(name="Test: Autonomous", group="Test")

public class Autonomous_Test extends Teaching_BaseLinearOpMode {
    enum TurnDirection {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }


    private String currentStatus;
    private static final long pauseTimeBetweenSteps = 1000;

    Servo trophyArm;

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
            turnDegrees(Autonomous_Teaching.TurnDirection.CLOCKWISE, 10);
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

        turnDegrees((movementAngle < 0) ? Autonomous_Teaching.TurnDirection.CLOCKWISE : Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, movementAngle);

        double hypotenuse = Math.sqrt(opposite*opposite + adjacent*adjacent);

        targetsRoverRuckus.deactivate();
        return hypotenuse;

    }


    void Silver2(double speed){
        //driveStraight(speed, 24, 10);

        turnDegrees(Autonomous_Teaching.TurnDirection.COUNTERCLOCKWISE, 90);
    }

    // Main Programming Chunk

    void Silver(double speed) {
        trophyArm.setPosition(1.0);

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
