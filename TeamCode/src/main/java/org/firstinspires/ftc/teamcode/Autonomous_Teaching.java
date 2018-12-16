package org.firstinspires.ftc.teamcode;

import android.sax.TextElementListener;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.GTADrive;
import org.firstinspires.ftc.teamcode.common.Quad;

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
//
//    private GoldPosition gpCenter = GoldPosition.CENTER;
//    private GoldPosition gpRight = GoldPosition.RIGHT;
//    private GoldPosition gpLeft = GoldPosition.LEFT;
//    private GoldPosition gpUnknown = GoldPosition.UNKNOWN;

    private static final long pauseTimeBetweenSteps = 100;

    int max = 3;
    int min = 0;

    public static double NEW_P = 10;
    public static double NEW_I = 3.0;
    public static double NEW_D = 0.0;


    private boolean doDrop = true;


    @Override
    public void runOpMode() throws InterruptedException {

        // We can control the number of lines shown in the log
        telemetry.log().setCapacity(10);


        telemetry.log().add("Setting up hardware");
        telemetry.update();
        Initialize(hardwareMap, true); //Four wheel drive is true meaning that it will run a four wheel drive
        setDrive(new GTADrive(robot, driverGamePad)); //That sets the robot into GTA mode

        config = new Config(hardwareMap.appContext);

        robot.updatePID(config.getKP(), config.getKI(), config.getKD());


        Config.Colors color = config.getColor();
        Config.Positions position = config.getPosition();


        robot.motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorSwing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        holdStartingPosition();

        telemetry.log().add("Setting up vuforia");
        telemetry.update();
        initializeVuforia();
        telemetry.log().add("Setting up tfod");
        telemetry.update();
        initTfod();

        double speed = config.getSpeed();

        telemetry.log().add("Completed Initialization. Good Luck!!!");
        telemetry.update();


        waitForStart();


        waitForDelayStart();


        //Drop Down The Robot From The Lander
        if(doDrop) {
            dropToGround();
            pause();
        }

        //try to detect the gold mineral
        state = detectGoldMineral();
        pause();

        robot.motorSwing.setTargetPosition(0);

        //move off lander
        driveStraight(speed, 6, 2);
        robot.motorIntake.setPower(-1);
        pause();
        pause();
        pause();
        pause();


        //TODO Add back in after gold and silver classes are working
        if(config.getPosition() == Config.Positions.GOLD) {
            Gold(speed);
        } else if(config.getPosition() == Config.Positions.SILVER) {
            Silver(speed);
        }

        resetLiftAndSwing();
        pause();

        robot.turnOffAllMotors();
    }

    private void pushOffGoldMineral() {
        //Turn On The Intake Motor In Reverse To Push Away The Gold Object
        robot.motorIntake.setPower(-1);
        //Drive Into The Gold Object and Back
        PushOffGoldObject(config.getSpeed(), state);
        //Turn Off The Intake Motor
        robot.motorIntake.setPower(0);
    }

    private GoldPosition detectGoldMineral() {

        //Turn The Lights On
        robot.toggleLights();
        //Find The Gold Object
        GoldPosition state = DetectObjectsNonStop();
        //Turn Off The Lights
        robot.toggleLights();
        return  state;
    }



    private GoldPosition DetectObjectsNonStop() {
        loop = false;


        ElapsedTime timer = new ElapsedTime();
        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            int missingDetectionCounter = 0;

            while (opModeIsActive() && timer.seconds() < 3) {
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

    void resetLiftAndSwing() {
        if(opModeIsActive()) {

            robot.motorSwing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorSwing.setTargetPosition(0);
            robot.motorSwing.setPower(1);

            robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorLift.setTargetPosition(0);
            robot.motorLift.setPower(1);

            while (opModeIsActive() && (robot.motorLift.isBusy() || robot.motorSwing.isBusy())) {
                idle();
            }
        }
    }
    void dropToGround() {

        if(opModeIsActive()) {
            robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorSwing.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.motorLift.setTargetPosition(3500);
            robot.motorLift.setPower(1);

            while(opModeIsActive() && robot.motorLift.isBusy()) {
                idle();
            }

            robot.motorLift.setTargetPosition(config.getMaxLiftTicks());
            robot.motorLift.setPower(1);

            robot.motorSwing.setTargetPosition(config.getMaxSwingTicks() / 2);
            robot.motorSwing.setPower(config.getSwingArmPower());

            while(opModeIsActive() && (robot.motorLift.isBusy() || robot.motorSwing.isBusy())) {
                idle();
            }

        }
    }

    void PushOffGoldObject(double speed, GoldPosition gp) {



        switch (gp){
            case LEFT:
                telemetry.log().add("Gold Position Is In The Left Position");
                turnDegrees (TurnDirection.COUNTERCLOCKWISE, config.get_initialTurnDegreesCounterClockwise());
                break;
            case RIGHT:
                telemetry.log().add("Gold Position Is In The Right Position");
                turnDegrees(TurnDirection.CLOCKWISE, config.get_initialTurnDegreesClockwise());
                break;
            case CENTER:
                telemetry.log().add("Gold Position Is In The Center Position");
                break;
            case UNKNOWN:
                telemetry.log().add("Could Not Find Gold Block Driving Straight! :(");
                break;
        }

        driveStraight(speed, 24, 3);
        pause();





//        driveStraight(speed, -24, 3);
//
//        //turn back to center
//        switch (gp){
//            case LEFT:
//                turnDegrees (TurnDirection.CLOCKWISE, config.get_initialTurnDegreesCounterClockwise());
//                break;
//            case RIGHT:
//                turnDegrees(TurnDirection.COUNTERCLOCKWISE, config.get_initialTurnDegreesClockwise());
//                break;
//
//        }

    }


    void Gold(double speed){
        switch (state){
            case RIGHT:
                Gold_Right(speed);
                break;
            case CENTER:
                Gold_Center(speed);
                break;
            case LEFT:
                Gold_Left(speed);
                break;
        }
    }
    void Gold_Right(double speed){

        double turndegrees = config.get_initialTurnDegreesClockwise();
//        move out 6"
//        turn CW 38
        turnDegrees(TurnDirection.CLOCKWISE, turndegrees);
        pause();
//        move fw 38"
        driveStraight(speed, 36, 3);
        pause();
//        turn CW 38+90
        turnDegrees(TurnDirection.CLOCKWISE, (90 - (45 + turndegrees))+90);
        pause();
//        move bw 30"
        driveStraight(speed, -32, 3);
        pause();
        pause();
        pause();
        pause();
//        turn CCW 45
        turn45(TurnDirection.COUNTERCLOCKWISE);
        pause();
//        boot
        robot.boot.setPosition(1);
        pause();
//        turn ccw 45
        turn45(TurnDirection.COUNTERCLOCKWISE);
        pause();
//        move bw 6*12"
        driveStraight(1, -6.7*12, 10);

    }
    void Gold_Center(double speed){
//        move fw 56"
        driveStraight(speed, 56, 4);
        pause();
//        turn CW 90
        turn90(TurnDirection.CLOCKWISE);
        pause();
//        move bw 9"
        driveStraight(speed, -9, 2);
        pause();
//        boot
        robot.boot.setPosition(1);
        pause();
//        move bw 9"
        driveStraight(speed, -9, 2);
        pause();
//        turn CCW 45
        turn45(TurnDirection.COUNTERCLOCKWISE);
        pause();
//        move bw 60"
        driveStraight(speed, 60, 2);
        pause();

    }
    void Gold_Left(double speed) {
//
//        turn CCW 38
        turnDegrees(TurnDirection.COUNTERCLOCKWISE, config.get_initialTurnDegreesCounterClockwise());
        pause();
//        move fw 49"
        driveStraight(speed, 49, 4);
        pause();
//        turn CW 45+38
        turnDegrees(TurnDirection.CLOCKWISE, config.get_initialTurnDegreesCounterClockwise() + 45);
        pause();
//        move fw 10.5
        driveStraight(speed, 10.5, 4);
        pause();
//        boot
//        move bw 63"
        driveStraight(speed, -63, 4);
        pause();
    }



    //12in, 45 turn, 42in
    void Silver(double speed) {
        //TODO Program Silver Case
    }



    void Gold_Old(double speed) {


        switch(state) {
            case RIGHT:
                //back up a bit
                driveStraight(speed, -6, 2);
                pause();
                pause();
                pause();

                //turn for large move to crater before moving to depot
                double turn = (45 - config.get_initialTurnDegreesClockwise()) + 52;
                turnDegrees(TurnDirection.CLOCKWISE, turn, 0.75, 5);
                pause();
                driveStraight(speed, -50, 6);
                pause();


                //turn back to depot and move towards it
                turnDegrees(TurnDirection.COUNTERCLOCKWISE, 63, 0.75, 4);
                pause();
                driveStraight(speed, 25, 4);
                pause();
                turnDegrees(TurnDirection.CLOCKWISE, 10, 0.75, 4);
                pause();
                driveStraight(speed, 25, 4);
                pause();

                turnDegrees(TurnDirection.CLOCKWISE, 45);
                pause();
                robot.boot.setPosition(1);
                pause();

                turnDegrees(TurnDirection.COUNTERCLOCKWISE, 45);

                pause();
                driveStraight(speed, -60, 10);
                break;
            default:
                return;

        }


//
//        //drive close to minerals 1*12
//        driveStraight(speed, 12, 2);
//        pause();
//        //turn CW 90 degrees
//        turn90(TurnDirection.CLOCKWISE);
//        pause();
//        //drive backwards towards wall 2.75*12
//        driveStraight(speed, -45, 4);
//        pause();


        //turn CCW 52 degrees
//        turnDegrees(TurnDirection.COUNTERCLOCKWISE, 65);
//        pause();
//        //drive fw 4.5*12
//        driveStraight(speed, 54, 4);
//        pause();
//
//        //turn 45 degrees and boot off trophy
//        turnDegrees(TurnDirection.CLOCKWISE, 25);
//        driveStraight(speed, 6, 1);
//        turnDegrees(TurnDirection.CLOCKWISE, 20);
//        robot.boot.setPosition(1);
//        pause();
//
//        turnDegrees(TurnDirection.COUNTERCLOCKWISE, 45);
//        pause();
//
//        //drive bw 6*12
//        driveStraight(speed, -72, 5);
//        pause();

    }




    private void driveStraight(double targetSpeed, double inches, double timeoutSeconds){

        if(opModeIsActive()) {
            Quad<Integer, Integer, Integer, Integer> positions = robot.setNewPositionFourWheel(inches);


            robot.setRunToPosition();
            robot.setPower(targetSpeed, targetSpeed);

            ElapsedTime timer = new ElapsedTime();
            while (opModeIsActive() && robot.isBusy(false) && timer.seconds() < timeoutSeconds) {
                idle();
            }

            robot.stop();
            robot.setRunUsingEncoders();
        }

    }

    void pause() {
        waitForTick(pauseTimeBetweenSteps);
    }

    public void waitForTick(long periodMs) {
        sleep(periodMs);
    }


}
