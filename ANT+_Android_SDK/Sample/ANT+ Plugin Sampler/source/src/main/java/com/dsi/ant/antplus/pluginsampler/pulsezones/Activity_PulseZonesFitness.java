package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

import com.dsi.ant.antplus.pluginsampler.R;
import com.dsi.ant.antplus.pluginsampler.multidevicesearch.Activity_MultiDeviceSearchSampler;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ksenia on 30.12.18.
 */

public class Activity_PulseZonesFitness extends Activity {
    AntPlusHeartRatePcc hrPcc = null;
    PccReleaseHandle<AntPlusHeartRatePcc> releaseHandle = null;
    Logger logger = Logger.getLogger(this.getClass().getName());

    TextView tv_status;
    TextView tv_heartRate;

    GraphView graph;
    long startTimeInMillisec;
    LineGraphSeries<DataPoint> series;

    PulseZoneUtils utility;

    HRRecordsRepository repository;
    private CustomChronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_zone_fitness);

        tv_status = findViewById(R.id.textView_ZoneStatus);
        tv_heartRate = findViewById(R.id.textView_HeartRatePulseZone);
        chronometer = findViewById(R.id.chronometer);
        tv_status.setText(R.string.connection_string);

        graph = findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(30);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setLabelVerticalWidth(60);
        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);

        Intent intent = getIntent();
        utility = new PulseZoneUtils(intent);
        utility.calculateZonePulse();

        repository = new HRRecordsRepository(this);
        handleReset();
    }


    /**
     * Resets the PCC connection to request access again and clears any existing display data.
     */
    protected void handleReset() {
        //Release the old access if it exists
        if(releaseHandle != null) {
            releaseHandle.close();
        }
        requestAccessToPcc();
    }

    /**
     * Switches the active view to the data display and subscribes to all the data events
     */
    public void subscribeToHrEvents() {
        hrPcc.subscribeHeartRateDataEvent((estTimestamp, eventFlags, computedHeartRate, heartBeatCount, heartBeatEventTime, dataState) -> {
            // Mark heart rate with asterisk if zero detected
            final String textHeartRate = String.valueOf(computedHeartRate);

            //Set vibration
            if(computedHeartRate > utility.getHighPulseLimit() || computedHeartRate < utility.getLowPulseLimit()) {
                // Get instance of Vibrator from current Context
                Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                // Vibrate for 75 milliseconds
                mVibrator.vibrate(75);
            }

            runOnUiThread(() -> {
                //Synchronization of starting time of the readings and chronometer start
                if(!chronometer.isRunning()) {
                    startTimeInMillisec = System.currentTimeMillis();
                    chronometer.start();
                }

                int realTime = (int)(System.currentTimeMillis() - startTimeInMillisec)/1000;
                series.appendData(new DataPoint(realTime, computedHeartRate), true, 1000);

                repository.addNewRecord(computedHeartRate);

                tv_heartRate.setText(textHeartRate);
            });
        });
    }


    //Handle the result, connecting to events on success or reporting failure to user.
    protected IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver =
            (result, resultCode, initialDeviceState) -> {
                tv_status.setText(R.string.connection_string);
                switch(resultCode) {
                    case SUCCESS:
                        hrPcc = result;
                        tv_status.setText("Pulse range: " + utility.getLowPulseLimit() + "-" + utility.getHighPulseLimit());
                        subscribeToHrEvents();
                        break;
                    case CHANNEL_NOT_AVAILABLE:
                        tv_status.setText(R.string.error_string);
                        break;
                    case ADAPTER_NOT_DETECTED:
                        tv_status.setText(R.string.error_string);
                        break;
                    case BAD_PARAMS:
                        tv_status.setText(R.string.error_string);
                        break;
                    case OTHER_FAILURE:
                        tv_status.setText(R.string.error_string);
                        break;
                    case DEPENDENCY_NOT_INSTALLED:
                        tv_status.setText(R.string.error_string);
                        break;
                    case USER_CANCELLED:
                        tv_status.setText(R.string.error_string);
                        break;
                    case UNRECOGNIZED:
                        tv_status.setText(R.string.error_string);
                        break;
                    default:
                        break;
                }
            };

    //Receives state changes and shows it on the status display line
    protected  IDeviceStateChangeReceiver base_IDeviceStateChangeReceiver =
            (DeviceState newDeviceState) ->
                    runOnUiThread(() -> logger.log(Level.INFO, hrPcc.getDeviceName() + ": " + newDeviceState));

    protected void requestAccessToPcc() {
        Intent intent = getIntent();
        if (intent.hasExtra(Activity_MultiDeviceSearchSampler.EXTRA_KEY_MULTIDEVICE_SEARCH_RESULT)) {
            // device has already been selected through the multi-device search
            MultiDeviceSearch.MultiDeviceSearchResult result = intent
                    .getParcelableExtra(Activity_MultiDeviceSearchSampler.EXTRA_KEY_MULTIDEVICE_SEARCH_RESULT);
            releaseHandle = AntPlusHeartRatePcc.requestAccess(this, result.getAntDeviceNumber(), 0,
                    base_IPluginAccessResultReceiver, base_IDeviceStateChangeReceiver);
        } else {
            // starts the plugins UI search
            releaseHandle = AntPlusHeartRatePcc.requestAccess(this, this,
                    base_IPluginAccessResultReceiver, base_IDeviceStateChangeReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        chronometer.stop();
        repository.closeDb();
        releaseHandle.close();
        super.onDestroy();
    }
}
