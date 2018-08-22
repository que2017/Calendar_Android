package com.demo.zhang.calendar;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarDatabase {
    private static final String DB_NAME = "Calendar.db";
    private static final int DB_VERSION = 1;
    private static final String TB_NAME = "events_table";

    private static final String KEY_ROWID = "_id";
    private static final String DATE      = "date";
    private static final String EVENT_TITLE = "event_title";
    private static final String EVENT_PLACE = "event_place";
    private static final String IS_FULLDAY  = "is_fullday";
    private static final String START_TIME  = "start_time";
    private static final String END_TIME    = "end_time";

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

    public void InsertEvent(){

    }
}
