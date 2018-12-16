package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.GTADrive;
import org.firstinspires.ftc.teamcode.common.Quad;
import org.firstinspires.ftc.teamcode.common.SimpleMenu;

/**
 * Created by dmill on 2/3/2018.
 */
@Autonomous(name = "PID Auto Test", group = "Testing")
//@Disabled
public class PIDAutoTest extends Teaching_BaseLinearOpMode {




    public static double NEW_P = 10.0;
    public static double NEW_I = 10.0;
    public static double NEW_D = 1.0;
    public static double NEW_KP = 0.1;
    public static double Kp = 0.1;
    public static double Distance = 48;

    public static SimpleMenu menu = new SimpleMenu("Autonomous Menu");

    Config config;

    @Override
    public void runOpMode() throws InterruptedException {

        Initialize(hardwareMap, true);

        setDrive(new GTADrive(robot, driverGamePad));

        config = new Config(hardwareMap.appContext);

        NEW_P = config.getKP();
        NEW_I = config.getKI();
        NEW_D = config.getKD();

        updatePID();
        robot.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        menu.clearOptions();
//        menu.addOption("P", 1000, 0, 0.01, NEW_P);
//        menu.addOption("I", 1000, 0, 0.01, NEW_I);
//        menu.addOption("D", 1000, 0, 0.01, NEW_D);
//        menu.addOption("Dist", 12*12, 12, 0.1, Distance);
//        menu.addOption("Kp", 1000, 0, 0.01, NEW_KP);
//        menu.setGamepad(gamepad1);
//        menu.setTelemetry(telemetry);




        waitForStart();

        boolean startPressed = false;


        while(opModeIsActive()) {

            if (gamepad1.start && startPressed == false) {
                startPressed = true;

            }
            if(!gamepad1.start && startPressed) {
                startPressed = false;
                this.Kp = NEW_KP;
                //updatePID();

                //drive_straight(0.75, 48);

                turn90(Autonomous_Teaching.TurnDirection.CLOCKWISE);

            }

//            menu.displayMenu();
//            NEW_P = Double.parseDouble(menu.getCurrentChoiceOf("P"));
//            NEW_I = Double.parseDouble(menu.getCurrentChoiceOf("I"));
//            NEW_D = Double.parseDouble(menu.getCurrentChoiceOf("D"));
//            NEW_KP = Double.parseDouble(menu.getCurrentChoiceOf("Kp"));
//            Distance = Double.parseDouble(menu.getCurrentChoiceOf("Dist"));
            telemetry.addData("POS", "%d", robot.motorBackLeft.getCurrentPosition());
            telemetry.addData("POS", "%d", robot.motorBackRight.getCurrentPosition());
            telemetry.update();
        }


//        config.setKP(NEW_P);
//        config.setKI(NEW_I);
//        config.setKD(NEW_D);
//        config.save();

    }


    public void updatePID() {

        robot.updatePID(NEW_P, NEW_I, NEW_D);

    }

    private void drive_straight(double targetSpeed, double inches){
        Quad<Integer, Integer, Integer, Integer>  positions = robot.setNewPositionFourWheel(inches);


        robot.setRunToPosition();
        robot.setPower(targetSpeed , targetSpeed);

        ElapsedTime timer = new ElapsedTime();
        while(opModeIsActive() && robot.isBusy() && timer.seconds() < 10.0) {
            idle();
        }

        robot.stop();
        robot.setRunUsingEncoders();

    }



//    public void encoderDrive_straight(double targetSpeed, double inches, double timeoutS) {
//        boolean atMaxSpeed = false;
//        ElapsedTime runtime = new ElapsedTime();
//
//        double currentSpeed = 0;
//        double minSpeed = 0.25;
//
//        int lastError = 0;
//        int error = 0;
//
//        if(opModeIsActive()) {
//
//            Pair<Integer, Integer> target = robot.setNewPosition(inches); // set robots new target, and set mode to run to position
//            Pair<Integer, Integer> start = robot.getCurrentPosition();    // get robots starting position
//
//            boolean shouldLoopContinue = opModeIsActive();
//
//            while(shouldLoopContinue){
//
//                Pair<Integer, Integer> current = robot.getCurrentPosition(); //get robots current position
//
//                if(!atMaxSpeed) {
//                    //accelerate until we reach our target speed
//                    currentSpeed = accelerate(0.25, runtime.seconds(), targetSpeed);
//                    if(currentSpeed >= targetSpeed) {
//                        currentSpeed = targetSpeed;
//                        atMaxSpeed = true;
//                    }
//                } else {
//                    //use deceleration curve CubicEaseIn
//                    //only using left wheel position
//                    currentSpeed = decelerate(start.getLeft(), target.getLeft(), current.getLeft(), currentSpeed);
//                    if(currentSpeed <= minSpeed){
//                        currentSpeed = minSpeed;
//                    }
//                }
//
//                double leftSpeed = currentSpeed;
//                double rightSpeed = currentSpeed;
//
//                if(Kp > 0) {
//                    //calculate error (has one wheel moved further than another)
//                    int rightPos = (target.getRight() - current.getRight());
//                    int leftPos = (target.getLeft() - current.getLeft());
//                    error = rightPos - leftPos;
//
//                    //set left and right powers based on error (P loop)
//                    leftSpeed = Range.clip(currentSpeed + (Kp * error), targetSpeed * -1, targetSpeed);
//                    rightSpeed = Range.clip(currentSpeed - (Kp * error), targetSpeed - 1, targetSpeed);
//
//                }
//
//                robot.setPower(leftSpeed, rightSpeed);
//
//
//                shouldLoopContinue =
//                        runtime.seconds() < timeoutS &&
//                                robot.isBusy() &&
//                                opModeIsActive();
//            }
//
//            robot.stop();
//            robot.setRunUsingEncoders();
//        }
//
//    }

//    public double accelerate(double accelerationSeconds, double currentSeconds, double targetSpeed){
//        double percent = currentSeconds * (1/accelerationSeconds);
//        return accelerate(percent, targetSpeed);
//    }
//
//    public double accelerate(double percent, double targetSpeed) {
//        double multiplier = Easing.Interpolate(percent, Easing.Functions.CubicEaseOut);
//        return (targetSpeed * multiplier);
//    }
//
//    public double decelerate(int startPosition, int targetPosition, int currentPosition, double currentSpeed) {
//
//        double totalDistance = (double)(Math.abs(targetPosition) - Math.abs(startPosition));
//        double distanceLeftToTravel = (double)(Math.abs(targetPosition) - Math.abs(currentPosition));
//
//        double percent = 1 - (distanceLeftToTravel / totalDistance);
//        return decelerate(percent, currentSpeed);
//
//    }
//
//    public double decelerate(double percent, double currentSpeed) {
//        double multiplier = 1 - (Easing.Interpolate(percent, Easing.Functions.CubicEaseIn));
//        return (currentSpeed * multiplier);
//    }
//
//    public void encoderDrive(double speed,
//                             double leftInches, double rightInches,
//                             double timeoutS, boolean holdPosition) {
//
//
//        double maxSpeed = 0.95;
//        if(speed > maxSpeed) {
//            speed = maxSpeed;
//        }
//        int targetLeft, targetRight, currentRight, currentLeft;
//        int differenceLeft, differenceRight;
//
//
//        boolean accelerate = (Math.abs(leftInches) > 5) || (Math.abs(rightInches) > 5);
//        boolean maxed = false;
//        ElapsedTime runtime = new ElapsedTime();
//
//        robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        // Ensure that the opmode is still active
//        if (opModeIsActive()) {
//
//            robot.setNewPosition(leftInches, rightInches);
//
//            targetLeft = robot.motorLeft.getTargetPosition();
//            targetRight =robot.motorRight.getTargetPosition();
//
//            currentLeft = robot.motorLeft.getCurrentPosition();
//
//            int scale = Math.abs(Math.abs(targetLeft) - Math.abs(currentLeft));
//
//            //int newLeftTarget = currentLeft + (int)(leftInches * robot.COUNTS_PER_INCH);
//
//            int distanceToMoveTicks = targetLeft - currentLeft;
//
//
//            // reset the timeout time and start motion.
//            runtime.reset();
//
//            double currentSpeed = 0;
//            double multiplier = 0;
//
//
//            robot.setPower(currentSpeed , currentSpeed);
//
//
//            int lastPosition = 0;
//            double lastRuntime = 0;
//            double easein = 0;
//
//            int slowdownTick = 150;
//            // keep looping while we are still active, and there is time left, and both motors are running.
//            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
//            // its target position, the motion will stop.  This is "safer" in the event that the robot will
//            // always end the motion as soon as possible.
//            // However, if you require that BOTH motors have finished their moves before the robot continues
//            // onto the next step, use (isBusy() || isBusy()) in the loop test.
//            while (opModeIsActive() &&
//                    (runtime.seconds() < timeoutS) &&
//                    (robot.isBusy())) {
//
//                currentLeft = robot.motorLeft.getCurrentPosition();
//                currentRight = robot.motorRight.getCurrentPosition();
//                differenceLeft = Math.abs(Math.abs(targetLeft) - Math.abs(currentLeft));
//
//                if(accelerate) {
//                    if (maxed) {
//
//                        easein = (Easing.Interpolate(1 - ((double)differenceLeft / (double)scale), Easing.Functions.CubicEaseIn));
//                        multiplier = 1 - easein;
//
//                        currentSpeed = speed * multiplier;
//                        if (currentSpeed < 0.4) {
//                            currentSpeed = 0.4;
//                        }
//                    } else if (currentSpeed < speed) {
//                        multiplier = Easing.Interpolate(runtime.seconds() * 4, Easing.Functions.CubicEaseOut);
//                        currentSpeed = speed * multiplier;
//                    }
//
//                } else {
//                    currentSpeed = speed;
//                }
//
//                telemetry.addLine()
//                        .addData("Scale", "%4d", scale)
//                        .addData("CL", "%4d", Math.abs(currentLeft))
//                        .addData("DL", "%4d", differenceLeft);
//
//                telemetry.addLine()
//                        .addData("Multiplier", "%7f", multiplier)
//                        .addData("Speed", "%7f", currentSpeed)
//                        .addData("Ease", "%7f", easein);
//
//                //telemetry.update();
//
//                if(currentSpeed >= speed) {
//                    currentSpeed = speed;
//                    maxed = true;
//                }
//                robot.setPower(currentSpeed, currentSpeed);
//
//
//                // Display it for the driver.
//                telemetry.addLine().addData("Target",  "Running to %7d :%7d",
//                        targetLeft,
//                        targetRight);
//                telemetry.addLine().addData("Current",  "Running at %7d :%7d",
//                        currentLeft,
//                        currentRight);
//                telemetry.update();
//            }
//
//            if(holdPosition==false) {
//                // Stop all motion;
//                robot.stop();
//
//                // Turn off RUN_TO_POSITION
//                robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            }
//        }
//    }





}
