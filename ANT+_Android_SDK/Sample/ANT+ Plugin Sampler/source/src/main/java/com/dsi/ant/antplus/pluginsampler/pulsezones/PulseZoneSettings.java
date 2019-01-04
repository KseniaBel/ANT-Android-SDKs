package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by ksenia on 01.01.19.
 */

public class PulseZoneSettings implements Parcelable {
    private int gender;
    private int age;
    private int restHr;
    private int maxHr;
    private int zone;

    public PulseZoneSettings(int gender, int age, int restHr, int maxHr, int zone){
        this.gender = gender;
        this.age = age;
        this.restHr = restHr;
        this.maxHr = maxHr;
        this.zone = zone;
    }

    public int getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getrestHr() {
        return restHr;
    }

    public int getMaxHr() {
        return maxHr;
    }

    public int getZone() {
        return zone;
    }


    protected PulseZoneSettings(Parcel in) {
        this.gender = in.readInt();
        this.age = in.readInt();
        this.restHr =  in.readInt();
        this.maxHr = in.readInt();
        this.zone = in.readInt();
    }

    public static final Creator<PulseZoneSettings> CREATOR = new Creator<PulseZoneSettings>() {
        @Override
        public PulseZoneSettings createFromParcel(Parcel in) {

            return new PulseZoneSettings(in);
        }

        @Override
        public PulseZoneSettings[] newArray(int size) {

            return new PulseZoneSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gender);
        dest.writeInt(this.age);
        dest.writeInt(this.restHr);
        dest.writeInt(this.maxHr);
        dest.writeInt(this.zone);
    }

    @Override
    public String toString(){
        return Arrays.asList(gender, age, restHr, maxHr, zone).toString();
    }
}
