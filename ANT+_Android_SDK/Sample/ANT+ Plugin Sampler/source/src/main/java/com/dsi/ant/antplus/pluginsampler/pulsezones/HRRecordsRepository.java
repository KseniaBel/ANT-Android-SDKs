package com.dsi.ant.antplus.pluginsampler.pulsezones;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ksenia on 08.01.19.
 */

public class HRRecordsRepository {
    SQLiteDatabase database;

    public HRRecordsRepository(Activity activity) {
        database =  new FitnessSQLiteDBHelper(activity).getWritableDatabase();
    }

    public void addNewRecord(int heartRate) {
        ContentValues values = new ContentValues();
        values.put(FitnessSQLiteDBHelper.RECORDS_COLUMN_HR, heartRate);
        values.put(FitnessSQLiteDBHelper.RECORDS_TIMESTAMP, System.currentTimeMillis());
        database.insert(FitnessSQLiteDBHelper.RECORDS_TABLE_NAME, null, values);
    }

    public void closeDb() {
        database.close();
    }

}
