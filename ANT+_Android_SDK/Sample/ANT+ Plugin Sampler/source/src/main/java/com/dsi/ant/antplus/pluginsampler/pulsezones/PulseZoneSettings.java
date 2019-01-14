package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.content.Context;
import android.content.SharedPreferences;

import com.dsi.ant.antplus.pluginsampler.R;

import java.util.Arrays;

/**
 * Created by ksenia on 01.01.19.
 */

public class PulseZoneSettings {
    private static final String PARAM_APP_PREFERENCES = "prefID";
    private int gender;
    private int age;
    private int restHr;
    private int maxHr;
    private int zone;
    private SharedPreferences sharedPreferences;

    /**
     *Initialization of pulseZoneSettings
     * @param context - context
     */
    public PulseZoneSettings(Context context) {
        sharedPreferences = context.getSharedPreferences(PARAM_APP_PREFERENCES, Context.MODE_PRIVATE);
        this.gender = Preferences.getUserSex(sharedPreferences);
        this.age = Preferences.getAge(sharedPreferences);
        this.restHr = Preferences.getRestHr(sharedPreferences);
        this.maxHr = Preferences.getMaxHr(sharedPreferences);
        this.zone = Preferences.getPulseZone(sharedPreferences);
    }

    /**
     * Setts gender
     * @param gender - gender int value
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * Setts age
     * @param age - age int value
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets rest heart rate
     * @param restHr - rest heart rate int value
     */
    public void setRestHr(int restHr) {
        this.restHr = restHr;
    }

    /**
     * Sets maximum heart rate
     * @param maxHr - maximum heart rate int value
     */
    public void setMaxHr(int maxHr) {
        this.maxHr = maxHr;
    }

    /**
     * Sets heart rate zone
     * @param zone - heart rate zone int value
     */
    public void setZone(int zone) {
        this.zone = zone;
    }

    /**
     * Returns if gender is Female
     * @return - true, if gender is female, false, if gender is male
     */
    public boolean isFemale() {
        return gender == R.id.radioButton_Female;
    }

    /**
     * returns zone number
     * @return - number of pulse zone
     */
    public int getZoneId() {
        switch (zone) {
            case R.id.radioButton_Zone1: return 1;
            case R.id.radioButton_Zone2: return 2;
            case R.id.radioButton_Zone3: return 3;
            case R.id.radioButton_Zone4: return 4;
            case R.id.radioButton_Zone5: return 5;
            default: return 1;
        }
    }

    /**
     * Getter method for gender
     * @return - gender
     */
    public int getGender() {
        return gender;
    }

    /**
     * Getter method for age
     * @return - age
     */
    public int getAge() {
        return age;
    }

    /**
     * Getter method for rest heart rate
     * @return - rest heart rate
     */
    public int getRestHr() {
        return restHr;
    }

    /**
     * Getter method for maximum heart rate
     * @return - maximum heart rate
     */
    public int getMaxHr() {
        return maxHr;
    }

    /**
     * Getter method for pulse zone
     * @return - pulse zone
     */
    public int getZone() {
        return zone;
    }

    /**
     * Saves values of gender, age. rest hr, maximum hr, pulse zone in shared preferences
     */
    public void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Preferences.USER_SEX_KEY.toString(), gender);
        editor.putInt(Preferences.AGE_KEY.toString(), age);
        editor.putInt(Preferences.REST_HR_KEY.toString(), restHr);
        this.maxHr = maxHr == 0 ? PulseZoneUtils.calculateMaxHrFieldValue(age, isFemale()) : maxHr;
        editor.putInt(Preferences.MAX_HR_KEY.toString(), maxHr);
        editor.putInt(Preferences.PULSE_ZONE_KEY.toString(), zone);
        editor.apply();
   }

    @Override
    public String toString(){
        return Arrays.asList(gender, age, restHr, maxHr, zone).toString();
    }

    enum Preferences {
        USER_SEX_KEY("userSexKey"),
        AGE_KEY("ageKey"),
        REST_HR_KEY("restHrKey"),
        MAX_HR_KEY("maxHrKey"),
        PULSE_ZONE_KEY("pulseZoneKey");

        private String prefName;

        Preferences(String prefName) {
            this.prefName = prefName;
        }

        @Override
        public String toString() {
            return prefName;
        }

        public static int getAge(SharedPreferences prefs) {
            return prefs.getInt(Preferences.AGE_KEY.toString(), 0);
        }

        public static int getUserSex(SharedPreferences prefs) {
            return prefs.getInt(Preferences.USER_SEX_KEY.toString(), R.id.radioButton_Female);
        }

        public static int getRestHr(SharedPreferences prefs) {
            return prefs.getInt(Preferences.REST_HR_KEY.toString(), 0);
        }

        public static int getMaxHr(SharedPreferences prefs) {
            return prefs.getInt(Preferences.MAX_HR_KEY.toString(), 0);
        }

        public static int getPulseZone(SharedPreferences prefs) {
            return prefs.getInt(Preferences.PULSE_ZONE_KEY.toString(), R.id.radioButton_Zone1);
        }
    }
}
