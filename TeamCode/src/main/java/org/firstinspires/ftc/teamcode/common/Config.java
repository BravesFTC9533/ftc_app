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
    private final String PHONEOREANTATION = "phoneOreantation";
    private final String ROBOTSPECS = "robotSpecs";
    private final String TENSORFLOW_Y_THRESHOLD = "F_tensorFlowYThreshold";

    private final String INITIAL_TURN_CLOCKWISE = "initial_turn_clockwise";
    private final String INITIAL_TURN_COUNTERCLOCKWISE = "initial_turn_counterclockwise";

    private final String INTAKE_POWER = "intakePower";
    private final String LIFT_SILVER = "liftSilverTicks";
    private final String LIFT_GOLD = "liftGoldTicks";

    private final String SWINGARM_POWER = "swingArmPower";
    private final String MAX_SWING_TICKS = "swingArmMaxTicks";

    private Colors _color;
    private Positions _position;
    private double _delayStart;
    private double _speed;
    private int _maxLiftTicks;
    private int _maxSwingTicks;
    private Oreantation _phoneOreantation;
    private RobotSpecs _robotSpecs;
    private double _tensorFlowYThreshold;
    private double _maxLightBrightness;
    private Directions _liftReverse;

    private double _initialTurnDegreesCounterClockwise;
    private double _initialTurnDegreesClockwise;

    private double _intakePower;

    private int _liftSilverTicks;
    private int _liftGoldTicks;
    private double _swingArmPower;


    public Oreantation getPHONEOREANTATION () { return _phoneOreantation; }
    public void setPHONEOREANTATION (Oreantation oreantation) { _phoneOreantation = oreantation; }

    public double getTensorflowYThreshold () { return _tensorFlowYThreshold; }
    public void setTensorflowYThreshold (double value) {_tensorFlowYThreshold = value;}

    public RobotSpecs getROBOTSPECS () { return _robotSpecs; }
    public void setROBOTSPECS (RobotSpecs robotspecs) {_robotSpecs = robotspecs;};

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

    public int getMaxSwingTicks() { return _maxSwingTicks; }
    public void setMaxSwingTicks(int ticks) {
        _maxSwingTicks = ticks;
    }

    public int getMaxLiftTicks() { return _maxLiftTicks; }
    public void setMaxLiftTicks(int ticks) {
        _maxLiftTicks = ticks;
    }

    public double getMaxLightBrightness() { return  _maxLightBrightness;}
    public void setMaxLightBrightness(double brightness) {
        _maxLightBrightness = brightness;
    }

    public boolean getLiftReverse() { return _liftReverse == Directions.REVERSE; }
    public void setLiftReverse(boolean reverse) {
        _liftReverse = reverse ? Directions.REVERSE : Directions.NORMAL;
    }

    public double getIntakePower() { return  _intakePower;}
    public void setIntakePower(double value) {
        _intakePower = value;
    }


    public int getLiftSilverTicks() { return _liftSilverTicks; }
    public void setLiftSilverTicks(int ticks) { _liftSilverTicks = ticks; }

    public int getLiftGoldTicks() { return _liftGoldTicks; }
    public void setLifTGoldTicks(int ticks) { _liftGoldTicks = ticks; }

    public double getSwingArmPower() { return _swingArmPower;  }
    public void setSwingArmPower(double _swingArmPower) { this._swingArmPower = _swingArmPower;   }

    public void save() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COLOR, _color.name());
        editor.putString(POSITION, _position.name());
        editor.putFloat(DELAY_START, (float)_delayStart);
        editor.putFloat(SPEED, (float)_speed);
        editor.putInt(MAX_LIFT_TICKS, _maxLiftTicks);
        editor.putFloat(MAX_LIGHT_BRIGHTNESS, (float)_maxLightBrightness);
        editor.putBoolean(LIFT_REVERSE, _liftReverse == Directions.REVERSE ? true : false);
        editor.putFloat(TENSORFLOW_Y_THRESHOLD, (float) _tensorFlowYThreshold);
        editor.putString(PHONEOREANTATION, _phoneOreantation.name());
        editor.putString(ROBOTSPECS, _robotSpecs.name());
        editor.putFloat(INITIAL_TURN_CLOCKWISE, (float)_initialTurnDegreesClockwise);
        editor.putFloat(INITIAL_TURN_COUNTERCLOCKWISE, (float)_initialTurnDegreesCounterClockwise);
        editor.putFloat(INTAKE_POWER, (float)_intakePower);

        editor.putInt(MAX_SWING_TICKS, _maxSwingTicks);



        editor.putInt(LIFT_SILVER, _liftSilverTicks);
        editor.putInt(LIFT_GOLD, _liftGoldTicks);

        editor.putFloat(SWINGARM_POWER, (float)_swingArmPower);


        editor.commit();

    }

    public double get_initialTurnDegreesCounterClockwise() {
        return _initialTurnDegreesCounterClockwise;
    }

    public void set_initialTurnDegreesCounterClockwise(double _initialTurnDegreesCounterClockwise) {
        this._initialTurnDegreesCounterClockwise = _initialTurnDegreesCounterClockwise;
    }

    public double get_initialTurnDegreesClockwise() {
        return _initialTurnDegreesClockwise;
    }

    public void set_initialTurnDegreesClockwise(double _initialTurnDegreesClockwise) {
        this._initialTurnDegreesClockwise = _initialTurnDegreesClockwise;
    }

    public enum Directions {
        NORMAL, REVERSE;

        public static Directions toDirection(String direction) {
            try {
                return valueOf(direction);
            } catch(Exception ex) {
                return  NORMAL;
            }
        }
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

    public enum RobotSpecs {
        TEST, REAL;

        public static RobotSpecs toSpecs(String specs) {
            try {
                return valueOf(specs);
            } catch(Exception e) {
                return TEST;
            }
        }
    }
    public enum Oreantation {
        LEFT, RIGHT;

        public static Oreantation toOreantation(String oreantation) {
            try {
                return valueOf(oreantation);
            } catch(Exception ex) {
                return  LEFT;
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
        _maxLiftTicks = sp.getInt(MAX_LIFT_TICKS, 1000);
        _maxSwingTicks = sp.getInt(MAX_SWING_TICKS, 128);
        _maxLightBrightness = sp.getFloat(MAX_LIGHT_BRIGHTNESS, 0.5f);

        boolean shouldLiftBeReverse = sp.getBoolean(LIFT_REVERSE, false);

        _liftReverse = shouldLiftBeReverse ? Directions.REVERSE : Directions.NORMAL;

        //_liftReverse =
        _tensorFlowYThreshold = sp.getFloat(TENSORFLOW_Y_THRESHOLD, 360.0f);
        //TODO change to right for robot for competition.
        _phoneOreantation = Oreantation.toOreantation(sp.getString(PHONEOREANTATION, "LEFT"));
        //TODO change to REAL for robot for competition
        _robotSpecs = RobotSpecs.toSpecs(sp.getString(ROBOTSPECS, "TEST"));
        _initialTurnDegreesClockwise = sp.getFloat(INITIAL_TURN_CLOCKWISE, 70);
        _initialTurnDegreesCounterClockwise = sp.getFloat(INITIAL_TURN_COUNTERCLOCKWISE, 70);
        _intakePower = sp.getFloat(INTAKE_POWER, 1.0f);

        _liftSilverTicks = sp.getInt(LIFT_SILVER, 100);
        _liftGoldTicks = sp.getInt(LIFT_GOLD, 120);

        _swingArmPower = sp.getFloat(SWINGARM_POWER, 0.5f);
    }

}
