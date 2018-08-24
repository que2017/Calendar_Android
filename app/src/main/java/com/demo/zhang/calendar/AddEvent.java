package com.demo.zhang.calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddEvent extends Activity implements View.OnClickListener {
    private final String TAG = AddEvent.class.getSimpleName();

    private Button bQuit;
    private Button bCorrect;
    private EditText eventTitle;
    private EditText eventPlace;
    private LinearLayout startTime;
    private LinearLayout endTime;
    private TextView startTimeShow;
    private TextView endTimeShow;

    private int isFullday = 0;
    private String currDate;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        bQuit = (Button)findViewById(R.id.eventQuit);
        bCorrect = (Button)findViewById(R.id.eventCorrect);
        eventTitle = (EditText)findViewById(R.id.eventTitle);
        eventPlace = (EditText)findViewById(R.id.eventPlace);
        startTime = (LinearLayout)findViewById(R.id.startTime);
        endTime = (LinearLayout)findViewById(R.id.endTime);
        startTimeShow = (TextView)findViewById(R.id.startTimeShow);
        endTimeShow = (TextView)findViewById(R.id.endTimeShow);

        bQuit.setOnClickListener(this);
        bCorrect.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        currDate = (String) bundle.get("currDate");
//        Toast.makeText(this, currDate, Toast.LENGTH_SHORT).show();

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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.eventQuit:
                this.finish();
                break;
            case R.id.eventCorrect:
                addEventToTable();
                break;
            case R.id.startTime:
                pickStartTime();
                break;
            case R.id.endTime:
                pickEndTime();
                break;
        }
    }

    private void addEventToTable() {
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

    private void pickStartTime() {
        pickTime(startTimeShow);
    }

    private void pickEndTime() {
        pickTime(endTimeShow);
    }

    private void pickTime(final TextView tv){
        calendar = Calendar.getInstance();
        new TimePickerDialog(this, 2, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv.setText(stringTime(hourOfDay, minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private String stringTime(int hour, int minute){
        return DateOperatorUtil.getTwoDigits(hour) + ":" + DateOperatorUtil.getTwoDigits(minute);
    }
}
