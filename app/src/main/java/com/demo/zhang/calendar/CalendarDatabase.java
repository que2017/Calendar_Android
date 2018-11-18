package com.demo.zhang.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.demo.zhang.util.ConstantUtil;

//import java.util.Date;

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
                    + ConstantUtil.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ConstantUtil.CALENDAR_ID + " INTEGER NOT NULL, "
                    + ConstantUtil.EVENT_TITLE + " TEXT, "
                    + ConstantUtil.EVENT_PLACE + " TEXT NOT NULL, "
                    + ConstantUtil.IS_FULLDAY + " INTEGER NOT NULL, "
                    + ConstantUtil.START_TIME + " INTEGER NOT NULL, "
                    + ConstantUtil.END_TIME + " INTEGER NOT NULL" + ")");
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

    public Cursor search_Event(long startDate, long endDate){
        String query = "SELECT * FROM " + TB_NAME
                + " WHERE (" + ConstantUtil.START_TIME + " >= ? AND "
                + ConstantUtil.START_TIME + " < ?) OR ("
                + ConstantUtil.END_TIME + " >= ? AND "
                + ConstantUtil.END_TIME + " < ?) ORDER BY " + ConstantUtil.START_TIME + ", " + ConstantUtil.END_TIME;
        Cursor cursor = mDatabase.rawQuery(query, new String[]{String.valueOf(startDate), String.valueOf(endDate), String.valueOf(startDate), String.valueOf(endDate)});
        if(null != cursor) {
            Log.i(TAG, "searchEvent success");
            return cursor;
        }
        Log.i(TAG, "searchEvent fail");
        return null;
    }

    public void insert_Event(long calendar_id, String eventTitle, String eventPlace, int isFullday, long startTime, long endTime){
//        String insert = "INSERT INTO " + TB_NAME + " ("
//                + DATE + ", " + EVENT_TITLE + ", " + EVENT_PLACE + ", " + IS_FULLDAY + ", " + START_TIME + ", " + END_TIME + ") VALUES ( "
//                + "'" + date + "', '" + eventTitle + "', '" + eventPlace + "', " + isFullday + ", '" + startTime + ", '" + endTime + "')";

//        mDatabase.execSQL(insert);
        ContentValues values = new ContentValues();
        values.put(ConstantUtil.CALENDAR_ID, calendar_id);
        values.put(ConstantUtil.EVENT_TITLE, eventTitle);
        values.put(ConstantUtil.EVENT_PLACE, eventPlace);
        values.put(ConstantUtil.IS_FULLDAY, isFullday);
        values.put(ConstantUtil.START_TIME, startTime);
        values.put(ConstantUtil.END_TIME, endTime);
        mDatabase.insert(TB_NAME, null, values);
    }

    public void modify_Event(String id,  String eventTitle, String eventPlace, int isFullday, String startTime, String endTime){
        ContentValues values = new ContentValues();
        values.put(ConstantUtil.EVENT_TITLE, eventTitle);
        values.put(ConstantUtil.EVENT_PLACE, eventPlace);
        values.put(ConstantUtil.IS_FULLDAY, isFullday);
        values.put(ConstantUtil.START_TIME, startTime);
        values.put(ConstantUtil.END_TIME, endTime);
        mDatabase.update(TB_NAME, values, ConstantUtil.KEY_ROWID + " = ?", new String[]{id});
    }

    public void delete_Event(String id){
        mDatabase.delete(TB_NAME, ConstantUtil.KEY_ROWID + " = ?" ,new String[]{id});
    }
}
