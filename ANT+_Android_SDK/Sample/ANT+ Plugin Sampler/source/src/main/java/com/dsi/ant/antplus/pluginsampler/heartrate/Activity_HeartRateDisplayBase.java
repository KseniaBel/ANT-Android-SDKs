/*
This software is subject to the license described in the License.txt file
included with this software distribution. You may not use this file except in compliance
with this license.

Copyright (c) Dynastream Innovations Inc. 2013
All rights reserved.
 */

package com.dsi.ant.antplus.pluginsampler.heartrate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dsi.ant.antplus.pluginsampler.R;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.DataState;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.RrFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;

/**
 * Base class to connects to Heart Rate Plugin and display all the event data.
 */
public abstract class Activity_HeartRateDisplayBase extends Activity
{
    protected abstract void requestAccessToPcc();

    AntPlusHeartRatePcc hrPcc = null;
    protected PccReleaseHandle<AntPlusHeartRatePcc> releaseHandle = null;

    TextView tv_status;

    TextView tv_estTimestamp;

    TextView tv_rssi;

    TextView tv_computedHeartRate;
    TextView tv_heartBeatCounter;
    TextView tv_heartBeatEventTime;

    TextView tv_manufacturerSpecificByte;
    TextView tv_previousHeartBeatEventTime;

    TextView tv_calculatedRrInterval;

    TextView tv_cumulativeOperatingTime;

    TextView tv_manufacturerID;
    TextView tv_serialNumber;

    TextView tv_hardwareVersion;
    TextView tv_softwareVersion;
    TextView tv_modelNumber;

    TextView tv_dataStatus;
    TextView tv_rrFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        handleReset();
    }

    /**
     * Resets the PCC connection to request access again and clears any existing display data.
     */
    protected void handleReset()
    {
        //Release the old access if it exists
        if(releaseHandle != null)
        {
            releaseHandle.close();
        }

        requestAccessToPcc();
    }

    protected void showDataDisplay(String status)
    {
        setContentView(R.layout.activity_heart_rate);

        tv_status = findViewById(R.id.textView_Status);

        tv_estTimestamp = findViewById(R.id.textView_EstTimestamp);

        tv_rssi = findViewById(R.id.textView_Rssi);

        tv_computedHeartRate = findViewById(R.id.textView_ComputedHeartRate);
        tv_heartBeatCounter = findViewById(R.id.textView_HeartBeatCounter);
        tv_heartBeatEventTime = findViewById(R.id.textView_HeartBeatEventTime);

        tv_manufacturerSpecificByte = findViewById(R.id.textView_ManufacturerSpecificByte);
        tv_previousHeartBeatEventTime = findViewById(R.id.textView_PreviousHeartBeatEventTime);

        tv_calculatedRrInterval = findViewById(R.id.textView_CalculatedRrInterval);

        tv_cumulativeOperatingTime = findViewById(R.id.textView_CumulativeOperatingTime);

        tv_manufacturerID = findViewById(R.id.textView_ManufacturerID);
        tv_serialNumber = findViewById(R.id.textView_SerialNumber);

        tv_hardwareVersion = findViewById(R.id.textView_HardwareVersion);
        tv_softwareVersion = findViewById(R.id.textView_SoftwareVersion);
        tv_modelNumber = findViewById(R.id.textView_ModelNumber);

        tv_dataStatus = findViewById(R.id.textView_DataStatus);
        tv_rrFlag = findViewById(R.id.textView_rRFlag);

        //Reset the text display
        tv_status.setText(status);

        tv_estTimestamp.setText("---");

        tv_rssi.setText("---");

        tv_computedHeartRate.setText("---");
        tv_heartBeatCounter.setText("---");
        tv_heartBeatEventTime.setText("---");

        tv_manufacturerSpecificByte.setText("---");
        tv_previousHeartBeatEventTime.setText("---");

        tv_calculatedRrInterval.setText("---");

        tv_cumulativeOperatingTime.setText("---");

        tv_manufacturerID.setText("---");
        tv_serialNumber.setText("---");

        tv_hardwareVersion.setText("---");
        tv_softwareVersion.setText("---");
        tv_modelNumber.setText("---");
        tv_dataStatus.setText("---");
        tv_rrFlag.setText("---");
    }

    /**
     * Switches the active view to the data display and subscribes to all the data events
     */
    public void subscribeToHrEvents()
    {
        hrPcc.subscribeHeartRateDataEvent((estTimestamp, eventFlags, computedHeartRate, heartBeatCount, heartBeatEventTime, dataState) -> {
            // Mark heart rate with asterisk if zero detected
            final String textHeartRate = String.valueOf(computedHeartRate)
                + ((DataState.ZERO_DETECTED.equals(dataState)) ? "*" : "");

            // Mark heart beat count and heart beat event time with asterisk if initial value
            final String textHeartBeatCount = String.valueOf(heartBeatCount)
                + ((DataState.INITIAL_VALUE.equals(dataState)) ? "*" : "");
            final String textHeartBeatEventTime = String.valueOf(heartBeatEventTime)
                + ((DataState.INITIAL_VALUE.equals(dataState)) ? "*" : "");

            runOnUiThread(() -> {
                tv_estTimestamp.setText(String.valueOf(estTimestamp));

                tv_computedHeartRate.setText(textHeartRate);
                tv_heartBeatCounter.setText(textHeartBeatCount);
                tv_heartBeatEventTime.setText(textHeartBeatEventTime);

                tv_dataStatus.setText(dataState.toString());
            });
        });

        hrPcc.subscribePage4AddtDataEvent((estTimestamp, eventFlags, manufacturerSpecificByte, previousHeartBeatEventTime) -> runOnUiThread(() -> {
            tv_estTimestamp.setText(String.valueOf(estTimestamp));

            tv_manufacturerSpecificByte.setText(String.format("0x%02X", manufacturerSpecificByte));
            tv_previousHeartBeatEventTime.setText(String.valueOf(previousHeartBeatEventTime));
        }));

        hrPcc.subscribeCumulativeOperatingTimeEvent((estTimestamp, eventFlags, cumulativeOperatingTime) -> runOnUiThread(() -> {
            tv_estTimestamp.setText(String.valueOf(estTimestamp));

            tv_cumulativeOperatingTime.setText(String.valueOf(cumulativeOperatingTime));
        }));

        hrPcc.subscribeManufacturerAndSerialEvent((estTimestamp, eventFlags, manufacturerID, serialNumber) -> runOnUiThread(() -> {
            tv_estTimestamp.setText(String.valueOf(estTimestamp));

            tv_manufacturerID.setText(String.valueOf(manufacturerID));
            tv_serialNumber.setText(String.valueOf(serialNumber));
        }));

        hrPcc.subscribeVersionAndModelEvent((estTimestamp, eventFlags, hardwareVersion, softwareVersion, modelNumber) -> runOnUiThread(() -> {
            tv_estTimestamp.setText(String.valueOf(estTimestamp));

            tv_hardwareVersion.setText(String.valueOf(hardwareVersion));
            tv_softwareVersion.setText(String.valueOf(softwareVersion));
            tv_modelNumber.setText(String.valueOf(modelNumber));
        }));

        hrPcc.subscribeCalculatedRrIntervalEvent((estTimestamp, eventFlags, rrInterval, flag) -> runOnUiThread(() -> {
            tv_estTimestamp.setText(String.valueOf(estTimestamp));
            tv_rrFlag.setText(flag.toString());

            // Mark RR with asterisk if source is not cached or page 4
            if (flag.equals(RrFlag.DATA_SOURCE_CACHED)
                || flag.equals(RrFlag.DATA_SOURCE_PAGE_4))
                tv_calculatedRrInterval.setText(String.valueOf(rrInterval));
            else
                tv_calculatedRrInterval.setText(String.valueOf(rrInterval) + "*");
        }));

        hrPcc.subscribeRssiEvent((estTimestamp, evtFlags, rssi) -> runOnUiThread(() -> {
            tv_estTimestamp.setText(String.valueOf(estTimestamp));
            tv_rssi.setText(String.valueOf(rssi) + " dBm");
        }));
    }

    protected IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver =
        new IPluginAccessResultReceiver<AntPlusHeartRatePcc>()
        {
        //Handle the result, connecting to events on success or reporting failure to user.
        @Override
        public void onResultReceived(AntPlusHeartRatePcc result, RequestAccessResult resultCode,
            DeviceState initialDeviceState)
        {
            showDataDisplay("Connecting...");
            switch(resultCode)
            {
                case SUCCESS:
                    hrPcc = result;
                    tv_status.setText(result.getDeviceName() + ": " + initialDeviceState);
                    subscribeToHrEvents();
                    if(!result.supportsRssi()) tv_rssi.setText("N/A");
                    break;
                case CHANNEL_NOT_AVAILABLE:
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "Channel Not Available", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                case ADAPTER_NOT_DETECTED:
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "ANT Adapter Not Available. Built-in ANT hardware or external adapter required.", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                case BAD_PARAMS:
                    //Note: Since we compose all the params ourself, we should never see this result
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "Bad request parameters.", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                case OTHER_FAILURE:
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "RequestAccess failed. See logcat for details.", Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                case DEPENDENCY_NOT_INSTALLED:
                    tv_status.setText("Error. Do Menu->Reset.");
                    AlertDialog.Builder adlgBldr = new AlertDialog.Builder(Activity_HeartRateDisplayBase.this);
                    adlgBldr.setTitle("Missing Dependency");
                    adlgBldr.setMessage("The required service\n\"" + AntPlusHeartRatePcc.getMissingDependencyName() + "\"\n was not found. You need to install the ANT+ Plugins service or you may need to update your existing version if you already have it. Do you want to launch the Play Store to get it?");
                    adlgBldr.setCancelable(true);
                    adlgBldr.setPositiveButton("Go to Store", (dialog, which) -> {
                        Intent startStore;
                        startStore = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + AntPlusHeartRatePcc.getMissingDependencyPackageName()));
                        startStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Activity_HeartRateDisplayBase.this.startActivity(startStore);
                    });
                    adlgBldr.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                    final AlertDialog waitDialog = adlgBldr.create();
                    waitDialog.show();
                    break;
                case USER_CANCELLED:
                    tv_status.setText("Cancelled. Do Menu->Reset.");
                    break;
                case UNRECOGNIZED:
                    Toast.makeText(Activity_HeartRateDisplayBase.this,
                        "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                        Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
                default:
                    Toast.makeText(Activity_HeartRateDisplayBase.this, "Unrecognized result: " + resultCode, Toast.LENGTH_SHORT).show();
                    tv_status.setText("Error. Do Menu->Reset.");
                    break;
            }
        }
        };

        //Receives state changes and shows it on the status display line
        protected  IDeviceStateChangeReceiver base_IDeviceStateChangeReceiver =
            new IDeviceStateChangeReceiver()
        {
            @Override
            public void onDeviceStateChange(final DeviceState newDeviceState)
            {
                runOnUiThread(() -> tv_status.setText(hrPcc.getDeviceName() + ": " + newDeviceState));
            }
        };

        @Override
        protected void onDestroy()
        {
            if(releaseHandle != null)
            {
                releaseHandle.close();
            }
            super.onDestroy();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.activity_heart_rate, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch(item.getItemId())
            {
                case R.id.menu_reset:
                    handleReset();
                    tv_status.setText("Resetting...");
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
}
