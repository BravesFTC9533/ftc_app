package org.firstinspires.ftc.teamcode.common;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 9533 on 10/28/2017.
 */

public class Config {

    private final String COLOR ="color";
    private final String POSITION ="position";
    private final String DELAY_START ="delayStart";
    private final String SPEED = "speed";
    private final String MAX_LIFT_TICKS = "maxLiftTicks";
    private final String MAX_LIGHT_BRIGHTNESS = "maxBrightness";
    private final String LIFT_REVERSE = "liftReverse";
    private final String SETTINGS_NAME = "robotconfig2";

    private final String TENSORFLOW_Y_THRESHOLD = "F_tensorFlowYThreshold";

    private Colors _color;
    private Positions _position;
    private double _delayStart;
    private double _speed;
    private int _maxLiftTicks;
    private double _tensorFlowYThreshold;
    private double _maxLightBrightness;
    private boolean _liftReverse;


    public double getTensorflowYThreshold () { return _tensorFlowYThreshold; }
    public void setTensorflowYThreshold (double value) {_tensorFlowYThreshold = value;}

    public Colors getColor() { return _color; }
    public void setColor(Colors color) {
        _color = color;
    }

    public Positions getPosition() { return  _position; }
    public void setPosition(Positions position) {
        _position = position;
    }

    public double getDelayStart() {
        return _delayStart;
    }
    public void setDelayStart(double delayStart) {
        _delayStart = delayStart;
    }

    public double getSpeed() { return _speed;}
    public void setSpeed(double speed) {
        _speed = speed;
    }

    public int getMaxLiftTicks() { return _maxLiftTicks; }
    public void setMaxLiftTicks(int ticks) {
        _maxLiftTicks = ticks;
    }

    public double getMaxLightBrightness() { return  _maxLightBrightness;}
    public void setMaxLightBrightness(double brightness) {
        _maxLightBrightness = brightness;
    }

    public boolean getLiftReverse() { return _liftReverse; }
    public void setLiftReverse(boolean reverse) {
        _liftReverse = reverse;
    }


    public void save() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COLOR, _color.name());
        editor.putString(POSITION, _position.name());
        editor.putFloat(DELAY_START, (float)_delayStart);
        editor.putFloat(SPEED, (float)_speed);
        editor.putInt(MAX_LIFT_TICKS, _maxLiftTicks);
        editor.putFloat(MAX_LIGHT_BRIGHTNESS, (float)_maxLightBrightness);
        editor.putBoolean(LIFT_REVERSE, _liftReverse);
        editor.putFloat(TENSORFLOW_Y_THRESHOLD, (float) _tensorFlowYThreshold);

        editor.commit();

    }
    public enum Colors {
        RED, BLUE;

        public static Colors toColor(String color) {
            try {
                return valueOf(color);
            } catch(Exception ex) {
                return  RED;
            }
        }
    }
    public enum Positions {
        GOLD, SILVER;

        public static Positions toPosition(String position) {
            try {
                return  valueOf(position);
            } catch (Exception ex){
                return  GOLD;
            }
        }
    }

    Context context;
    SharedPreferences sp;



    public Config(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        _color = Colors.toColor(sp.getString(COLOR, "RED"));
        _position = Positions.toPosition(sp.getString(POSITION, "GOLD"));
        _delayStart = sp.getFloat(DELAY_START, 0.2f);
        _speed = sp.getFloat(SPEED, 0.85f);
        _maxLiftTicks = sp.getInt(MAX_LIFT_TICKS, 100);
        _maxLightBrightness = sp.getFloat(MAX_LIGHT_BRIGHTNESS, 0.5f);
        _liftReverse = sp.getBoolean(LIFT_REVERSE, false);
        _tensorFlowYThreshold = sp.getFloat(TENSORFLOW_Y_THRESHOLD, 360.0f);

    }

}
