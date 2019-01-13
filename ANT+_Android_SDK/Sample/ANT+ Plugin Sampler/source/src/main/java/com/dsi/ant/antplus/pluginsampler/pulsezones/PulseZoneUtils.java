package com.dsi.ant.antplus.pluginsampler.pulsezones;

import java.util.concurrent.TimeUnit;

/**
 * Created by ksenia on 09.01.19.
 */

public class PulseZoneUtils {
    /**
     * Calculates the low and high limit of particular pulse zone
     */
    public static PulseLimits calculateZonePulse(int restHr, int maxHr, int zone) {
        int hrReserve = maxHr - restHr;
        int lowPulseLimit, highPulseLimit;
        if(zone == 1) {
            lowPulseLimit = (int)Math.round(restHr + hrReserve*0.4);
            highPulseLimit = (int)Math.round(restHr + hrReserve*0.51);
        } else if(zone == 2) {
            lowPulseLimit = (int)Math.round(restHr + hrReserve*0.52);
            highPulseLimit = (int)Math.round(restHr + hrReserve*0.63);
        } else if(zone == 3) {
            lowPulseLimit = (int)Math.round(restHr + hrReserve*0.64);
            highPulseLimit = (int)Math.round(restHr + hrReserve*0.75);
        } else if(zone == 4) {
            lowPulseLimit = (int)Math.round(restHr + hrReserve*0.76);
            highPulseLimit = (int)Math.round(restHr + hrReserve*0.87);
        } else {
            lowPulseLimit = (int)Math.round(restHr + hrReserve*0.88);
            highPulseLimit = maxHr;
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
