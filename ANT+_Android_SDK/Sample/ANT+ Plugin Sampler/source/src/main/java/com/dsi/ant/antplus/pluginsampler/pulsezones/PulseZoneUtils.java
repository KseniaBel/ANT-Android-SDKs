package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.os.Bundle;
import android.content.Intent;

/**
 * Created by ksenia on 09.01.19.
 */

public class PulseZoneUtils {
    private int gender;
    private int age;
    private int restHr;
    private int maxHr;
    private int zone;
    private int lowPulseLimit;
    private int highPulseLimit;

    /**
     * Initializes all the setting fields
     */
    public PulseZoneUtils(Intent intent) {
        Bundle bundle = intent.getExtras();
        PulseZoneSettings settings = bundle.getParcelable("settings");

        gender = settings.getGender();
        age = settings.getAge();
        restHr = settings.getrestHr();
        maxHr = settings.getMaxHr();
        zone = settings.getZone();
    }

    /**
     * Calculates the low and high limit of particular pulse zone
     */
    public void calculateZonePulse() {
        int hrReserve = maxHr - restHr;
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
            lowPulseLimit = (int)Math.round(restHr + hrReserve*0.99);
            highPulseLimit = maxHr;
        }
    }

    public int getLowPulseLimit() {
        return lowPulseLimit;
    }

    public int getHighPulseLimit() {
        return highPulseLimit;
    }
}
