package com.demo.zhang.calendar;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.demo.zhang.util.CalendarUtil;
import com.demo.zhang.util.ConstantUtil;

import java.util.Calendar;
import java.util.TimeZone;

public class AddEvent extends Add_Or_Modify_Event {
    private long date;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkCalendarReadPermission();
        init();

        interfaceTitle.setText("新建活动");
        date = (long) bundle.get(ConstantUtil.DATE);

        // 初始化startTimeShow 和 endTimeShow 的text
        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currMin = calendar.get(Calendar.MINUTE);
        startTimeShow.setText(stringTime(currHour, currMin));
        if(currHour >= 23){
            endTimeShow.setText(stringTime(0, 0));
        }else{
            endTimeShow.setText(stringTime(currHour + 1, currMin));
        }
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
        startTimeMillis = date + (digitalHour(start) * 60 + digitalMinute(start)) * 60 * 1000;
        endTimeMillis = date + (digitalHour(end) * 60 + digitalMinute(end)) * 60 * 1000;

//        cd.open();
//        cd.insert_Event(currDate, title, place, isFullday, start, end);
        ContentValues values = new ContentValues();
        values.put(ConstantUtil.TITLE, title);
//        values.put(ConstantUtil.DESCRIPTION, title);
        values.put(ConstantUtil.EVENT_LOCATION, place);
        values.put(ConstantUtil.DSTSRT, startTimeMillis);
        values.put(ConstantUtil.DEND, endTimeMillis);
        values.put(ConstantUtil.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        CalendarUtil.inertGoogleCalendar(this, values);

//        cd.close();
        this.finish();
    }
}
