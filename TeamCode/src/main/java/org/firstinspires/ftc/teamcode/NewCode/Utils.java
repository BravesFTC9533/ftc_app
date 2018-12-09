package org.firstinspires.ftc.teamcode.NewCode;

public class Utils {

    private MainOpMode opmode;

    enum Box {
        LEFT, RIGHT
    }

    //TODO Might have to switch the 0.5 and 0 out later
    public void toggleBox(Box box) {
        if(box == Box.LEFT) {
            if(opmode.boxLeft.getPosition() > 0) {
                opmode.boxLeft.setPosition(0.5);
            } else {
                opmode.boxLeft.setPosition(0);
            }
        } else if(box == Box.RIGHT) {
            if(opmode.boxRight.getPosition() > 0) {
                opmode.boxRight.setPosition(0.5);
            } else {
                opmode.boxRight.setPosition(0);
            }
        }
    }

}
