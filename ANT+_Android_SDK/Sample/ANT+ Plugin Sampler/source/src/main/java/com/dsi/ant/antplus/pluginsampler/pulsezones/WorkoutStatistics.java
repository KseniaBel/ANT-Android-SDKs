package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksenia on 10.01.19.
 */

public class WorkoutStatistics implements Parcelable {
    private int maxWorkoutHr;
    private int averageHr;
    private long workoutTime;


    public WorkoutStatistics(int maxWorkoutHr, int averageHr, long workoutTime) {
        this.maxWorkoutHr = maxWorkoutHr;
        this.averageHr = averageHr;
        this.workoutTime = workoutTime;
    }

    protected WorkoutStatistics(Parcel in) {
        maxWorkoutHr = in.readInt();
        averageHr = in.readInt();
        workoutTime = in.readLong();
    }

    public int getMaxWorkoutHr() {
        return maxWorkoutHr;
    }

    public int getAverageHr() {
        return averageHr;
    }

    public long getWorkoutTime() {
        return workoutTime;
    }

    public static final Creator<WorkoutStatistics> CREATOR = new Creator<WorkoutStatistics>() {
        @Override
        public WorkoutStatistics createFromParcel(Parcel in) {
            return new WorkoutStatistics(in);
        }

        @Override
        public WorkoutStatistics[] newArray(int size) {
            return new WorkoutStatistics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxWorkoutHr);
        dest.writeInt(averageHr);
        dest.writeLong(workoutTime);
    }
}
