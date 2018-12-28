/*
This software is subject to the license described in the License.txt file
included with this software distribution. You may not use this file except in compliance
with this license.

Copyright (c) Dynastream Innovations Inc. 2014
All rights reserved.
 */

package com.dsi.ant.antplus.pluginsampler.fitnessequipment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dsi.ant.antplus.pluginsampler.R;
import com.dsi.ant.antplus.pluginsampler.multidevicesearch.Activity_MultiDeviceSearchSampler;
import com.dsi.ant.plugins.antplus.common.FitFileCommon.FitFile;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc.CalculatedTrainerDistanceReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc.CalculatedTrainerSpeedReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc.IFitnessEquipmentStateReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc.Settings;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc.TrainerDataSource;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc.TrainerStatusFlag;
import com.dsi.ant.plugins.antplus.pcc.AntPlusFitnessEquipmentPcc.UserConfiguration;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusCommonPcc.CommonDataPage;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusCommonPcc.IRequestFinishedReceiver;
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.EnumSet;

/**
 * Connects to Fitness Equipment Plugin and display all the event data.
 */
public class Activity_FitnessEquipmentSampler extends Activity
{
    AntPlusFitnessEquipmentPcc fePcc = null;
    PccReleaseHandle<AntPlusFitnessEquipmentPcc> releaseHandle = null;
    Settings settings;
    FitFile[] files;

    TextView tv_status;
    TextView tv_estTimestamp;

    TextView tv_feType;
    TextView tv_state;
    TextView tv_laps;
    TextView tv_cycleLength;
    TextView tv_inclinePercentage;
    TextView tv_resistanceLevel;
    TextView tv_mets;
    TextView tv_caloricBurn;
    TextView tv_calories;
    TextView tv_time;
    TextView tv_distance;
    TextView tv_speed;
    TextView tv_heartRate;
    TextView tv_heartRateSource;
    TextView tv_treadmillCadence;
    TextView tv_treadmillNegVertDistance;
    TextView tv_treadmillPosVertDistance;
    TextView tv_ellipticalPosVertDistance;
    TextView tv_ellipticalStrides;
    TextView tv_ellipticalCadence;
    TextView tv_ellipticalPower;
    TextView tv_bikeCadence;
    TextView tv_bikePower;
    TextView tv_rowerStrokes;
    TextView tv_rowerCadence;
    TextView tv_rowerPower;
    TextView tv_climberStrideCycles;
    TextView tv_climberCadence;
    TextView tv_climberPower;
    TextView tv_skierStrides;
    TextView tv_skierCadence;
    TextView tv_skierPower;

    TextView textView_CalculatedPower;
    TextView textView_CalculatedPowerSource;
    TextView textView_CalculatedSpeed;
    TextView textView_CalculatedSpeedSource;
    TextView textView_CalculatedDistance;
    TextView textView_CalculatedDistanceSource;
    TextView textView_TrainerUpdateEventCount;
    TextView textView_TrainerInstantaneousCadence;
    TextView textView_TrainerInstantaneousPower;
    TextView textView_TrainerAccumulatedPower;
    TextView textView_TrainerTorqueUpdateEventCount;
    TextView textView_AccumulatedWheelTicks;
    TextView textView_AccumulatedWheelPeriod;
    TextView textView_TrainerAccumulatedTorque;
    TextView textView_MaximumResistance;
    TextView textView_BasicResistanceSupport;
    TextView textView_TargetPowerSupport;
    TextView textView_SimulationModeSupport;
    TextView textView_ZeroOffsetCalPending;
    TextView textView_SpinDownCalPending;
    TextView textView_TemperatureCondition;
    TextView textView_SpeedCondition;
    TextView textView_CurrentTemperature;
    TextView textView_TargetSpeed;
    TextView textView_TargetSpinDownTime;
    TextView textView_ZeroOffsetCalSuccess;
    TextView textView_SpinDownCalSuccess;
    TextView textView_Temperature;
    TextView textView_ZeroOffset;
    TextView textView_SpinDownTime;
    TextView textView_LastRxCmdId;
    TextView textView_SequenceNumber;
    TextView textView_CommandStatus;
    TextView textView_RawData;
    TextView textView_TotalResistanceStatus;
    TextView textView_TargetPowerStatus;
    TextView textView_WindResistanceCoefficientStatus;
    TextView textView_WindSpeedStatus;
    TextView textView_DraftingFactorStatus;
    TextView textView_GradeStatus;
    TextView textView_RollingResistanceCoefficientStatus;
    TextView textView_UserWeight;
    TextView textView_BicycleWeight;
    TextView textView_BicycleWheelDiameter;
    TextView textView_GearRatio;
    TextView textView_TotalResistance;
    TextView textView_TargetPower;
    TextView textView_Grade;
    TextView textView_RollingResistanceCoefficient;
    TextView textView_WindResistanceCoefficient;
    TextView textView_WindSpeed;
    TextView textView_DraftingFactor;

    TextView tv_hardwareRevision;
    TextView tv_manufacturerID;
    TextView tv_modelNumber;

    TextView tv_mainSoftwareRevision;
    TextView tv_supplementalSoftwareRevision;
    TextView tv_serialNumber;

    TextView tv_deviceNumber;

    Button button_requestZeroOffsetCalibration;
    Button button_requestSpinDownCalibration;
    Button button_requestCapabilities;
    Button button_setUserConfiguration;
    Button button_requestUserConfiguration;
    Button button_setBasicResistance;
    Button button_setTargetPower;
    Button button_setWindResistance;
    Button button_setTrackResistance;
    Button button_requestCommandStatus;
    Button button_requestBasicResistance;
    Button button_requestTargetPower;
    Button button_requestWindResistance;
    Button button_requestTrackResistance;
    Button button_requestCommonDataPage;

    boolean subscriptionsDone = false;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitnessequipment);

        tv_status = findViewById(R.id.textView_Status);
        tv_deviceNumber = findViewById(R.id.textView_DeviceNumber);

        tv_estTimestamp = findViewById(R.id.textView_EstTimestamp);

        tv_feType = findViewById(R.id.textView_FitnessEquipmentType);
        tv_state = findViewById(R.id.textView_State);
        tv_laps = findViewById(R.id.textView_Laps);
        tv_cycleLength = findViewById(R.id.textView_CycleLength);
        tv_inclinePercentage = findViewById(R.id.textView_InclinePercentage);
        tv_resistanceLevel = findViewById(R.id.textView_ResistanceLevel);
        tv_mets = findViewById(R.id.textView_METS);
        tv_caloricBurn = findViewById(R.id.textView_CaloricBurn);
        tv_calories = findViewById(R.id.textView_Calories);
        tv_time = findViewById(R.id.textView_Time);
        tv_distance = findViewById(R.id.textView_Distance);
        tv_speed = findViewById(R.id.textView_Speed);
        tv_heartRate = findViewById(R.id.textView_HeartRate);
        tv_heartRateSource = findViewById(R.id.textView_HeartRateSource);
        tv_treadmillCadence = findViewById(R.id.textView_TreadmillCadence);
        tv_treadmillNegVertDistance = findViewById(R.id.textView_TreadmillNegVertDistance);
        tv_treadmillPosVertDistance = findViewById(R.id.textView_TreadmillPosVertDistance);
        tv_ellipticalPosVertDistance = findViewById(R.id.textView_EllipticalPosVertDistance);
        tv_ellipticalStrides = findViewById(R.id.textView_EllipticalStrides);
        tv_ellipticalCadence = findViewById(R.id.textView_EllipticalCadence);
        tv_ellipticalPower = findViewById(R.id.textView_EllipticalPower);
        tv_bikeCadence = findViewById(R.id.textView_BikeCadence);
        tv_bikePower = findViewById(R.id.textView_BikePower);
        tv_rowerStrokes = findViewById(R.id.textView_RowerStrokes);
        tv_rowerCadence = findViewById(R.id.textView_RowerCadence);
        tv_rowerPower = findViewById(R.id.textView_RowerPower);
        tv_climberStrideCycles = findViewById(R.id.textView_ClimberStrideCycles);
        tv_climberCadence = findViewById(R.id.textView_ClimberCadence);
        tv_climberPower = findViewById(R.id.textView_ClimberPower);
        tv_skierStrides = findViewById(R.id.textView_SkierStrides);
        tv_skierCadence = findViewById(R.id.textView_SkierCadence);
        tv_skierPower = findViewById(R.id.textView_SkierPower);

        textView_CalculatedPower = findViewById(R.id.textView_CalculatedPower);
        textView_CalculatedPowerSource = findViewById(R.id.textView_CalculatedPowerSource);
        textView_CalculatedSpeed = findViewById(R.id.textView_CalculatedSpeed);
        textView_CalculatedSpeedSource = findViewById(R.id.textView_CalculatedSpeedSource);
        textView_CalculatedDistance = findViewById(R.id.textView_CalculatedDistance);
        textView_CalculatedDistanceSource = findViewById(R.id.textView_CalculatedDistanceSource);
        textView_TrainerUpdateEventCount = findViewById(R.id.textView_TrainerUpdateEventCount);
        textView_TrainerInstantaneousCadence = findViewById(R.id.textView_TrainerInstantaneousCadence);
        textView_TrainerInstantaneousPower = findViewById(R.id.textView_TrainerInstantaneousPower);
        textView_TrainerAccumulatedPower = findViewById(R.id.textView_TrainerAccumulatedPower);
        textView_TrainerTorqueUpdateEventCount = findViewById(R.id.textView_TrainerTorqueUpdateEventCount);
        textView_AccumulatedWheelTicks = findViewById(R.id.textView_AccumulatedWheelTicks);
        textView_AccumulatedWheelPeriod = findViewById(R.id.textView_AccumulatedWheelPeriod);
        textView_TrainerAccumulatedTorque = findViewById(R.id.textView_TrainerAccumulatedTorque);
        textView_MaximumResistance = findViewById(R.id.textView_MaximumResistance);
        textView_BasicResistanceSupport = findViewById(R.id.textView_BasicResistanceSupport);
        textView_TargetPowerSupport = findViewById(R.id.textView_TargetPowerSupport);
        textView_SimulationModeSupport = findViewById(R.id.textView_SimulationModeSupport);
        textView_ZeroOffsetCalPending = findViewById(R.id.textView_ZeroOffsetCalPending);
        textView_SpinDownCalPending = findViewById(R.id.textView_SpinDownCalPending);
        textView_TemperatureCondition = findViewById(R.id.textView_TemperatureCondition);
        textView_SpeedCondition = findViewById(R.id.textView_SpeedCondition);
        textView_CurrentTemperature = findViewById(R.id.textView_CurrentTemperature);
        textView_TargetSpeed = findViewById(R.id.textView_TargetSpeed);
        textView_TargetSpinDownTime = findViewById(R.id.textView_TargetSpinDownTime);
        textView_ZeroOffsetCalSuccess = findViewById(R.id.textView_ZeroOffsetCalSuccess);
        textView_SpinDownCalSuccess = findViewById(R.id.textView_SpinDownCalSuccess);
        textView_Temperature = findViewById(R.id.textView_Temperature);
        textView_ZeroOffset = findViewById(R.id.textView_ZeroOffset);
        textView_SpinDownTime = findViewById(R.id.textView_SpinDownTime);
        textView_LastRxCmdId = findViewById(R.id.textView_LastRxCmdId);
        textView_SequenceNumber = findViewById(R.id.textView_SequenceNumber);
        textView_CommandStatus = findViewById(R.id.textView_CommandStatus);
        textView_RawData = findViewById(R.id.textView_RawData);
        textView_TotalResistanceStatus = findViewById(R.id.textView_TotalResistanceStatus);
        textView_TargetPowerStatus = findViewById(R.id.textView_TargetPowerStatus);
        textView_WindResistanceCoefficientStatus = findViewById(R.id.textView_WindResistanceCoefficientStatus);
        textView_WindSpeedStatus = findViewById(R.id.textView_WindSpeedStatus);
        textView_DraftingFactorStatus = findViewById(R.id.textView_DraftingFactorStatus);
        textView_GradeStatus = findViewById(R.id.textView_GradeStatus);
        textView_RollingResistanceCoefficientStatus = findViewById(R.id.textView_RollingResistanceCoefficientStatus);
        textView_UserWeight = findViewById(R.id.textView_UserWeight);
        textView_BicycleWeight = findViewById(R.id.textView_BicycleWeight);
        textView_BicycleWheelDiameter = findViewById(R.id.textView_BicycleWheelDiameter);
        textView_GearRatio = findViewById(R.id.textView_GearRatio);
        textView_TotalResistance = findViewById(R.id.textView_TotalResistance);
        textView_TargetPower = findViewById(R.id.textView_TargetPower);
        textView_Grade = findViewById(R.id.textView_Grade);
        textView_RollingResistanceCoefficient = findViewById(R.id.textView_RollingResistanceCoefficient);
        textView_WindResistanceCoefficient = findViewById(R.id.textView_WindResistanceCoefficient);
        textView_WindSpeed = findViewById(R.id.textView_WindSpeed);
        textView_DraftingFactor = findViewById(R.id.textView_DraftingFactor);

        tv_hardwareRevision = findViewById(R.id.textView_HardwareRevision);
        tv_manufacturerID = findViewById(R.id.textView_ManufacturerID);
        tv_modelNumber = findViewById(R.id.textView_ModelNumber);

        tv_mainSoftwareRevision = findViewById(R.id.textView_MainSoftwareRevision);
        tv_supplementalSoftwareRevision = findViewById(R.id.textView_SupplementalSoftwareRevision);
        tv_serialNumber = findViewById(R.id.textView_SerialNumber);

        button_requestZeroOffsetCalibration = findViewById(R.id.button_requestZeroOffsetCalibration);
        button_requestSpinDownCalibration = findViewById(R.id.button_requestSpinDownCalibration);
        button_requestCapabilities = findViewById(R.id.button_requestCapabilities);
        button_setUserConfiguration = findViewById(R.id.button_setUserConfiguration);
        button_requestUserConfiguration = findViewById(R.id.button_requestUserConfiguration);
        button_setBasicResistance = findViewById(R.id.button_setBasicResistance);
        button_setTargetPower = findViewById(R.id.button_setTargetPower);
        button_setWindResistance = findViewById(R.id.button_setWindResistance);
        button_setTrackResistance = findViewById(R.id.button_setTrackResistance);
        button_requestCommandStatus = findViewById(R.id.button_requestCommandStatus);
        button_requestBasicResistance = findViewById(R.id.button_requestBasicResistance);
        button_requestTargetPower = findViewById(R.id.button_requestTargetPower);
        button_requestWindResistance = findViewById(R.id.button_requestWindResistance);
        button_requestTrackResistance = findViewById(R.id.button_requestTrackResistance);
        button_requestCommonDataPage = findViewById(R.id.button_requestCommonDataPage);

        b = getIntent().getExtras();
        if(b != null)
        {
            String name = b.getString(Dialog_ConfigSettings.SETTINGS_NAME);
            Settings.Gender gender = Settings.Gender.FEMALE;
            if(b.getBoolean(Dialog_ConfigSettings.SETTINGS_GENDER))
                gender = Settings.Gender.MALE;
            short age = b.getShort(Dialog_ConfigSettings.SETTINGS_AGE);
            float height = b.getFloat(Dialog_ConfigSettings.SETTINGS_HEIGHT);
            float weight = b.getFloat(Dialog_ConfigSettings.SETTINGS_WEIGHT);

            settings = new Settings(name, gender, age, height, weight);

            if(b.getBoolean(Dialog_ConfigSettings.INCLUDE_WORKOUT))
            {
                try
                {
                    // Make available a FIT workout file to the fitness equipment
                    // The sample file included with this project was obtained from the FIT SDK, v7.10
                    InputStream is = getAssets().open("WorkoutRepeatSteps.fit");
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int next;
                    while((next = is.read()) != -1)
                        bos.write(next);
                    bos.flush();
                    is.close();
                    FitFile workoutFile = new FitFile(bos.toByteArray());
                    workoutFile.setFileType((short) 5);  // Make sure to set the File Type, so this information is also available to the fitness equipment
                    // Refer to the FIT SDK for more details on FIT file types
                    files = new FitFile[] { workoutFile};
                }
                catch (IOException e)
                {
                    files = null;
                }
            }
        }

        final IRequestFinishedReceiver requestFinishedReceiver = requestStatus -> runOnUiThread(
                () -> {
                    switch(requestStatus)
                    {
                        case SUCCESS:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Successfully Sent", Toast.LENGTH_SHORT).show();
                            break;
                        case FAIL_PLUGINS_SERVICE_VERSION:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this,
                                "Plugin Service Upgrade Required?",
                                Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Failed to be Sent", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });

        button_requestZeroOffsetCalibration.setOnClickListener(v -> {
            //TODO The fitness equipment may request calibration from the user, which is when this command would be sent.
            boolean submitted = fePcc.requestZeroOffsetCalibration(requestFinishedReceiver, null, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestSpinDownCalibration.setOnClickListener(v -> {
            //TODO The fitness equipment may request calibration from the user, which is when this command would be sent.
            boolean submitted = fePcc.requestSpinDownCalibration(requestFinishedReceiver, null, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestCapabilities.setOnClickListener(v -> {
            //TODO The capabilities should be requested before attempting to send new control settings to determine which modes are supported.
            boolean submitted = fePcc.requestCapabilities(requestFinishedReceiver, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_setUserConfiguration.setOnClickListener(v -> {
            UserConfiguration config = new UserConfiguration();
            config.bicycleWeight = new BigDecimal("10.00");         //10kg bike weight
            config.gearRatio = new BigDecimal("0.03");              //0.03 gear ratio
            config.bicycleWheelDiameter = new BigDecimal("0.70");   //0.70m wheel diameter
            config.userWeight = new BigDecimal("75.00");            //75kg user

            boolean submitted = fePcc.requestSetUserConfiguration(config, requestFinishedReceiver);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestUserConfiguration.setOnClickListener(v -> {
            boolean submitted = fePcc.requestUserConfiguration(requestFinishedReceiver, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_setBasicResistance.setOnClickListener(v -> {
            //TODO The capabilities should be requested before attempting to send new control settings to determine which modes are supported.
            boolean submitted = fePcc.getTrainerMethods().requestSetBasicResistance(new BigDecimal("4.5"), requestFinishedReceiver);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_setTargetPower.setOnClickListener(new View.OnClickListener()
        {
            BigDecimal targetPower = new BigDecimal("42.25");   //42.25%

            @Override
            public void onClick(View v)
            {
                //TODO The capabilities should be requested before attempting to send new control settings to determine which modes are supported.
                boolean submitted = fePcc.getTrainerMethods().requestSetTargetPower(targetPower, requestFinishedReceiver);

                if(!submitted)
                    Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
            }
        });

        button_setWindResistance.setOnClickListener(v -> {
            //TODO The capabilities should be requested before attempting to send new control settings to determine which modes are supported.
            //null indicates default values
            boolean submitted = fePcc.getTrainerMethods().requestSetWindResistance(null, null, null, null, null, requestFinishedReceiver);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_setTrackResistance.setOnClickListener(v -> {
            //TODO The capabilities should be requested before attempting to send new control settings to determine which modes are supported.
            //null indicates default values
            boolean submitted = fePcc.getTrainerMethods().requestSetTrackResistance(null, null, requestFinishedReceiver);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestCommandStatus.setOnClickListener(v -> {
            //TODO This can be requested after a command is sent to determine it's status on the trainer.
            boolean submitted = fePcc.getTrainerMethods().requestCommandStatus(requestFinishedReceiver, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestBasicResistance.setOnClickListener(v -> {
            boolean submitted = fePcc.getTrainerMethods().requestBasicResistance(requestFinishedReceiver, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestTargetPower.setOnClickListener(v -> {
            boolean submitted = fePcc.getTrainerMethods().requestTargetPower(requestFinishedReceiver, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestWindResistance.setOnClickListener(v -> {
            boolean submitted = fePcc.getTrainerMethods().requestWindResistance(requestFinishedReceiver, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestTrackResistance.setOnClickListener(v -> {
            boolean submitted = fePcc.getTrainerMethods().requestTrackResistance(requestFinishedReceiver, null);

            if(!submitted)
                Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
        });

        button_requestCommonDataPage.setOnClickListener(v -> {
            // Create a String array of names from the enum
            final CommonDataPage[] commonDataPages = CommonDataPage.values();
            String[] names = new String[commonDataPages.length];

            for(int i = 0; i < names.length; i++)
            {
                names[i] = commonDataPages[i].name();
            }

            // Build the list alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_FitnessEquipmentSampler.this);
            builder.setTitle("Pick a common data page")
            .setItems(names, (dialog, which) -> {
                CommonDataPage selectedCommonDataPage = commonDataPages[which];
                boolean submitted = fePcc.requestCommonDataPage(selectedCommonDataPage, requestFinishedReceiver);

                if(!submitted)
                    Toast.makeText(Activity_FitnessEquipmentSampler.this, "Request Could not be Made", Toast.LENGTH_SHORT).show();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        resetPcc();
    }

    /**
     * Resets the PCC connection to request access again and clears any existing display data.
     */
    private void resetPcc()
    {
        //Release the old access if it exists
        if(releaseHandle != null)
            releaseHandle.close();

        //Reset event subscriptions
        subscriptionsDone = false;

        //Reset the text display
        tv_status.setText("Connecting...");

        tv_estTimestamp.setText("---");

        tv_feType.setText("---");
        tv_state.setText("---");
        tv_laps.setText("---");
        tv_cycleLength.setText("---");
        tv_inclinePercentage.setText("---");
        tv_resistanceLevel.setText("---");
        tv_mets.setText("---");
        tv_caloricBurn.setText("---");
        tv_calories.setText("---");
        tv_time.setText("---");
        tv_distance.setText("---");
        tv_speed.setText("---");
        tv_heartRate.setText("---");
        tv_heartRateSource.setText("---");
        tv_treadmillCadence.setText("---");
        tv_treadmillNegVertDistance.setText("---");
        tv_treadmillPosVertDistance.setText("---");
        tv_ellipticalStrides.setText("---");
        tv_ellipticalCadence.setText("---");
        tv_ellipticalPower.setText("---");
        tv_bikeCadence.setText("---");
        tv_bikePower.setText("---");
        tv_rowerStrokes.setText("---");
        tv_rowerCadence.setText("---");
        tv_rowerPower.setText("---");
        tv_climberStrideCycles.setText("---");
        tv_climberPower.setText("---");
        tv_skierStrides.setText("---");
        tv_skierCadence.setText("---");
        tv_skierPower.setText("---");

        tv_hardwareRevision.setText("---");
        tv_manufacturerID.setText("---");
        tv_modelNumber.setText("---");

        tv_mainSoftwareRevision.setText("---");
        tv_supplementalSoftwareRevision.setText("");
        tv_serialNumber.setText("---");

        final IPluginAccessResultReceiver<AntPlusFitnessEquipmentPcc> mPluginAccessResultReceiver =
                new IPluginAccessResultReceiver<AntPlusFitnessEquipmentPcc>()
                {
                //Handle the result, connecting to events on success or reporting failure to user.
                @Override
                public void onResultReceived(AntPlusFitnessEquipmentPcc result,
                    RequestAccessResult resultCode, DeviceState initialDeviceState)
                {
                    switch(resultCode)
                    {
                        case SUCCESS:
                            fePcc = result;
                            tv_deviceNumber.setText(String.valueOf(fePcc.getAntDeviceNumber()));    //Get device ID
                            if(initialDeviceState == DeviceState.CLOSED)
                                tv_status.setText(fePcc.getDeviceName() + ": " + "Waiting for FE Session Request");
                            else
                                tv_status.setText(result.getDeviceName() + ": " + initialDeviceState);
                            subscribeToEvents();
                            break;
                        case CHANNEL_NOT_AVAILABLE:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this, "Channel Not Available", Toast.LENGTH_SHORT).show();
                            tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case ADAPTER_NOT_DETECTED:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this, "ANT Adapter Not Available. Built-in ANT hardware or external adapter required.", Toast.LENGTH_SHORT).show();
                            tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case BAD_PARAMS:
                            //Note: Since we compose all the params ourself, we should never see this result
                            Toast.makeText(Activity_FitnessEquipmentSampler.this, "Bad request parameters.", Toast.LENGTH_SHORT).show();
                            tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case OTHER_FAILURE:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this, "RequestAccess failed. See logcat for details.", Toast.LENGTH_SHORT).show();
                            tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case DEPENDENCY_NOT_INSTALLED:
                            tv_status.setText("Error. Do Menu->Reset.");
                            AlertDialog.Builder adlgBldr = new AlertDialog.Builder(Activity_FitnessEquipmentSampler.this);
                            adlgBldr.setTitle("Missing Dependency");
                            adlgBldr.setMessage("The required service\n\"" + AntPlusFitnessEquipmentPcc.getMissingDependencyName() + "\"\n was not found. You need to install the ANT+ Plugins service or you may need to update your existing version if you already have it. Do you want to launch the Play Store to get it?");
                            adlgBldr.setCancelable(true);
                            adlgBldr.setPositiveButton("Go to Store", (dialog, which) -> {
                                Intent startStore = null;
                                startStore = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + AntPlusFitnessEquipmentPcc.getMissingDependencyPackageName()));
                                startStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                Activity_FitnessEquipmentSampler.this.startActivity(startStore);
                            });
                            adlgBldr.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                            final AlertDialog waitDialog = adlgBldr.create();
                            waitDialog.show();
                            break;
                        case USER_CANCELLED:
                            tv_status.setText("Cancelled. Do Menu->Reset.");
                            break;
                        case UNRECOGNIZED:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this,
                                "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                                Toast.LENGTH_SHORT).show();
                            tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        default:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this, "Unrecognized result: " + resultCode, Toast.LENGTH_SHORT).show();
                            tv_status.setText("Error. Do Menu->Reset.");
                            break;
                    }
                }

                /**
                 * Subscribe to all the data events, connecting them to display their data.
                 */
                private void subscribeToEvents()
                {
                    fePcc.subscribeGeneralFitnessEquipmentDataEvent((estTimestamp, eventFlags, elapsedTime, cumulativeDistance, instantaneousSpeed, virtualInstantaneousSpeed, instantaneousHeartRate, heartRateDataSource) -> runOnUiThread(() -> {
                        tv_estTimestamp.setText(String.valueOf(estTimestamp));
                        if(elapsedTime.intValue() == -1)
                            tv_time.setText("Invalid");
                        else
                            tv_time.setText(String.valueOf(elapsedTime) + "s");

                        if(cumulativeDistance == -1)
                            tv_distance.setText("Invalid");
                        else
                            tv_distance.setText(String.valueOf(cumulativeDistance) + "m");

                        if(instantaneousSpeed.intValue() == -1)
                            tv_speed.setText("Invalid");
                        else
                            tv_speed.setText(String.valueOf(instantaneousSpeed) + "m/s");

                        if(virtualInstantaneousSpeed)
                            tv_speed.setText(tv_speed.getText() + " (Virtual)");

                        if(instantaneousHeartRate == -1)
                            tv_heartRate.setText("Invalid");
                        else
                            tv_heartRate.setText(String.valueOf(instantaneousHeartRate) + "bpm");

                        switch(heartRateDataSource)
                        {
                            case ANTPLUS_HRM:
                            case EM_5KHz:
                            case HAND_CONTACT_SENSOR:
                            case UNKNOWN:
                                tv_heartRateSource.setText(heartRateDataSource.toString());
                                break;
                            case UNRECOGNIZED:
                                Toast.makeText(Activity_FitnessEquipmentSampler.this,
                                    "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                                    Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }));

                    fePcc.subscribeLapOccuredEvent((estTimestamp, eventFlags, lapCount) -> runOnUiThread(() -> {
                        tv_estTimestamp.setText(String.valueOf(estTimestamp));

                        tv_laps.setText(String.valueOf(lapCount));
                    }));

                    fePcc.subscribeGeneralSettingsEvent((estTimestamp, eventFlags, cycleLength, inclinePercentage, resistanceLevel) -> runOnUiThread(() -> {
                        tv_estTimestamp.setText(String.valueOf(estTimestamp));

                        if(cycleLength.intValue() == -1)
                            tv_cycleLength.setText("Invalid");
                        else
                            tv_cycleLength.setText(String.valueOf(cycleLength) + "m");

                        if(inclinePercentage.intValue() == 0x7FFF)
                            tv_inclinePercentage.setText("Invalid");
                        else
                            tv_inclinePercentage.setText(String.valueOf(inclinePercentage) + "%");

                        if(resistanceLevel == -1)
                            tv_resistanceLevel.setText("Invalid");
                        else
                            //TODO If this is a Fitness Equipment Controls device, this represents the current set resistance level at 0.5% per unit from 0% to 100%
                            tv_resistanceLevel.setText(String.valueOf(resistanceLevel));
                    }));

                    fePcc.subscribeGeneralMetabolicDataEvent((estTimestamp, eventFlags, instantaneousMetabolicEquivalents, instantaneousCaloricBurn, cumulativeCalories) -> runOnUiThread(() -> {
                        tv_estTimestamp.setText(String.valueOf(estTimestamp));

                        if(instantaneousMetabolicEquivalents.intValue() == -1)
                            tv_mets.setText("Invalid");
                        else
                            tv_mets.setText(String.valueOf(instantaneousMetabolicEquivalents) + "METs");

                        if(instantaneousCaloricBurn.intValue() == -1)
                            tv_caloricBurn.setText("Invalid");
                        else
                            tv_caloricBurn.setText(String.valueOf(instantaneousCaloricBurn) + "kcal/h");

                        if(cumulativeCalories == -1)
                            tv_calories.setText("Invalid");
                        else
                            tv_calories.setText(String.valueOf(cumulativeCalories) + "kcal");
                    }));

                    fePcc.subscribeManufacturerIdentificationEvent((estTimestamp, eventFlags, hardwareRevision, manufacturerID, modelNumber) -> runOnUiThread(() -> {
                        tv_estTimestamp.setText(String.valueOf(estTimestamp));

                        tv_hardwareRevision.setText(String.valueOf(hardwareRevision));
                        tv_manufacturerID.setText(String.valueOf(manufacturerID));
                        tv_modelNumber.setText(String.valueOf(modelNumber));
                    }));

                    fePcc.subscribeProductInformationEvent((estTimestamp, eventFlags, mainSoftwareRevision, supplementalSoftwareRevision, serialNumber) -> runOnUiThread(() -> {
                        tv_estTimestamp.setText(String.valueOf(estTimestamp));

                        tv_mainSoftwareRevision.setText(String
                            .valueOf(mainSoftwareRevision));

                        if (supplementalSoftwareRevision == -2)
                            // Plugin Service installed does not support supplemental revision
                            tv_supplementalSoftwareRevision.setText("?");
                        else if (supplementalSoftwareRevision == 0xFF)
                            // Invalid supplemental revision
                            tv_supplementalSoftwareRevision.setText("");
                        else
                            // Valid supplemental revision
                            tv_supplementalSoftwareRevision.setText(", " + String
                                .valueOf(supplementalSoftwareRevision));

                        tv_serialNumber.setText(String.valueOf(serialNumber));
                    }));
                }
                };

        IDeviceStateChangeReceiver mDeviceStateChangeReceiver =
                //Receives state changes and shows it on the status display line
                newDeviceState -> runOnUiThread(() -> {
                    //Note: The state here is the state of our data receiver channel which is closed until the ANTFS session is established
                    if(newDeviceState == DeviceState.CLOSED)
                    {
                        tv_status.setText(fePcc.getDeviceName() + ": " + "Waiting for FE Session Request");
                    }
                    else
                    {
                        tv_status.setText(fePcc.getDeviceName() + ": " + newDeviceState);
                    }
                });

        IFitnessEquipmentStateReceiver mFitnessEquipmentStateReceiver =
                (estTimestamp, eventFlags, equipmentType, equipmentState) -> runOnUiThread(() -> {
                    tv_estTimestamp.setText(String.valueOf(estTimestamp));


                    switch(equipmentType)
                    {
                        case GENERAL:
                            tv_feType.setText("GENERAL");
                            break;
                        case TREADMILL:
                            tv_feType.setText("TREADMILL");

                            if(subscriptionsDone)
                                break;

                            fePcc.getTreadmillMethods().subscribeTreadmillDataEvent((estTimestamp1, eventFlags1, instantaneousCadence, cumulativeNegVertDistance, cumulativePosVertDistance) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(instantaneousCadence == -1)
                                    tv_treadmillCadence.setText("Invalid");
                                else
                                    tv_treadmillCadence.setText(String.valueOf(instantaneousCadence) + "strides/min");
                                if(cumulativeNegVertDistance.intValue() == 1)
                                    tv_treadmillNegVertDistance.setText("Invalid");
                                else
                                    tv_treadmillNegVertDistance.setText(String.valueOf(cumulativeNegVertDistance) + "m");
                                if(cumulativePosVertDistance.intValue() == -1)
                                    tv_treadmillPosVertDistance.setText("Invalid");
                                else
                                    tv_treadmillPosVertDistance.setText(String.valueOf(cumulativePosVertDistance) + "m");
                            }));
                            subscriptionsDone = true;
                            break;
                        case ELLIPTICAL:
                            tv_feType.setText("ELLIPTICAL");

                            if(subscriptionsDone)
                                break;

                            fePcc.getEllipticalMethods().subscribeEllipticalDataEvent((estTimestamp1, eventFlags1, cumulativePosVertDistance, cumulativeStrides, instantaneousCadence, instantaneousPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(instantaneousCadence == -1)
                                    tv_ellipticalCadence.setText("Invalid");
                                else
                                    tv_ellipticalCadence.setText(String.valueOf(instantaneousCadence) + "strides/min");
                                if(cumulativePosVertDistance.intValue() == -1)
                                    tv_ellipticalPosVertDistance.setText("Invalid");
                                else
                                    tv_ellipticalPosVertDistance.setText(String.valueOf(cumulativePosVertDistance) + "m");
                                if(cumulativeStrides == -1)
                                    tv_ellipticalStrides.setText("Invalid");
                                else
                                    tv_ellipticalStrides.setText(String.valueOf(cumulativeStrides));
                                if(instantaneousPower == -1)
                                    tv_ellipticalPower.setText("Invalid");
                                else
                                    tv_ellipticalPower.setText(String.valueOf(instantaneousPower) + "W");
                            }));
                            subscriptionsDone = true;
                            break;
                        case BIKE:
                            tv_feType.setText("BIKE");

                            if(subscriptionsDone)
                                break;

                            fePcc.getBikeMethods().subscribeBikeDataEvent((estTimestamp1, eventFlags1, instantaneousCadence, instantaneousPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(instantaneousCadence == -1)
                                    tv_bikeCadence.setText("Invalid");
                                else
                                    tv_bikeCadence.setText(String.valueOf(instantaneousCadence) + "rpm");
                                if(instantaneousPower == -1)
                                    tv_bikePower.setText("Invalid");
                                else
                                    tv_bikePower.setText(String.valueOf(instantaneousPower) + "W");
                            }));
                            subscriptionsDone = true;
                            break;
                        case ROWER:
                            tv_feType.setText("ROWER");

                            if(subscriptionsDone)
                                break;

                            fePcc.getRowerMethods().subscribeRowerDataEvent((estTimestamp1, eventFlags1, cumulativeStrokes, instantaneousCadence, instantaneousPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(cumulativeStrokes == -1)
                                    tv_rowerStrokes.setText("Invalid");
                                else
                                    tv_rowerStrokes.setText(String.valueOf(cumulativeStrokes));
                                if(instantaneousCadence == -1)
                                    tv_rowerCadence.setText("Invalid");
                                else
                                    tv_rowerCadence.setText(String.valueOf(instantaneousCadence) + "strokes/min");
                                if(instantaneousPower == -1)
                                    tv_rowerPower.setText("Invalid");
                                else
                                    tv_rowerPower.setText(String.valueOf(instantaneousPower) + "W");
                            }));
                            subscriptionsDone = true;
                            break;
                        case CLIMBER:
                            tv_feType.setText("CLIMBER");

                            if(subscriptionsDone)
                                break;

                            fePcc.getClimberMethods().subscribeClimberDataEvent((estTimestamp1, eventFlags1, cumulativeStrideCycles, instantaneousCadence, instantaneousPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(cumulativeStrideCycles == -1)
                                    tv_climberStrideCycles.setText("Invalid");
                                else
                                    tv_climberStrideCycles.setText(String.valueOf(cumulativeStrideCycles));
                                if(instantaneousCadence == -1)
                                    tv_climberCadence.setText("Invalid");
                                else
                                    tv_climberCadence.setText(String.valueOf(instantaneousCadence) + "strides/min");
                                if(instantaneousPower == -1)
                                    tv_climberPower.setText("Invalid");
                                else
                                    tv_climberPower.setText(String.valueOf(instantaneousPower) + "W");
                            }));
                            subscriptionsDone = true;
                            break;
                        case NORDICSKIER:
                            tv_feType.setText("NORDIC SKIER");

                            if(subscriptionsDone)
                                break;

                            fePcc.getNordicSkierMethods().subscribeNordicSkierDataEvent((estTimestamp1, eventFlags1, cumulativeStrides, instantaneousCadence, instantaneousPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(cumulativeStrides == -1)
                                    tv_skierStrides.setText("Invalid");
                                else
                                    tv_skierStrides.setText(String.valueOf(cumulativeStrides));
                                if(instantaneousCadence == -1)
                                    tv_skierCadence.setText("Invalid");
                                else
                                    tv_skierCadence.setText(String.valueOf(instantaneousCadence) + "strides/min");
                                if(instantaneousPower == -1)
                                    tv_skierPower.setText("Invalid");
                                else
                                    tv_skierPower.setText(String.valueOf(instantaneousPower) + "W");
                            }));
                            subscriptionsDone = true;
                            break;
                        case TRAINER:
                            tv_feType.setText("TRAINER");

                            if(subscriptionsDone)
                                break;

                            fePcc.getTrainerMethods().subscribeCalculatedTrainerPowerEvent((estTimestamp1, eventFlags1, dataSource, calculatedPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_CalculatedPower.setText(String.valueOf(calculatedPower) + "W");
                                String source;

                                //NOTE: The calculated power event will send an initial value code if it needed to calculate a NEW average.
                                //This is important if using the calculated power event to record user data, as an initial value indicates an average could not be guaranteed.
                                //The event prioritizes calculating with torque data over power only data.
                                switch(dataSource)
                                {
                                    case COAST_OR_STOP_DETECTED:
                                        //A coast or stop condition detected by the ANT+ Plugin.
                                        //This is automatically sent by the plugin after 3 seconds of unchanging events.
                                        //NOTE: This value should be ignored by apps which are archiving the data for accuracy.
                                    case INITIAL_VALUE_TRAINER_DATA:
                                        //New data calculated from initial value data source
                                    case TRAINER_DATA:
                                    case INITIAL_VALUE_TRAINER_TORQUE_DATA:
                                        //New data calculated from initial value data source
                                    case TRAINER_TORQUE_DATA:
                                        source = dataSource.toString();
                                        break;
                                    case UNRECOGNIZED:
                                        Toast.makeText(
                                            Activity_FitnessEquipmentSampler.this,
                                            "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                                            Toast.LENGTH_SHORT).show();
                                    default:
                                        source = "N/A";
                                        break;
                                }

                                textView_CalculatedPowerSource.setText(source);
                            }));
                            fePcc.getTrainerMethods().subscribeCalculatedTrainerSpeedEvent(new CalculatedTrainerSpeedReceiver(new BigDecimal("0.70"))   //0.70m wheel diameter
                            {
                                @Override
                                public void onNewCalculatedTrainerSpeed(final long estTimestamp1, EnumSet<EventFlag> eventFlags1,
                                                                        final TrainerDataSource dataSource, final BigDecimal calculatedSpeed)
                                {
                                    runOnUiThread(() -> {
                                        tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                        textView_CalculatedSpeed.setText(String.valueOf(calculatedSpeed) + "km/h");
                                        String source;

                                        //NOTE: The calculated speed event will send an initial value code if it needed to calculate a NEW average.
                                        //This is important if using the calculated speed event to record user data, as an initial value indicates an average could not be guaranteed.
                                        switch(dataSource)
                                        {
                                            case COAST_OR_STOP_DETECTED:
                                                //A coast or stop condition detected by the ANT+ Plugin.
                                                //This is automatically sent by the plugin after 3 seconds of unchanging events.
                                                //NOTE: This value should be ignored by apps which are archiving the data for accuracy.
                                            case INITIAL_VALUE_TRAINER_TORQUE_DATA:
                                                //New data calculated from initial value data source
                                            case TRAINER_TORQUE_DATA:
                                                source = dataSource.toString();
                                                break;
                                            case UNRECOGNIZED:
                                                Toast.makeText(
                                                    Activity_FitnessEquipmentSampler.this,
                                                    "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                                                    Toast.LENGTH_SHORT).show();
                                            default:
                                                source = "N/A";
                                                break;
                                        }

                                        textView_CalculatedSpeedSource.setText(source);
                                    });
                                }
                            });
                            fePcc.getTrainerMethods().subscribeCalculatedTrainerDistanceEvent(new CalculatedTrainerDistanceReceiver(new BigDecimal("0.70")) //0.70m wheel diameter
                            {

                                @Override
                                public void onNewCalculatedTrainerDistance(final long estTimestamp1, EnumSet<EventFlag> eventFlags1,
                                                                           final TrainerDataSource dataSource, final BigDecimal calculatedDistance)
                                {
                                    runOnUiThread(() -> {
                                        tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                        textView_CalculatedDistance.setText(String.valueOf(calculatedDistance) + "m");
                                        String source;

                                        //NOTE: The calculated speed event will send an initial value code if it needed to calculate a NEW average.
                                        //This is important if using the calculated speed event to record user data, as an initial value indicates an average could not be guaranteed.
                                        switch(dataSource)
                                        {
                                            case COAST_OR_STOP_DETECTED:
                                                //A coast or stop condition detected by the ANT+ Plugin.
                                                //This is automatically sent by the plugin after 3 seconds of unchanging events.
                                                //NOTE: This value should be ignored by apps which are archiving the data for accuracy.
                                            case INITIAL_VALUE_TRAINER_TORQUE_DATA:
                                                //New data calculated from initial value data source
                                            case TRAINER_TORQUE_DATA:
                                                source = dataSource.toString();
                                                break;
                                            case UNRECOGNIZED:
                                                Toast.makeText(
                                                Activity_FitnessEquipmentSampler.this,
                                                "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                                                Toast.LENGTH_SHORT).show();
                                            default:
                                                source = "N/A";
                                                break;
                                        }

                                        textView_CalculatedDistanceSource.setText(source);
                                    });
                                }
                            });
                            fePcc.getTrainerMethods().subscribeRawTrainerDataEvent((estTimestamp1, eventFlags1, updateEventCount, instantaneousCadence, instantaneousPower, accumulatedPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                //NOTE: If the update event count has not incremented then the data on this page has not changed.
                                //Please refer to the ANT+ Fitness Equipment Device Profile for more information.
                                textView_TrainerUpdateEventCount.setText(String.valueOf(updateEventCount));

                                if(instantaneousCadence != -1)
                                    textView_TrainerInstantaneousCadence.setText(String.valueOf(instantaneousCadence) + "RPM");
                                else
                                    textView_TrainerInstantaneousCadence.setText("N/A");

                                if(instantaneousPower != -1)
                                    textView_TrainerInstantaneousPower.setText(String.valueOf(instantaneousPower) + "W");
                                else
                                    textView_TrainerInstantaneousPower.setText("N/A");

                                if(accumulatedPower != -1)
                                    textView_TrainerAccumulatedPower.setText(String.valueOf(accumulatedPower) + "W");
                                else
                                    textView_TrainerAccumulatedPower.setText("N/A");
                            }));
                            fePcc.getTrainerMethods().subscribeRawTrainerTorqueDataEvent((estTimestamp1, eventFlags1, updateEventCount, accumulatedWheelTicks, accumulatedWheelPeriod, accumulatedTorque) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                textView_TrainerTorqueUpdateEventCount.setText(String.valueOf(updateEventCount));
                                textView_AccumulatedWheelTicks.setText(String.valueOf(accumulatedWheelTicks) + "rotations");
                                textView_AccumulatedWheelPeriod.setText(String.valueOf(accumulatedWheelPeriod) + "s");
                                textView_TrainerAccumulatedTorque.setText(String.valueOf(accumulatedTorque) + "Nm");
                            }));
                            fePcc.subscribeCapabilitiesEvent((estTimestamp1, eventFlags1, capabilities) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(capabilities.maximumResistance != null)
                                    textView_MaximumResistance.setText(String.valueOf(capabilities.maximumResistance) + "N");
                                else
                                    textView_MaximumResistance.setText("N/A");

                                textView_BasicResistanceSupport.setText(capabilities.basicResistanceModeSupport ? "True" : "False");
                                textView_TargetPowerSupport.setText(capabilities.targetPowerModeSupport ? "True" : "False");
                                textView_SimulationModeSupport.setText(capabilities.simulationModeSupport ? "True" : "False");
                            }));
                            fePcc.getTrainerMethods().subscribeTrainerStatusEvent((estTimestamp1, eventFlags1, trainerStatusFlags) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                for(TrainerStatusFlag flag : trainerStatusFlags)
                                {
                                    switch(flag)
                                    {
                                        case BICYCLE_POWER_CALIBRATION_REQUIRED:
                                            break;
                                        case MAXIMUM_POWER_LIMIT_REACHED:
                                            break;
                                        case MINIMUM_POWER_LIMIT_REACHED:
                                            break;
                                        case RESISTANCE_CALIBRATION_REQUIRED:
                                            break;
                                        case UNRECOGNIZED_FLAG_PRESENT:
                                            break;
                                        case USER_CONFIGURATION_REQUIRED:
                                            break;
                                    }
                                }
                            }));
                            fePcc.subscribeCalibrationInProgressEvent((estTimestamp1, eventFlags1, calibrationInProgress) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_ZeroOffsetCalPending.setText(calibrationInProgress.zeroOffsetCalibrationPending ? "Pending" : "Not Requested");
                                textView_SpinDownCalPending.setText(calibrationInProgress.spinDownCalibrationPending ? "Pending" : "Not Requested");

                                switch(calibrationInProgress.temperatureCondition)
                                {
                                    case CURRENT_TEMPERATURE_OK:
                                    case CURRENT_TEMPERATURE_TOO_HIGH:
                                    case CURRENT_TEMPERATURE_TOO_LOW:
                                    case NOT_APPLICABLE:
                                    case UNRECOGNIZED:
                                    default:
                                        textView_TemperatureCondition.setText(calibrationInProgress.temperatureCondition.toString());
                                        break;

                                }

                                switch(calibrationInProgress.speedCondition)
                                {
                                    case CURRENT_SPEED_OK:
                                    case CURRENT_SPEED_TOO_LOW:
                                    case NOT_APPLICABLE:
                                    case UNRECOGNIZED:
                                    default:
                                        textView_SpeedCondition.setText(calibrationInProgress.speedCondition.toString());
                                        break;

                                }

                                if(calibrationInProgress.currentTemperature != null)
                                    textView_CurrentTemperature.setText(calibrationInProgress.currentTemperature.toString() + "C");
                                else
                                    textView_CurrentTemperature.setText("N/A");

                                if(calibrationInProgress.targetSpeed != null)
                                    textView_TargetSpeed.setText(calibrationInProgress.targetSpeed.toString() + "m/s");
                                else
                                    textView_TargetSpeed.setText("N/A");

                                if(calibrationInProgress.targetSpinDownTime != null)
                                    textView_TargetSpinDownTime.setText(calibrationInProgress.targetSpinDownTime.toString() + "ms");
                                else
                                    textView_TargetSpinDownTime.setText("N/A");
                            }));
                            fePcc.subscribeCalibrationResponseEvent((estTimestamp1, eventFlags1, calibrationResponse) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_ZeroOffsetCalSuccess.setText(calibrationResponse.zeroOffsetCalibrationSuccess ? "Success" : "N/A");
                                textView_SpinDownCalSuccess.setText(calibrationResponse.spinDownCalibrationSuccess ? "Success" : "N/A");

                                if(calibrationResponse.temperature != null)
                                    textView_Temperature.setText(calibrationResponse.temperature.toString() + "C");
                                else
                                    textView_Temperature.setText("N/A");

                                if(calibrationResponse.zeroOffset != null)
                                    textView_ZeroOffset.setText(calibrationResponse.zeroOffset.toString());
                                else
                                    textView_ZeroOffset.setText("N/A");

                                if(calibrationResponse.spinDownTime != null)
                                    textView_SpinDownTime.setText(calibrationResponse.spinDownTime.toString() + "ms");
                                else
                                    textView_SpinDownTime.setText("N/A");

                            }));
                            fePcc.getTrainerMethods().subscribeCommandStatusEvent((estTimestamp1, eventFlags1, commandStatus) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));

                                if(commandStatus.lastReceivedSequenceNumber != -1)
                                    textView_SequenceNumber.setText(String.valueOf(commandStatus.lastReceivedSequenceNumber));
                                else
                                    textView_SequenceNumber.setText("No control page Rx'd");

                                switch(commandStatus.status)
                                {
                                    case FAIL:
                                    case NOT_SUPPORTED:
                                    case PASS:
                                    case PENDING:
                                    case REJECTED:
                                    case UNINITIALIZED:
                                    case UNRECOGNIZED:
                                    default:
                                        textView_CommandStatus.setText(commandStatus.status.toString());
                                        break;
                                }

                                String rawData = "";
                                for(byte b : commandStatus.rawResponseData)
                                    rawData += "[" + b + "]";
                                textView_RawData.setText(rawData);

                                textView_LastRxCmdId.setText(commandStatus.lastReceivedCommandId.toString());
                                switch(commandStatus.lastReceivedCommandId)
                                {
                                    case BASIC_RESISTANCE:
                                        textView_TotalResistanceStatus.setText(commandStatus.totalResistance.toString()+ "%");
                                        break;
                                    case TARGET_POWER:
                                        textView_TargetPowerStatus.setText(commandStatus.targetPower.toString() + "W");
                                        break;
                                    case WIND_RESISTANCE:
                                        textView_WindResistanceCoefficientStatus.setText(commandStatus.windResistanceCoefficient.toString() + "kg/m");
                                        textView_WindSpeedStatus.setText(commandStatus.windSpeed.toString() + "km/h");
                                        textView_DraftingFactorStatus.setText(commandStatus.draftingFactor.toString());
                                        break;
                                    case TRACK_RESISTANCE:
                                        textView_GradeStatus.setText(commandStatus.grade.toString() + "%");
                                        textView_RollingResistanceCoefficientStatus.setText(commandStatus.rollingResistanceCoefficient.toString());
                                        break;
                                    case NO_CONTROL_PAGE_RECEIVED:
                                        break;
                                    case UNRECOGNIZED:
                                        break;
                                    default:
                                        break;
                                }

                            }));
                            fePcc.subscribeUserConfigurationEvent((estTimestamp1, eventFlags1, userConfiguration) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_UserWeight.setText(userConfiguration.userWeight != null ? userConfiguration.userWeight.toString() + "kg" : "N/A");
                                textView_BicycleWeight.setText(userConfiguration.bicycleWeight != null ? userConfiguration.bicycleWeight.toString() + "kg" : "N/A");
                                textView_BicycleWheelDiameter.setText(userConfiguration.bicycleWheelDiameter != null ? userConfiguration.bicycleWheelDiameter.toString() + "m" : "N/A");
                                textView_GearRatio.setText(userConfiguration.gearRatio != null ? userConfiguration.gearRatio.toString() : "N/A");
                            }));
                            fePcc.getTrainerMethods().subscribeBasicResistanceEvent((estTimestamp1, eventFlags1, totalResistance) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_TotalResistance.setText(totalResistance.toString() + "%");
                            }));
                            fePcc.getTrainerMethods().subscribeTargetPowerEvent((estTimestamp1, eventFlags1, targetPower) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_TargetPower.setText(targetPower.toString() + "W");
                            }));
                            fePcc.getTrainerMethods().subscribeTrackResistanceEvent((estTimestamp1, eventFlags1, grade, rollingResistanceCoefficient) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_Grade.setText(grade.toString() + "%");
                                textView_RollingResistanceCoefficient.setText(rollingResistanceCoefficient.toString());
                            }));
                            fePcc.getTrainerMethods().subscribeWindResistanceEvent((estTimestamp1, eventFlags1, windResistanceCoefficient, windSpeed, draftingFactor) -> runOnUiThread(() -> {
                                tv_estTimestamp.setText(String.valueOf(estTimestamp1));
                                textView_WindResistanceCoefficient.setText(windResistanceCoefficient.toString() + "kg/m");
                                textView_WindSpeed.setText(String.valueOf(windSpeed) + "km/h");
                                textView_DraftingFactor.setText(draftingFactor.toString());
                            }));
                            subscriptionsDone = true;
                            break;
                        case UNKNOWN:
                            tv_feType.setText("UNKNOWN");
                            break;
                        case UNRECOGNIZED:
                            tv_feType.setText("UNRECOGNIZED type, PluginLib upgrade required?");
                            break;
                        default:
                            tv_feType.setText("INVALID: " + String.valueOf(equipmentType));
                            break;
                    }

                    switch(equipmentState)
                    {
                        case ASLEEP_OFF:
                            tv_state.setText("OFF");
                            break;
                        case READY:
                            tv_state.setText("READY");
                            break;
                        case IN_USE:
                            tv_state.setText("IN USE");
                            break;
                        case FINISHED_PAUSED:
                            tv_state.setText("FINISHED/PAUSE");
                            break;
                        case UNRECOGNIZED:
                            Toast.makeText(Activity_FitnessEquipmentSampler.this,
                                "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                                Toast.LENGTH_SHORT).show();
                        default:
                            tv_state.setText("INVALID: " + equipmentState);
                    }
                });

        //Make the access request
        //If the Activity was passed the bundle it indicates this Activity was started intended for traditional Fitness Equipment, otherwise connect to broadcast based FE Controls
        //TODO Both PCC request types may be done concurrently on separate release handles in order to simultaneously support both types of FE without requiring prior knowledge of types
        if (b == null)
        {
            releaseHandle = AntPlusFitnessEquipmentPcc.requestNewOpenAccess(this,
                this, mPluginAccessResultReceiver, mDeviceStateChangeReceiver,
                mFitnessEquipmentStateReceiver);
        }
        else if (b.containsKey(
            Activity_MultiDeviceSearchSampler.EXTRA_KEY_MULTIDEVICE_SEARCH_RESULT))
        {
            // device has already been selected through the multi-device search
            MultiDeviceSearchResult result = b.getParcelable(
                Activity_MultiDeviceSearchSampler.EXTRA_KEY_MULTIDEVICE_SEARCH_RESULT);

            releaseHandle = AntPlusFitnessEquipmentPcc.requestNewOpenAccess(this,
                result.getAntDeviceNumber(), 0, mPluginAccessResultReceiver,
                mDeviceStateChangeReceiver, mFitnessEquipmentStateReceiver);
        }
        else
        {
            releaseHandle = AntPlusFitnessEquipmentPcc.requestNewPersonalSessionAccess(this,
                mPluginAccessResultReceiver, mDeviceStateChangeReceiver,
                mFitnessEquipmentStateReceiver, 0, settings, files);
        }
    }

    @Override
    protected void onDestroy()
    {
        releaseHandle.close();
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
                resetPcc();
                tv_status.setText("Resetting...");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
