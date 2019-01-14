package com.dsi.ant.antplus.pluginsampler.pulsezones;

/**
 * Created by ksenia on 30.12.18.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.dsi.ant.antplus.pluginsampler.R;

public class Dialog_DataInput extends  DialogFragment {
    private RadioGroup radioSexGroup;
    private NumericEditText et_age;
    private NumericEditText et_rest_hr;
    private EditText et_max_hr;
    private RadioGroup radioZones;

    private PulseZoneSettings pulseSettings;
    // int selectedIdSex = 0;
   // private int selectedIdZone = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Settings Configuration")
                .setPositiveButton("OK", (DialogInterface dialog, int which) -> {})
                .setNegativeButton("Cancel", (dialog, which) -> {
                    //Let dialog dismiss
                });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View detailsView = inflater.inflate(R.layout.dialog_pz_settings, null);
        builder.setView(detailsView);

        radioSexGroup = detailsView.findViewById(R.id.radioGroupSex);
        et_age = detailsView.findViewById(R.id.editText_Age);
        et_rest_hr = detailsView.findViewById(R.id.editText_RestHR);
        et_max_hr = detailsView.findViewById(R.id.editText_MaxHR);
        radioZones = detailsView.findViewById(R.id.radioGroupZones);
        /*detailsView.findViewById(R.id.radioButton_Female).setOnClickListener((View view) -> selectedIdSex = 0);
        detailsView.findViewById(R.id.radioButton_Male).setOnClickListener((View view) -> selectedIdSex = 1);
        detailsView.findViewById(R.id.radioButton_Zone1).setOnClickListener((View view) -> selectedIdZone = 1);
        detailsView.findViewById(R.id.radioButton_Zone2).setOnClickListener((View view) -> selectedIdZone = 2);
        detailsView.findViewById(R.id.radioButton_Zone3).setOnClickListener((View view) -> selectedIdZone = 3);
        detailsView.findViewById(R.id.radioButton_Zone4).setOnClickListener((View view) -> selectedIdZone = 4);
        detailsView.findViewById(R.id.radioButton_Zone5).setOnClickListener((View view) -> selectedIdZone = 5);*/

        //Get shared preferences
        pulseSettings = new PulseZoneSettings(detailsView.getContext());
        radioSexGroup.check(pulseSettings.getGender());
        et_age.setText(String.valueOf(pulseSettings.getAge()));
        et_rest_hr.setText(String.valueOf(pulseSettings.getRestHr()));
        et_max_hr.setText(String.valueOf(pulseSettings.getMaxHr()));
        radioZones.check(pulseSettings.getZone());

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                if(isFormValid()) {
                    //Controls if the dialog should be close on Ok click
                    Boolean wantToCloseDialog;
                    int ageFieldValue = Integer.parseInt(et_age.getText().toString());
                    int restHrFieldValue = Integer.parseInt(et_rest_hr.getText().toString());
                    int maxHrFieldValue;
                    if(TextUtils.isEmpty(et_max_hr.getText())) {
                        maxHrFieldValue = 0;
                    } else {
                        maxHrFieldValue = Integer.parseInt(et_max_hr.getText().toString());
                    }

                    pulseSettings = new PulseZoneSettings(getContext());
                    pulseSettings.setGender(radioSexGroup.getCheckedRadioButtonId());
                    pulseSettings.setAge(ageFieldValue);
                    pulseSettings.setRestHr(restHrFieldValue);
                    pulseSettings.setMaxHr(maxHrFieldValue);
                    pulseSettings.setZone(radioZones.getCheckedRadioButtonId());
                    pulseSettings.save();

                    wantToCloseDialog = true;
                    //Starts new activity
                    Intent intent = new Intent(Dialog_DataInput.this.getActivity(), Activity_PulseZonesFitness.class);
                    startActivity(intent);
                    if(wantToCloseDialog)
                        d.dismiss();
                    //else dialog stays open.
                }
            });
        }
    }

    /**
     * Returns if age and rest heart rate values are in valid range
     * @return - true, if values are in valid range, false, if not
     */
    private boolean isFormValid() {
        return et_age.isValid() && et_rest_hr.isValid();
    }
}
