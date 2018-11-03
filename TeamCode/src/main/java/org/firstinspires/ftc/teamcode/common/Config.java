package org.firstinspires.ftc.teamcode.common;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 9533 on 10/28/2017.
 */

public class Config {


    public Colors getColor() {
        return Colors.toColor(sp.getString("color", "RED"));
    }

    public void setColor(Colors color) {
        sp.edit().putString("color", color.name()).apply();
    }

    public Positions getPosition() {
        return  Positions.toPosition(sp.getString("position", "GOLD"));
    }

    public void setPosition(Positions position) {
        sp.edit().putString("position", position.name()).apply();
    }

    public double getDelayStart() {
        return sp.getFloat("delayStart", 0);
    }

    public void setDelayStart(double delayStart) {
        sp.edit().putFloat("delayStart", (float)delayStart).apply();
    }

    public double getSpeed() {
        return sp.getFloat("speed", 0.85f);
    }

    public void setSpeed(double speed) {
        sp.edit().putFloat("speed", (float)speed).apply();
    }


    public double getMaxLightBrightness() { return  sp.getFloat("maxbrightnes", 0.5f);}
    public void setMaxLightBrightness(double brightness) {
        sp.edit().putFloat("maxbrightness", (float)brightness).apply();
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
        sp = context.getSharedPreferences("robotconfig", Context.MODE_PRIVATE);

    }

}
