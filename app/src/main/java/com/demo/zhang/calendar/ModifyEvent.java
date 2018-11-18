package com.demo.zhang.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.demo.zhang.util.ConstantUtil;

public class ModifyEvent extends Add_Or_Modify_Event {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkCalendarReadPermission();
        init();
        interfaceTitle.setText("修改活动");

        eventTitle.setText((String)bundle.get(ConstantUtil.EVENT_TITLE));
        eventPlace.setText((String)bundle.get(ConstantUtil.EVENT_PLACE));
        startTimeShow.setText((String)bundle.get(ConstantUtil.START_TIME));
        endTimeShow.setText((String)bundle.get(ConstantUtil.END_TIME));
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
        cd.modify_Event((String)bundle.get(ConstantUtil.KEY_ROWID), title, place, isFullday, start, end);
        cd.close();
        this.finish();
    }
}
