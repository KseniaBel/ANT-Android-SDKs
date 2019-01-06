package com.dsi.ant.antplus.pluginsampler.pulsezones;

/**
 * Created by ksenia on 30.12.18.
 */

import com.dsi.ant.antplus.pluginsampler.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
    ValidatableEditText et_age;
    ValidatableEditText et_rest_hr;
    EditText et_max_hr;
    RadioGroup radioZones;
    SharedPreferences myPrefs;

    int selectedIdSex = 0;
    int selectedIdZone = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Settings Configuration")
                .setPositiveButton("OK", (DialogInterface dialog, int which) -> {})
                .setNegativeButton("Cancel", (dialog, which) -> {
                    //Let dialog dismiss
                 });

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


        //Get shared preferences
        myPrefs = detailsView.getContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        int userSex = myPrefs.getInt("userSexKey", R.id.radioButton_Female);
        radioSexGroup.check(userSex);
        int age = myPrefs.getInt("ageKey", 0);
        et_age.setText((String.valueOf(age)));
        int restHr = myPrefs.getInt("restHrKey",0);
        et_rest_hr.setText((String.valueOf(restHr)));
        int maxHr = myPrefs.getInt("maxHrKey",0);
        et_max_hr.setText((String.valueOf(maxHr)));

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
                    Boolean wantToCloseDialog = false;
                    int ageFieldValue = Integer.parseInt(et_age.getText().toString());
                    int restHrFieldValue = Integer.parseInt(et_rest_hr.getText().toString());
                    int maxHrFieldValue;
                    if(TextUtils.isEmpty(et_max_hr.getText())) {
                        maxHrFieldValue = calculateMaxHrFieldValue(ageFieldValue);
                    } else {
                        maxHrFieldValue = Integer.parseInt(et_max_hr.getText().toString());
                    }

                    myPrefs = getContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.putInt("userSexKey", radioSexGroup.getCheckedRadioButtonId());
                    editor.putInt("ageKey", ageFieldValue);
                    editor.putInt("restHrKey", restHrFieldValue);
                    editor.putInt("maxHrKey", maxHrFieldValue);

                    editor.apply();
                    wantToCloseDialog = true;
                    Intent intent = new Intent(Dialog_DataInput.this.getActivity(), Activity_PulseZonesFitness.class);
                    intent.putExtra("settings", new PulseZoneSettings(selectedIdSex, ageFieldValue, restHrFieldValue, maxHrFieldValue, selectedIdZone));
                    startActivity(intent);
                    if(wantToCloseDialog)
                        d.dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }

    private int calculateMaxHrFieldValue(int ageFieldValue) {
        int maxHrFieldValue;
        if(ageFieldValue == 0) {
            maxHrFieldValue = (int) Math.round(209 - ageFieldValue * 0.7);
        } else {
            maxHrFieldValue = (int) Math.round(214 - ageFieldValue * 0.8);
        }
        return maxHrFieldValue;
    }

    private boolean isFormValid() {
        return et_age.isValid() && et_rest_hr.isValid();
    }
}
