package com.dsi.ant.antplus.pluginsampler.pulsezones;

import com.dsi.ant.antplus.pluginsampler.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by ksenia on 09.01.19.
 */

public class PulseZoneUtils {
    /**
     * Calculates the low and high limit of particular pulse zone
     */
    public static PulseLimits calculateZonePulse(PulseZoneSettings settings) {
        int hrReserve = settings.getMaxHr() - settings.getRestHr();
        int lowPulseLimit = 0, highPulseLimit = 0;
        switch(settings.getZoneRadioButtonId()) {
            case R.id.radioButton_Zone1:
                lowPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.4);
                highPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.51);
                break;
            case R.id.radioButton_Zone2:
                lowPulseLimit = (int)Math.round(settings.getRestHr()+ hrReserve*0.52);
                highPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.63);
                break;
            case R.id.radioButton_Zone3:
                lowPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.64);
                highPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.75);
                break;
            case R.id.radioButton_Zone4:
                lowPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.76);
                highPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.87);
                break;
            case R.id.radioButton_Zone5:
                lowPulseLimit = (int)Math.round(settings.getRestHr() + hrReserve*0.88);
                highPulseLimit = settings.getMaxHr();
                break;
        }
        return new PulseLimits(lowPulseLimit, highPulseLimit);
    }

    /**
     * Calculates maximum heart rate value based on age and gender
     * @param ageFieldValue - age value
     * @param isFemale - gender value
     * @return
     */
    public static int calculateMaxHrFieldValue(int ageFieldValue, boolean isFemale) {
        int maxHrFieldValue;
        if(isFemale) {
            maxHrFieldValue = (int) Math.round(209 - ageFieldValue * 0.7);
        } else {
            maxHrFieldValue = (int) Math.round(214 - ageFieldValue * 0.8);
        }
        return maxHrFieldValue;
    }

    /**
     * Calculates upper limit for axis maximum
     * @param highPulseLimit - maximum pulse
     * @return
     */
    public static float calculateUpperRangeLimit(int highPulseLimit) {
        return highPulseLimit + highPulseLimit*0.1f;
    }

    /**
     * Calculates lower limit for axis minimum
     * @param lowPulseLimit - rest pulse
     * @return
     */
    public static float calculateLowerRangeLimit(int lowPulseLimit) {
        return lowPulseLimit - lowPulseLimit*0.1f;
    }

    /**
     * Converts time from milliseconds to String in format hh:mm:ss
     * @param miliSeconds - time in milliseconds
     * @return
     */
    public static String fromMillisecondsToTime(long miliSeconds) {
        int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
        int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
        return String.format("%02d:%02d:%02d", hrs, min, sec);
    }
}
