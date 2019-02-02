package org.firstinspires.ftc.teamcode.NewCode;

import org.firstinspires.ftc.teamcode.common.Config;

public class LightController {

    private Config config;
    private Robot robot;

    public LightController(Robot robot, Config config) {
        this.config = config;
        this.robot = robot;
    }

    public void toggleLights() {
        if(robot.lights.getPower() == config.getMaxLightBrightness()) {
            robot.lights.setPower(0);
        } else {
            robot.lights.setPower(config.getMaxLightBrightness());
        }
    }

}
