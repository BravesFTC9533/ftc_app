package org.firstinspires.ftc.teamcode.NewCode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class SwingAndLiftController {

    private final Robot robot;
    private final int MAX_LIFT_TICKS = 2000;

    public SwingAndLiftController(Robot robot) {
        this.robot = robot;
    }

    public void toggleLiftArm() {
        // Set lift arm to run to position
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(robot.lift.getCurrentPosition() > 0) {
            robot.lift.setTargetPosition(0);
        } else {
            robot.lift.setTargetPosition(MAX_LIFT_TICKS);
        }

        // Set lift motor to run using encoders
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
