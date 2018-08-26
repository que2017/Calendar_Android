package com.demo.zhang.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AddEvent extends Add_Or_Modify_Event {
    private String currDate;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        currDate = (String) bundle.get("currDate");

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
        CalendarDatabase cd = new CalendarDatabase(this);
        String title = eventTitle.getText().toString();
        String place = eventPlace.getText().toString();
        String start = startTimeShow.getText().toString();
        String end   = endTimeShow.getText().toString();
        if((null == place) || (null == start) || (null == end)){
            Toast.makeText(this, "地点和时间不能为空！", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "place or time is null");
            return;
        }
        cd.open();
        cd.insert_Event(currDate, title, place, isFullday, start, end);
        cd.close();
        this.finish();
    }
}
