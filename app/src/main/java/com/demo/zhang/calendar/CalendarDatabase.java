package com.demo.zhang.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class CalendarDatabase {
    private static final String TAG = CalendarDatabase.class.getSimpleName();

    private static final String DB_NAME = "Calendar.db";
    private static final int DB_VERSION = 1;
    private static final String TB_NAME = "events_table";

//    private static final String KEY_ROWID = "_id";
//    private static final String DATE      = "date";
//    private static final String EVENT_TITLE = "event_title";
//    private static final String EVENT_PLACE = "event_place";
//    private static final String IS_FULLDAY  = "is_fullday";
//    private static final String START_TIME  = "start_time";
//    private static final String END_TIME    = "end_time";

    public static final String KEY_ROWID = "_id";
    public static final String DATE      = "date";
    public static final String EVENT_TITLE = "event_title";
    public static final String EVENT_PLACE = "event_place";
    public static final String IS_FULLDAY  = "is_fullday";
    public static final String START_TIME  = "start_time";
    public static final String END_TIME    = "end_time";

    private CalendarDBHelper calendarDBHelper;
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public CalendarDatabase(Context context){
        mContext = context;
    }

    private class CalendarDBHelper extends SQLiteOpenHelper{
        public CalendarDBHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                    + TB_NAME + "("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATE + " VARCHAR(8) NOT NULL, "
                    + EVENT_TITLE + " TEXT, "
                    + EVENT_PLACE + " TEXT NOT NULL, "
                    + IS_FULLDAY + " INTEGER NOT NULL, "
                    + START_TIME + " VARCHAR(5) NOT NULL, "
                    + END_TIME + " VARCHAR(5) NOT NULL" + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
            onCreate(db);
        }
    }

    public CalendarDatabase open() throws SQLException{
        calendarDBHelper = new CalendarDBHelper(mContext);
        mDatabase = calendarDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        calendarDBHelper.close();
    }

    public Cursor search_Event(String date){
        String query = "SELECT * FROM " + TB_NAME + " WHERE " + DATE + " = ?";
        Cursor cursor = mDatabase.rawQuery(query, new String[]{date});
        if(null != cursor) {
            Log.i(TAG, "searchEvent success: " + date);
            return cursor;
        }
        Log.i(TAG, "searchEvent fail: " + date);
        return null;
    }

    public void insert_Event(String date, String eventTitle, String eventPlace, int isFullday, String startTime, String endTime){
//        String insert = "INSERT INTO " + TB_NAME + " ("
//                + DATE + ", " + EVENT_TITLE + ", " + EVENT_PLACE + ", " + IS_FULLDAY + ", " + START_TIME + ", " + END_TIME + ") VALUES ( "
//                + "'" + date + "', '" + eventTitle + "', '" + eventPlace + "', " + isFullday + ", '" + startTime + ", '" + endTime + "')";

//        mDatabase.execSQL(insert);
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(EVENT_TITLE, eventTitle);
        values.put(EVENT_PLACE, eventPlace);
        values.put(IS_FULLDAY, isFullday);
        values.put(START_TIME, startTime);
        values.put(END_TIME, endTime);
        mDatabase.insert(TB_NAME, null, values);
    }

    public void modify_Event(){

    }

    public void delete_Event(String id){

    }
}
