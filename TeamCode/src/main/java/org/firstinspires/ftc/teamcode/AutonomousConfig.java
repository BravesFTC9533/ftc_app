package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.SimpleMenu;

@Autonomous(name="Autonomous Config", group="Competition")
public class AutonomousConfig extends LinearOpMode {


    public static SimpleMenu menu = new SimpleMenu("Autonomous Menu");


    @Override
    public void runOpMode() {
        Config config = new Config(hardwareMap.appContext);


        menu.clearOptions();

        menu.addOption("Team", Config.Colors.class, config.getColor());
        menu.addOption("Position", Config.Positions.class, config.getPosition());



        menu.addOption("Max Lift Ticks", 10000, 0, 1, config.getMaxLiftTicks());
        menu.addOption("Lift Ticks - Silver", 10000, 0, 1, config.getLiftSilverTicks());
        menu.addOption("Lift Ticks - Gold", 10000, 0, 1, config.getLiftGoldTicks());

        menu.addOption("Max Swing Ticks", 10000, 0, 1, config.getMaxSwingTicks());

        menu.addOption("Speed", 1, 0, 0.05, config.getSpeed());
        menu.addOption("Intake Power", 1, 0, 0.05, config.getIntakePower());
        menu.addOption("Swingarm Power", 1, 0, 0.05, config.getSwingArmPower());

        menu.addOption("Delay Start", 30, 0, 0.1, config.getDelayStart());
        menu.addOption("Max Light Brightness", 1, 0, 0.05, config.getMaxLightBrightness());

        //menu.addOption("TF Y Thresh", 1000, 0, 1, config.getTensorflowYThreshold());
        //menu.addOption("Oreantation", Config.Oreantation.class, config.getPHONEOREANTATION());

        //menu.addOption("What Robot?", Config.RobotSpecs.class, config.getROBOTSPECS());

        menu.addOption("Initial Turn CW", 90, 0, 1, config.get_initialTurnDegreesClockwise());
        menu.addOption("Initial Turn CCW", 90, 0, 1, config.get_initialTurnDegreesCounterClockwise());

        menu.addOption("Lift Reverse", Config.Directions.class, config.getLiftReverse() ? Config.Directions.REVERSE : Config.Directions.NORMAL);
//        menu.addOption("kP", 1000, 0, 0.01, config.kp);
//        menu.addOption("kI", 1000, 0, 0.01, config.ki);
//        menu.addOption("kD", 1000, 0, 0.01, config.kd);

        menu.setGamepad(gamepad1);
        menu.setTelemetry(telemetry);

        waitForStart();

        while (opModeIsActive()) {

            menu.displayMenu();


            config.setMaxLiftTicks((int)Double.parseDouble(menu.getCurrentChoiceOf("Max Lift Ticks")));
            config.setMaxSwingTicks((int)Double.parseDouble(menu.getCurrentChoiceOf("Max Swing Ticks")));
            config.setLiftSilverTicks((int)Double.parseDouble(menu.getCurrentChoiceOf("Lift Ticks - Silver")));
            config.setLifTGoldTicks((int)Double.parseDouble(menu.getCurrentChoiceOf("Lift Ticks - Gold")));

            switch(menu.getCurrentChoiceOf("Lift Reverse")) {
                case "REVERSE":
                    config.setLiftReverse(true);
                    break;
                case "NORMAL":
                    config.setLiftReverse(false);
                    break;

            }

            config.setDelayStart(Double.parseDouble(menu.getCurrentChoiceOf("Delay Start")));



           /// config.setTensorflowYThreshold(Double.parseDouble(menu.getCurrentChoiceOf("TF Y Thresh")));
            config.setSpeed(Double.parseDouble(menu.getCurrentChoiceOf("Speed")));
            config.setIntakePower(Double.parseDouble(menu.getCurrentChoiceOf("Intake Power")));
            config.setSwingArmPower(Double.parseDouble(menu.getCurrentChoiceOf("Swingarm Power")));
            config.setMaxLightBrightness(Double.parseDouble(menu.getCurrentChoiceOf("Max Light Brightness")));

            config.setMaxLightBrightness(Double.parseDouble(menu.getCurrentChoiceOf("Initial Turn CW")));
            config.setMaxLightBrightness(Double.parseDouble(menu.getCurrentChoiceOf("Initial Turn CCW")));

            switch (menu.getCurrentChoiceOf("Position")) {
                case "SILVER":
                    config.setPosition(Config.Positions.SILVER);
                    break;
                case "GOLD":
                    config.setPosition(Config.Positions.GOLD);
                    break;
            }

            switch (menu.getCurrentChoiceOf("Team")) {
                case "RED":
                    config.setColor(Config.Colors.RED);

                    //set red team
                    break;
                case "BLUE":
                    config.setColor(Config.Colors.BLUE);
                    //set blue team
                    break;
            }



            sleep(50);
        }

        config.save();


    }

}
