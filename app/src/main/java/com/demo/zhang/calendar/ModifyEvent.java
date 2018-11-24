package com.demo.zhang.calendar;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.demo.zhang.util.CalendarUtil;
import com.demo.zhang.util.ConstantUtil;
import com.demo.zhang.util.DateOperatorUtil;

import java.util.TimeZone;

public class ModifyEvent extends Add_Or_Modify_Event {
    private long date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkCalendarReadPermission();
        init();
        interfaceTitle.setText("修改活动");
        date = (long) bundle.get(ConstantUtil.DATE);

        eventTitle.setText((String)bundle.get(ConstantUtil.EVENT_TITLE));
        eventPlace.setText((String)bundle.get(ConstantUtil.EVENT_PLACE));
        startTimeShow.setText(DateOperatorUtil.getShowTime((long)bundle.get(ConstantUtil.START_TIME)));
        endTimeShow.setText(DateOperatorUtil.getShowTime((long)bundle.get(ConstantUtil.END_TIME)));
    }

    @Override
    public void addEventToTable() {
//        CalendarDatabase cd = new CalendarDatabase(this);
        String title = eventTitle.getText().toString();
        String place = eventPlace.getText().toString();
        String start = startTimeShow.getText().toString();
        String end   = endTimeShow.getText().toString();
        if((null == place) || (null == start) || (null == end)){
            Toast.makeText(this, "地点和时间不能为空！", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "place or time is null");
            return;
        }
//        cd.open();
//        cd.modify_Event((String)bundle.get(ConstantUtil.KEY_ROWID), title, place, isFullday, start, end);
//        cd.close();
        startTimeMillis = date + (digitalHour(start) * 60 + digitalMinute(start)) * 60 * 1000;
        endTimeMillis = date + (digitalHour(end) * 60 + digitalMinute(end)) * 60 * 1000;

        ContentValues values = new ContentValues();
        values.put(ConstantUtil.KEY_ROWID, bundle.getLong(ConstantUtil.KEY_ROWID));
        values.put(ConstantUtil.TITLE, title);
//        values.put(ConstantUtil.DESCRIPTION, title);
        values.put(ConstantUtil.EVENT_LOCATION, place);
        values.put(ConstantUtil.DSTSRT, startTimeMillis);
        values.put(ConstantUtil.DEND, endTimeMillis);
        values.put(ConstantUtil.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        CalendarUtil.updateGoogleCalendar(this, values);
        this.finish();
    }
}
