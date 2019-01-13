package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dsi.ant.antplus.pluginsampler.R;

/**
 * Created by ksenia on 09.01.19.
 */

public class Activity_WorkoutStatistics extends Activity {
    private TextView tv_workoutTime;
    private TextView tv_averageHr;
    private TextView tv_maxWorkoutHr;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistic);

        tv_workoutTime = findViewById(R.id.textView_timeWorkout);
        tv_averageHr = findViewById(R.id.textView_averageHr);
        tv_maxWorkoutHr = findViewById(R.id.textView_maximumWorkoutHr);
        btn_back = findViewById(R.id.button_back);

        Bundle bundle = getIntent().getExtras();
        WorkoutStatistics statistic = bundle.getParcelable("statistics");

        tv_maxWorkoutHr.setText(statistic.getMaxWorkoutHr() + "bpm");
        tv_averageHr.setText(statistic.getAverageHr() + "bpm");
        long miliSeconds = statistic.getWorkoutTime();
        tv_workoutTime.setText(PulseZoneUtils.fromMillisecondsToTime(miliSeconds));

        btn_back.setOnClickListener(v -> finish());
    }
}
