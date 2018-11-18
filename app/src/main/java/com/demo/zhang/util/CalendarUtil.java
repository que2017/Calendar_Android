package com.demo.zhang.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class CalendarUtil {

    public static Cursor queryGoogleCalendar(Context context, long start, long end) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(ConstantUtil.CALENDAR_EVENT_URI);
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, null,
                    "(" + ConstantUtil.DSTSRT + " >= ? AND "
                    + ConstantUtil.DSTSRT + " < ?) OR ("
                    + ConstantUtil.DEND + " >= ? AND "
                    + ConstantUtil.DEND + " < ?)", new String[]{String.valueOf(start), String.valueOf(end), String.valueOf(start), String.valueOf(end)},
                    ConstantUtil.DSTSRT + ", " + ConstantUtil.DEND);
//            cursor = cr.query(uri, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public static void inertGoogleCalendar(Context context, ContentValues values){
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(ConstantUtil.CALENDAR_EVENT_URI);
        values.put(ConstantUtil.CALENDAR_ID, checkCalendarAccount(context));
        cr.insert(uri, values);
    }

    private static int checkCalendarAccount(Context context) {
        Uri uri = Uri.parse(ConstantUtil.CALENDAR_URI);
        int account = -1;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            if(cursor != null) {
                int count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToNext();
                    account = cursor.getInt(cursor.getColumnIndex(ConstantUtil._ID));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        if (-1 == account) {
            account = ConstantUtil.CAL_ID;
        }
        return account;
    }
}
