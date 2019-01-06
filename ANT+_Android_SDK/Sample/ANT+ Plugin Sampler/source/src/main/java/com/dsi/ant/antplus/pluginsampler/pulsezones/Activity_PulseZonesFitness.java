package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

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
    Bundle bundle;

    int gender;
    int age;
    int restHr;
    int maxHr;
    int zone;
    int lowPulseLimit;
    int highPulseLimit;
    SQLiteDatabase database;

    private CustomChronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_zone_fitness);

        tv_status = findViewById(R.id.textView_ZoneStatus);
        tv_heartRate = findViewById(R.id.textView_HeartRatePulseZone);
        chronometer = findViewById(R.id.chronometer);
        tv_status.setText("Connecting...");

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

        settingsInit();
        calculateZonePulse(restHr, maxHr, zone);

        //Create database
        database =  new FitnessSQLiteDBHelper(this).getWritableDatabase();

        handleReset();
    }

    /**
     * Initializes all the setting fields
     */
    private void settingsInit() {
        bundle = getIntent().getExtras();
        PulseZoneSettings settings = bundle.getParcelable("settings");

        gender = settings.getGender();
        age = settings.getAge();
        restHr = settings.getrestHr();
        maxHr = settings.getMaxHr();
        zone = settings.getZone();
    }

    /**
     * Calculates the low and high limit of particular pulse zone
     * @param restHr - resting pulse value
     * @param maxHr - maximum pulse value
     * @param zone - chosen zone
     */
    private void calculateZonePulse(int restHr, int maxHr, int zone) {
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
            if(computedHeartRate > highPulseLimit || computedHeartRate < lowPulseLimit) {
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

                //Insert values into database
                ContentValues values = new ContentValues();
                values.put(FitnessSQLiteDBHelper.RECORDS_COLUMN_HR, computedHeartRate);
                values.put(FitnessSQLiteDBHelper.RECORDS_TIMESTAMP, realTime);
                long newRowId = database.insert(FitnessSQLiteDBHelper.RECORDS_TABLE_NAME, null, values);
                logger.info("The new row id is " + newRowId);

                tv_heartRate.setText(textHeartRate);
            });
        });
    }

    //Handle the result, connecting to events on success or reporting failure to user.
    protected IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver =
            (result, resultCode, initialDeviceState) -> {
                tv_status.setText("Connecting...");
                switch(resultCode) {
                    case SUCCESS:
                        hrPcc = result;
                        tv_status.setText("Pulse range: " + lowPulseLimit + "-" + highPulseLimit);
                        subscribeToHrEvents();
                        break;
                    case CHANNEL_NOT_AVAILABLE:
                        tv_status.setText("Error");
                        break;
                    case ADAPTER_NOT_DETECTED:
                        tv_status.setText("Error");
                        break;
                    case BAD_PARAMS:
                        tv_status.setText("Error");
                        break;
                    case OTHER_FAILURE:
                        tv_status.setText("Error");
                        break;
                    case DEPENDENCY_NOT_INSTALLED:
                        tv_status.setText("Error");
                        break;
                    case USER_CANCELLED:
                        tv_status.setText("Cancelled");
                        break;
                    case UNRECOGNIZED:
                        tv_status.setText("Error");
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
        database.close();
        releaseHandle.close();
        super.onDestroy();
    }
}
