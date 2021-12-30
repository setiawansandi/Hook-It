package sp.com.p2020358assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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
    public long insertInfo(String name, String date, String length, String weight, double lat, double lon, byte[] image, String addTimeStamp, String updateTimeStamp) {

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



    public ArrayList<Model> getAllData (String orderBy) {
        ArrayList<Model> arrayList = new ArrayList<>();

        // query for select all info in database
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        // curate the data from all the column in each row, moveToNext() -> Move the cursor to the next row, start from index -1
        if(cursor.moveToNext()) {
            do {
                Model model = new Model(
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_LENGTH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_WEIGHT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADD_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATE_TIMESTAMP)),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.C_LAT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.C_LON))
                );

                arrayList.add(model);
            } while (cursor.moveToNext());
        }

        db.close();
        return arrayList;
    }
}
