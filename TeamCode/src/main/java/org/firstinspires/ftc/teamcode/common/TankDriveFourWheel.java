package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDriveFourWheel implements IDrive {
    private final DcMotor fl;
    private final DcMotor fr;
    private final DcMotor bl;
    private final DcMotor br;
    private boolean isReverse;
    private final FtcGamePad driver;

    public TankDriveFourWheel(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br, FtcGamePad driver)
    {
        // this.robot = robot;
        // this.driver = driver;
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
        this.driver = driver;
    }

    @Override
    public boolean getIsReverse() {
        return isReverse;
    }

    @Override
    public void setIsReverse(boolean value) {
        isReverse = value;
    }

    @Override
    public void handle() {
        this.drive(this.driver.getLeftStickY() * -1, this.driver.getRightStickY() * -1);
    }

    public void drive(double left, double right) {
        this.fl.setPower(this.isReverse ? -right : left);
        this.bl.setPower(this.isReverse ? -right: left);
        this.fr.setPower(this.isReverse ? -left : right);
        this.br.setPower(this.isReverse ? -left: right);
    }


    @Override
    public void drive(double ly, double lx, double rx) {

    }

    @Override
    public void stop() {
        stopMotor(fl);
        stopMotor(fr);
        stopMotor(bl);
        stopMotor(br);
    }

    private void stopMotor(DcMotor m) {
        m.setPower(0);
    }

    @Override
    public void setMode(DcMotor.RunMode runMode) {

    }

    @Override
    public void driveToPosition(int leftPosition, int rightPosition) {

    }
}
