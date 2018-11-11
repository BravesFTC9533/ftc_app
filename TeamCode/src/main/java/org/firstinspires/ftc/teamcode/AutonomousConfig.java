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

        menu.addOption("Speed", 1, 0, 0.05, config.getSpeed());
        menu.addOption("Delay Start", 30, 0, 0.1, config.getDelayStart());
        menu.addOption("Max Light Brightness", 1, 0, 0.05, config.getMaxLightBrightness());

        menu.addOption("TF Y Thresh", 1000, 0, 1, config.getTensorflowYThreshold());
        menu.addOption("Oreantation", Config.Oreantation.class, config.getPHONEOREANTATION());


//        menu.addOption("kP", 1000, 0, 0.01, config.kp);
//        menu.addOption("kI", 1000, 0, 0.01, config.ki);
//        menu.addOption("kD", 1000, 0, 0.01, config.kd);

        menu.setGamepad(gamepad1);
        menu.setTelemetry(telemetry);

        waitForStart();

        while (opModeIsActive()) {

            menu.displayMenu();

            config.setDelayStart(Double.parseDouble(menu.getCurrentChoiceOf("Delay Start")));
            config.setTensorflowYThreshold(Double.parseDouble(menu.getCurrentChoiceOf("TF Y Thresh")));
            config.setSpeed(Double.parseDouble(menu.getCurrentChoiceOf("Speed")));
            config.setMaxLightBrightness(Double.parseDouble(menu.getCurrentChoiceOf("Max Light Brightness")));


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
