package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dsi.ant.antplus.pluginsampler.R;
import com.dsi.ant.antplus.pluginsampler.multidevicesearch.Activity_MultiDeviceSearchSampler;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ksenia on 30.12.18.
 */

public class Activity_PulseZonesFitness extends Activity {
    public static final String START_TIMING = "startTiming";
    public static final String WORKOUT_TIME = "workoutTime";
    private AntPlusHeartRatePcc hrPcc = null;
    private PccReleaseHandle<AntPlusHeartRatePcc> releaseHandle = null;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private TextView tv_status;
    private TextView tv_heartRate;
    private ImageButton btn_pauseResume;
    private ImageButton btn_stop;

    private List<Integer> readingsBuffer;
    private ScheduledExecutorService service;
    private LineChart graph;

    private HRRecordsRepository repository;
    private PulseZoneSettings pulseSettings;
    private CustomChronometer chronometer;
    private PulseLimits pulseLimits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_zone_fitness);

        pulseSettings = new PulseZoneSettings(this.getApplicationContext());
        logger.info("Restore setting: " + pulseSettings.toString());
        pulseLimits = PulseZoneUtils.calculateZonePulse(pulseSettings);
        logger.info("low: " + pulseLimits.getLowPulseLimit() + " high: " + pulseLimits.getHighPulseLimit());
        readingsBuffer = Collections.synchronizedList(new ArrayList<>());

        tv_status = findViewById(R.id.textView_ZoneStatus);
        tv_heartRate = findViewById(R.id.textView_HeartRatePulseZone);
        chronometer = findViewById(R.id.chronometer);
        tv_status.setText(R.string.connection_string);
        btn_pauseResume = findViewById(R.id.button_pause);
        btn_stop = findViewById(R.id.button_stop);
        btn_stop.setVisibility(View.GONE);
        graph = findViewById(R.id.graph);
        setupChart();
        setLegend();
        setupAxes();
        setupData();

        repository = new HRRecordsRepository(this);
        handleReset();

        btn_pauseResume.setOnClickListener(view -> {
            if (chronometer.isRunning()) {
                unsubscribeToHrEvents();
                btn_pauseResume.setImageResource(R.drawable.play_button2);
                btn_stop.setVisibility(View.VISIBLE);
            }else{
                subscribeToHrEvents();
                btn_pauseResume.setImageResource(R.drawable.pause_button2);
                btn_stop.setVisibility(View.GONE);
            }
        });

        btn_stop.setOnClickListener(view -> {
            unsubscribeToHrEvents();
            Intent intent = new Intent(this, Activity_WorkoutStatistics.class);
            intent.putExtra(START_TIMING, chronometer.getStartTime());
            intent.putExtra(WORKOUT_TIME, chronometer.getText());
            startActivity(intent);
            finish();
        });
    }

    //Creates and displays graph
    private void setupChart() {
        // disable description text
        graph.getDescription().setEnabled(false);
        // enable touch gestures
        graph.setTouchEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        graph.setPinchZoom(true);
        // enable scaling
        graph.setScaleEnabled(true);
        graph.setDrawGridBackground(false);
        // set an alternative background color
        graph.setBackgroundColor(Color.DKGRAY);
    }

    private void setupAxes() {
        XAxis xl = graph.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = graph.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(PulseZoneUtils.calculateUpperRangeLimit(pulseLimits.getHighPulseLimit()));
        leftAxis.setAxisMinimum(PulseZoneUtils.calculateLowerRangeLimit(pulseLimits.getLowPulseLimit()));
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = graph.getAxisRight();
        rightAxis.setEnabled(false);

        // Add a limit line
        LimitLine upperLimitLine = getLimitLine(pulseLimits.getHighPulseLimit(), "Upper limit");
        LimitLine lowerLimitLine = getLimitLine(pulseLimits.getLowPulseLimit(), "Lower limit");
        // reset all limit lines to avoid overlapping lines
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upperLimitLine);
        leftAxis.addLimitLine(lowerLimitLine);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);
    }

    @NonNull
    private LimitLine getLimitLine(int limit, String label) {
        LimitLine ll = new LimitLine(limit, label);
        ll.setLineWidth(2f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll.setTextSize(10f);
        ll.setTextColor(Color.WHITE);
        return ll;
    }

    private void setupData() {
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        // add empty data
        graph.setData(data);
    }

    private void setLegend() {
        // get the legend (only possible after setting data)
        Legend l = graph.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextColor(Color.WHITE);
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Heart rate data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColors(ColorTemplate.VORDIPLOM_COLORS[0]);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        return set;
    }

    /**
     * Adds an entry of heart rate to the graph
     * @param time - the time of the reading
     * @param hrValue - the heart rate value
     */
    private void addEntry(int time, int hrValue) {
        logger.info("Add entry: time: " + time + " heart Reat: " + hrValue);
        LineData data = graph.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            YAxis leftAxis = graph.getAxisLeft();

            //increase axis maximum heart rate by 5 in case heart rate value is bigger than current maximum or
            //Decrease axis minimum heart rate by 5 in case heart rate value is smaller than current minimum
            if(hrValue > leftAxis.getAxisMaximum()) {
                leftAxis.setAxisMaximum(hrValue + 5);
            } else if(hrValue < leftAxis.getAxisMinimum()) {
                leftAxis.setAxisMinimum(hrValue - 5);
            }
            leftAxis.setDrawGridLines(true);

            data.addEntry(new Entry(time, hrValue), 0);

            // let the chart know it's data has changed
            data.notifyDataChanged();
            graph.notifyDataSetChanged();

            // limit the number of visible entries
            graph.setVisibleXRangeMaximum(20);

            // move to the latest entry
            graph.moveViewToX(data.getEntryCount());
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
     * Switches the active view to the data display and subscribes to all the data events.
     * Starts chronometer
     * Contains two threads: one is adding heart rate reading from the pulsometer to the shared readingsBuffer, another displays the average of readings from the readingsBuffer every 1 second
     */
    protected void subscribeToHrEvents() {
        chronometer.start();
        hrPcc.subscribeHeartRateDataEvent((estTimestamp, eventFlags, computedHeartRate, heartBeatCount, heartBeatEventTime, dataState) -> {
            logger.info(String.format("estTimestamp:%1$s eventFlags:%2$s computedHeartRate:%3$s heartBeatCount:%4$s heartBeatEventTime:%5$s dataState:%6$s", estTimestamp, eventFlags, computedHeartRate, heartBeatCount, heartBeatEventTime, dataState));
            readingsBuffer.add(computedHeartRate);
        });
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> displayReading(), 1, 1, TimeUnit.SECONDS);
    }

    /**
     * Stop all the threads and chronometer
     */
    protected void unsubscribeToHrEvents() {
        List<Runnable> list = service.shutdownNow();
        logger.fine("Scheduled events are skiped: " + list.size());
        chronometer.stop();
        hrPcc.subscribeHeartRateDataEvent((estTimestamp, eventFlags, computedHeartRate, heartBeatCount, heartBeatEventTime, dataState) -> {});
    }

    /**
     * Computes the average of heart rate and runs the UI thread, that displays info and adds new record in DB
     */
    private void displayReading() {
        int computedHeartRate;
        int sum = 0;
        //calculate the average of the readings from shared readingBuffer in synchronized way
        synchronized (readingsBuffer) {
            for(Integer i : readingsBuffer) {
                sum += i;
            }
            computedHeartRate = Math.round(sum/readingsBuffer.size());
        }
        // Mark heart rate with asterisk if zero detected
        final String textHeartRate = String.valueOf(computedHeartRate);

        runOnUiThread(() -> {
                addEntry(chronometer.getElapsedTime(), computedHeartRate);
                repository.addNewRecord(computedHeartRate);
                tv_heartRate.setText(textHeartRate);

                //Set vibration
                if(computedHeartRate > pulseLimits.getHighPulseLimit() || computedHeartRate < pulseLimits.getLowPulseLimit()) {
                    // Get instance of Vibrator from current Context
                    Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 75 milliseconds
                    mVibrator.vibrate(75);
                }
        });
    }

    /**
     * Handle the result, connecting to events on success or reporting failure to user.
     */
    protected IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver =
            (result, resultCode, initialDeviceState) -> {
                tv_status.setText(R.string.connection_string);
                switch(resultCode) {
                    case SUCCESS:
                        hrPcc = result;
                        tv_status.setText(String.format("Pulse range: %d-%d", pulseLimits.getLowPulseLimit(), pulseLimits.getHighPulseLimit()));
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

    /**
     * Receives state changes and shows it on the status display line
     */
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

    /**
     * Actions on Destroying the activity
     */
    @Override
    protected void onDestroy() {
        if(!(service == null) ) {
            List<Runnable> list = service.shutdownNow();
            logger.fine("Scheduled events are skipped: " + list.size());
        }
        chronometer.stop();
        repository.closeDb();
        releaseHandle.close();
        super.onDestroy();
    }
}
