package com.demo.zhang.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Calendar.db";
    public static final int DB_VERSION = 1;
    public static final String TB_NAME = "events_table";

    public static final String KEY_ROWID = "_id";
    public static final String EVENT_TITLE = "event_title";
    public static final String EVENT_PLACE = "event_place";
    public static final String IS_FULLDAY  = "is_fullday";
    public static final String START_TIME  = "start_time";
    public static final String END_TIME    = "end_time";

    public CalendarDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                    + TB_NAME + "("
                    + KEY_ROWID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EVENT_TITLE + "TEXT, "
                    + EVENT_PLACE + "TEXT NOT NULL, "
                    + IS_FULLDAY + "INTEGER NOT NULL, "
                    + START_TIME + "INTEGER NOT NULL, "
                    + END_TIME + "INTEGER NOT NULL" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}
