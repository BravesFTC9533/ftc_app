package org.firstinspires.ftc.teamcode.NewCode;

public class Utils {

    private final Robot robot;

    private boolean boxLeftPositon = false;
    private boolean boxRightPosition = false;

    public Utils(Robot robot){
        this.robot = robot;
    }
    public void toggleBox(NewLinearOpMode.Box box) {
        if(box == NewLinearOpMode.Box.LEFT) {
            if(!boxLeftPositon) {
                robot.boxLeft.setPosition(1);
                boxLeftPositon = true;
            } else {
                robot.boxLeft.setPosition(0);
                boxLeftPositon = false;
            }
        } else {
            if(!boxRightPosition) {
                robot.boxRight.setPosition(1);
                boxRightPosition = true;
            } else {
                robot.boxRight.setPosition(0);
                boxRightPosition = false;
            }
        }
    }

}
