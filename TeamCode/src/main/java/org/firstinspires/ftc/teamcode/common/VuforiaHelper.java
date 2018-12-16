package org.firstinspires.ftc.teamcode.common;

import android.content.Context;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Autonomous_Teaching;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/**
 * Created by dmill on 10/28/2017.
 */

public class VuforiaHelper {

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;


    public enum GoldPosition {
        CENTER,
        LEFT,
        RIGHT,
        UNKNOWN
    }

    protected static final String VUFORIA_KEY = "AeWceoD/////AAAAGWvk7AQGLUiTsyU4mSW7gfldjSCDQHX76lt9iPO5D8zaboG428r" +
            "dS9WN0+AFpAlc/g4McLRAQIb5+ijFCPJJkLc+ynXYdhljdI2k9R4KL8t3MYk/tbmQ75st9VI7//2vNkp0JHV6oy4HXltxVFcEbtBYeT" +
            "BJ9CFbMW+0cMNhLBPwHV7RYeNPZRgxf27J0oO8VoHOlj70OYdNYos5wvDM+ZbfWrOad/cpo4qbAw5iB95T5I9D2/KRf1HQHygtDl8/O" +
            "tDFlOfqK6v2PTvnEbNnW1aW3vPglGXknX+rm0k8b0S7GFJkgl7SLq/HFNl0VEIVJGVQe9wt9PB6bJuxOMMxN4asy4rW5PRRBqasSM7O" +
            "Ll4W";

    protected static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    protected static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    protected static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    protected TFObjectDetector tfod;

    // Select which camera you want use.  The FRONT camera is the one on the same side as the screen.
    // Valid choices are:  BACK or FRONT
    protected static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private HardwareMap hardwareMap;
    private Context context;


    public void initVuforia(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.context = hardwareMap.appContext;
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        int tfodMonitorViewId = context.getResources().getIdentifier("tfodMonitorViewId", "id", context.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }


    public void ActivateTFOD() {
        tfod.activate();
    }

    public void DeactivateTFOD() {
        tfod.deactivate();
    }

    public GoldPosition DetectObjects() {

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getRecognitions();
            if (updatedRecognitions != null) {

                int goldMineralY = -1;
                int silverMineral1Y = -1;
                int silverMineral2Y = -1;

                for (Recognition recognition : updatedRecognitions) {

                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                        goldMineralY = (int) recognition.getTop();
                    } else if (silverMineral1Y == -1) {
                        silverMineral1Y = (int) recognition.getTop();
                    } else {
                        silverMineral2Y = (int) recognition.getTop();
                    }
                }


                if (goldMineralY != -1 && silverMineral1Y != -1 && silverMineral2Y != -1) {
                    if (goldMineralY < silverMineral1Y && goldMineralY < silverMineral2Y) {
                        return GoldPosition.LEFT;
                    } else if (goldMineralY > silverMineral1Y && goldMineralY > silverMineral2Y) {
                        return GoldPosition.RIGHT;
                    } else {
                        return GoldPosition.CENTER;
                    }
                }

            }
        }

        return GoldPosition.UNKNOWN;
    }

}
