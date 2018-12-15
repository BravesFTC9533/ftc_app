package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class SwingAndLiftController {

    private Robot robot;

    public void dumpAuto() {
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.swing.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Lift the lift to get the swing arm out of the way
        robot.lift.setTargetPosition(-6905);
        robot.swing.setTargetPosition(824);
    }

    public void resetArmPosition() {
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.swing.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(robot.lift.isBusy()) {}

        if(robot.lift.getCurrentPosition() < 3000) {
            robot.lift.setTargetPosition(3000);
        }

        robot.swing.setTargetPosition(0);
        robot.lift.setTargetPosition(0);
    }
}
