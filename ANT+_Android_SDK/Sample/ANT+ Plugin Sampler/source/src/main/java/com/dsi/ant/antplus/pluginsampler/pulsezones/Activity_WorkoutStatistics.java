package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
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

    private HRRecordsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_statistic);

        tv_workoutTime = findViewById(R.id.textView_timeWorkout);
        tv_averageHr = findViewById(R.id.textView_averageHr);
        tv_maxWorkoutHr = findViewById(R.id.textView_maximumWorkoutHr);
        btn_back = findViewById(R.id.button_back);

        Intent intent = getIntent();
        long startTime = intent.getLongExtra(Activity_PulseZonesFitness.START_TIMING, 0);
        String workoutTime = intent.getStringExtra(Activity_PulseZonesFitness.WORKOUT_TIME);
        repository = new HRRecordsRepository(this);

        tv_maxWorkoutHr.setText(repository.getMaxHeartRate(startTime, SystemClock.elapsedRealtime()) + "bpm");
        tv_averageHr.setText(repository.getAverageHeartRate(startTime, SystemClock.elapsedRealtime()) + "bpm");
        tv_workoutTime.setText(workoutTime);

        btn_back.setOnClickListener(v -> finish());
    }
}
