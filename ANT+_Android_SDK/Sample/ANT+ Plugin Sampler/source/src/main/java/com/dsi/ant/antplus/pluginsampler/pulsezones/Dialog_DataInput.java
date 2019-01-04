package com.dsi.ant.antplus.pluginsampler.pulsezones;

/**
 * Created by ksenia on 30.12.18.
 */

import com.dsi.ant.antplus.pluginsampler.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Dialog_DataInput extends  DialogFragment {

    RadioGroup radioSexGroup;
    EditText et_age;
    EditText et_rest_hr;
    EditText et_max_hr;
    RadioGroup radioZones;

    int selectedIdSex = 0;
    int selectedIdZone = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Settings Configuration");

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View detailsView = inflater.inflate(R.layout.dialog_pz_settings, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(detailsView);

        radioSexGroup = detailsView.findViewById(R.id.radioGroupSex);
        et_age = detailsView.findViewById(R.id.editText_Age);
        et_rest_hr = detailsView.findViewById(R.id.editText_RestHR);
        et_max_hr = detailsView.findViewById(R.id.editText_MaxHR);
        radioZones = detailsView.findViewById(R.id.radioGroupZones);
        detailsView.findViewById(R.id.radioButton_Female).setOnClickListener((View view) -> selectedIdSex = 0);
        detailsView.findViewById(R.id.radioButton_Male).setOnClickListener((View view) -> selectedIdSex = 1);
        detailsView.findViewById(R.id.radioButton_Zone1).setOnClickListener((View view) -> selectedIdZone = 1);
        detailsView.findViewById(R.id.radioButton_Zone2).setOnClickListener((View view) -> selectedIdZone = 2);
        detailsView.findViewById(R.id.radioButton_Zone3).setOnClickListener((View view) -> selectedIdZone = 3);
        detailsView.findViewById(R.id.radioButton_Zone4).setOnClickListener((View view) -> selectedIdZone = 4);
        detailsView.findViewById(R.id.radioButton_Zone5).setOnClickListener((View view) -> selectedIdZone = 5);

        // Add action buttons
        builder.setPositiveButton("OK", (DialogInterface dialog, int which) -> {});

                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    //Let dialog dismiss
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                Boolean wantToCloseDialog = false;
                if (TextUtils.isEmpty(et_age.getText()) || Integer.parseInt(et_age.getText().toString()) <= 0 || Integer.parseInt(et_age.getText().toString()) > 130) {
                    Toast.makeText(getContext(), "Please, input a valid age", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(et_rest_hr.getText()) || Integer.parseInt(et_rest_hr.getText().toString()) <= 0) {
                    Toast.makeText(getContext(), "Please, input a valid rest heart rate", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(et_max_hr.getText()) || Integer.parseInt(et_max_hr.getText().toString()) < 0) {
                    Toast.makeText(getContext(), "Please, input a valid maximum heart rate or 0", Toast.LENGTH_SHORT).show();
                } else if(Integer.parseInt(et_rest_hr.getText().toString()) >= Integer.parseInt(et_max_hr.getText().toString())) {
                    Toast.makeText(getContext(), "Maximum heart rate cannot be lower than rest heart rate", Toast.LENGTH_SHORT).show();
                } else {
                    int ageFieldValue = Integer.parseInt(et_age.getText().toString());
                    int restHrFieldValue = Integer.parseInt(et_rest_hr.getText().toString());
                    int maxHrFieldValue = Integer.parseInt(et_max_hr.getText().toString());
                    wantToCloseDialog = true;
                    Intent intent = new Intent(Dialog_DataInput.this.getActivity(), Activity_PulseZonesFitness.class);

                    intent.putExtra("settings", new PulseZoneSettings(selectedIdSex, ageFieldValue, restHrFieldValue, maxHrFieldValue, selectedIdZone));
                    startActivity(intent);
                }
                if(wantToCloseDialog)
                    d.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            });
        }
    }
}
