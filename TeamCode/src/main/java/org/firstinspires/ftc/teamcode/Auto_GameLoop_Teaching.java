package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.ListIntSet;
import org.firstinspires.ftc.teamcode.common.AutoHelper;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.VuforiaHelper;


@Autonomous(name="Doug: Autonomous", group="Competition Code")
//@Disabled
public class Auto_GameLoop_Teaching extends OpMode {

    private static final long pauseTimeBetweenSteps = 100;

    VuforiaHelper vuforiaHelper = new VuforiaHelper();
    AutoHelper autoHelper;

    Config.Positions startingPosition;
    Robot robot;
    double speed;

    Config config;

    enum SwingarmState {
        Ignore,
        Resetting,
        Reset,
        Done
    }
    enum LiftState{
        Ignore,
        Resetting,
        Reset,
        Done
    }

    enum ManeuverState {
        Start,
        Run,
        Waiting,
        Pausing,
        Next
    }



    enum GameState {
        Start,
        Dropping1, //first half of drop, need to free swing arm
        Dropping2, //second half of drop, need to lift swing arm and continue drop

        Landed,
        Detecting,
        DoneDetecting,
        PreparingForManeuvers,
        WaitingForInitialMove,
        DonePreparingForManeuvers,
        LeftMineralFound,
        CenterMineralFound,
        RightMineralFound,

        Done

    }


    LiftState liftState = LiftState.Ignore;
    SwingarmState swingarmState = SwingarmState.Ignore;
    GameState gameState = GameState.Start;

    ManeuverState maneuverState = ManeuverState.Start;

    VuforiaHelper.GoldPosition goldPosition;

    int maneuverCounter = 0;
    double maneuverWaitTime = 0;

    @Override
    public void init() {
        vuforiaHelper.initVuforia(hardwareMap);
        robot = new Robot(hardwareMap, true);
        autoHelper = new AutoHelper(robot);
        config = new Config(hardwareMap.appContext);


        robot.motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorSwing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void start() {
        //gameState =
        startingPosition = config.getPosition();
        speed = config.getSpeed();
    }

    @Override
    public void loop() {


        handleSwingarmState();
        handleLiftState();
        handleGameState();
    }


    private void handleSwingarmState() {
        // deal with resetting the swingarm
        switch (swingarmState){
            case Ignore:
                break;
            case Reset:
                robot.motorSwing.setTargetPosition(0);
                robot.motorSwing.setPower(config.getSwingArmPower());
                swingarmState = SwingarmState.Resetting;
            case Resetting:
                if(!robot.motorSwing.isBusy()) {
                    swingarmState = SwingarmState.Done;
                }
                break;
            case Done:
                liftState = LiftState.Reset;
                robot.motorSwing.setPower(0);
                swingarmState = SwingarmState.Ignore;
                break;
        }
    }
    private void handleLiftState() {
        //deal with resetting the lift
        //triggered by swingarm
        switch (liftState){
            case Ignore:
                break;
            case Reset:
                robot.motorLift.setTargetPosition(0);
                robot.motorLift.setPower(1);
                liftState = LiftState.Resetting;
            case Resetting:
                if(!robot.motorLift.isBusy()) {
                    liftState = LiftState.Done;
                }
                break;
            case Done:
                robot.motorLift.setPower(0);
                liftState = LiftState.Ignore;
                break;
        }
    }
    private void handleGameState() {
        switch (gameState){
            case Start:
                gameState = GameState.Dropping1;
                robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.motorLift.setTargetPosition(3500);
                robot.motorLift.setPower(1);
                break;
            case Dropping1:
                if(!robot.motorLift.isBusy()) { //hit target, move to next state
                    gameState = GameState.Dropping2;
                    robot.motorLift.setTargetPosition(config.getMaxLiftTicks());
                    robot.motorSwing.setTargetPosition(config.getMaxSwingTicks()/2);
                    robot.motorSwing.setPower(1);
                    robot.motorLift.setPower(1);
                }
                break;
            case Dropping2:
                if(!robot.motorLift.isBusy() && !robot.motorSwing.isBusy()) {
                    gameState = GameState.Landed;

                }
                break;
            case Landed:
                //start detecting minerals
                gameState = GameState.Detecting;
                robot.toggleLights();
                vuforiaHelper.ActivateTFOD();
                resetStartTime();
                break;
            case Detecting:
                if(getRuntime() >= 3) {
                    //we didnt find anything in 3 seconds, assume center
                    gameState = GameState.DoneDetecting;
                    goldPosition = VuforiaHelper.GoldPosition.CENTER;
                    break;
                }
                //look for gold mineral
                goldPosition = vuforiaHelper.DetectObjects();
                if(goldPosition != VuforiaHelper.GoldPosition.UNKNOWN) {
                    //we found it, move on
                    gameState = GameState.DoneDetecting;
                }
                break;

            case DoneDetecting:
                vuforiaHelper.DeactivateTFOD();
                robot.toggleLights();

                gameState = GameState.PreparingForManeuvers;
                break;

            case PreparingForManeuvers:
                autoHelper.PIDencoderDrive(config.getSpeed(), 6);
                resetStartTime();
                gameState = GameState.WaitingForInitialMove;
                break;
            case WaitingForInitialMove:
                if(!robot.isBusy() || getRuntime() > 2) {
                    robot.stop();
                    robot.setRunUsingEncoders();
                    gameState = GameState.DonePreparingForManeuvers;
                }
                break;

            case DonePreparingForManeuvers:
                //we've moved away from lander, we can reset the swingarm and lift now
                swingarmState = SwingarmState.Reset;
                maneuverCounter = 1;
                switch(goldPosition){
                    case CENTER:
                        gameState = GameState.CenterMineralFound;
                        break;
                    case LEFT:
                        gameState = GameState.LeftMineralFound;
                        break;
                    case RIGHT:
                        gameState = GameState.RightMineralFound;
                        break;
                }
                break;

            case LeftMineralFound:
            case CenterMineralFound:
            case RightMineralFound:
                handleManeuverState();
                break;
            case Done:
                requestOpModeStop();
                break;
        }
    }
    private void handleManeuverState() {

        switch(maneuverState) {
            case Start:
                maneuverCounter = 1;
                maneuverState = ManeuverState.Run;
                break;
            case Run:
                switch (gameState) {
                    case LeftMineralFound:
                        if (startingPosition == Config.Positions.GOLD) {
                            handleLeftMineral_Gold();
                        } else {
                            handleLeftMineral_Silver();
                        }

                        break;
                    case CenterMineralFound:
                        if (startingPosition == Config.Positions.GOLD) {
                            handleCenterMineral_Gold();
                        } else {
                            handleCenterMineral_Silver();
                        }
                        break;
                    case RightMineralFound:
                        if (startingPosition == Config.Positions.GOLD) {
                            handleRightMineral_Gold();
                        } else {
                            handleRightMineral_Silver();
                        }
                        break;
                }
                maneuverState = ManeuverState.Waiting;
                resetStartTime();
                break;
            case Waiting:
                if(getRuntime() >= maneuverWaitTime || !robot.isBusy()) {
                    maneuverState = ManeuverState.Pausing;
                    resetStartTime();
                }
                break;
            case Pausing:
                if(getRuntime() >= pauseTimeBetweenSteps) {
                    maneuverState= ManeuverState.Next;
                }
                break;
            case Next:
                maneuverCounter++;
                maneuverState= ManeuverState.Run;
                break;
        }
    }


    private void handleRightMineral_Gold() {
        switch (maneuverCounter){
            case 1:
                //                turn CW 38
                autoHelper.turnDegrees(AutoHelper.TurnDirection.CLOCKWISE, config.get_initialTurnDegreesClockwise());
                break;
            case 2:
                //        move fw 38"
                driveStraight(38);
                break;
            case 3:
                //        turn CW 38+90
                autoHelper.turnDegrees(AutoHelper.TurnDirection.CLOCKWISE, config.get_initialTurnDegreesClockwise()+90);
                break;
            case 4:
                //                move bw 30"
                driveStraight(-30);
                break;
            case 5:
                //                turn CCW 45
                autoHelper.turn45(AutoHelper.TurnDirection.COUNTERCLOCKWISE);
                break;
            case 6:
                //                boot
                robot.boot.setPosition(1);
                break;
            case 7:
                //                turn ccw 45
                autoHelper.turn45(AutoHelper.TurnDirection.COUNTERCLOCKWISE);
                 break;
            case 8:
                //                move bw 6*12"
                driveStraight(1,-6*12);
                break;
            case 9:
                gameState = GameState.Done;
                return;
        }

    }

    private void handleLeftMineral_Gold() {

        switch (maneuverCounter) {
            case 1:
                //        turn CCW 38
                autoHelper.turnDegrees(AutoHelper.TurnDirection.COUNTERCLOCKWISE, config.get_initialTurnDegreesCounterClockwise());
                break;
            case 2:
                //        move fw 49"
                driveStraight(49);
                break;
            case 3:
                //        turn CW 45+38
                autoHelper.turnDegrees(AutoHelper.TurnDirection.CLOCKWISE, config.get_initialTurnDegreesCounterClockwise() + 45);
                break;
            case 4:
                //        move fw 10.5
                driveStraight(10.5);
                break;
            case 5:
                //        boot
                robot.boot.setPosition(1);
                break;
            case 6:
                //        move bw 63"
                driveStraight(1, -63);
                break;
            case 7:
                gameState = GameState.Done;
                break;
        }

    }

    private void handleCenterMineral_Gold() {
        switch(maneuverCounter) {
            case 1:
                //        move fw 56"
                driveStraight(56);
                break;
            case 2:
                //        turn CW 90
                autoHelper.turn90(AutoHelper.TurnDirection.CLOCKWISE);
                break;
            case 3:
                //        move bw 9"
                driveStraight(-9);
                break;
            case 4:
                //        boot
                robot.boot.setPosition(1);
                break;
            case 5:
                //        move bw 9"
                driveStraight(-9);
                break;
            case 6:
                //        turn CCW 45
                autoHelper.turn45(AutoHelper.TurnDirection.COUNTERCLOCKWISE);
                break;
            case 7:
                //        move bw 60"
                driveStraight(1,60);
                break;
            case 8:
                gameState = GameState.Done;
                return;

        }









    }


    private void handleRightMineral_Silver() {

        //turn 38 degrees CW
        //drive into mineral
        //reverse some amount
        //turn 38+90 degrees CCW
        //drive to wall
        //turn 45 degrees CCW
        //drive to depot
        //boot
        //drive 6.5*12 back to crater
    }
    private void handleLeftMineral_Silver() {

    }
    private void handleCenterMineral_Silver() {

    }


    private void driveStraight(double speed, double inches) {
        autoHelper.PIDencoderDrive(speed, inches);
    }
    private void driveStraight(double inches)  {
        driveStraight(config.getSpeed(), inches);
    }
}
