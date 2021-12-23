package com.sp.p2020358assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        onCreate(db);
    }

    // insert info function
    public long insertInfo(String name, String date, double length, double weight, double lat, double lon, byte[] image, String addTimeStamp, String updateTimeStamp) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.C_NAME, name);
        cv.put(Constants.C_DATE, date);
        cv.put(Constants.C_LENGTH, length);
        cv.put(Constants.C_WEIGHT, weight);
        cv.put(Constants.C_LAT, lat);
        cv.put(Constants.C_LON, lon);
        cv.put(Constants.C_IMAGE, image);
        cv.put(Constants.C_ADD_TIMESTAMP, addTimeStamp);
        cv.put(Constants.C_UPDATE_TIMESTAMP, updateTimeStamp);

        long id = db.insert(Constants.TABLE_NAME, Constants.C_NAME, cv);
        db.close();
        return id;
    }
}
