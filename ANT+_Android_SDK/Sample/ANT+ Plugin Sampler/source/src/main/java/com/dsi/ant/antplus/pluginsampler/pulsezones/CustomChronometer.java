package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;

/**
 * Created by ksenia on 06.01.19.
 */

public class CustomChronometer extends Chronometer {
    private boolean isRunning = false;

    public CustomChronometer(Context context) {
        super(context);
    }

    public CustomChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void start() {
        super.start();
        isRunning = true;
    }

    @Override
    public void stop() {
        super.stop();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
