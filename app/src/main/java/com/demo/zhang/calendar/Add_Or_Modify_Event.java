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

public abstract class Add_Or_Modify_Event extends Activity implements View.OnClickListener {
    public final String TAG = Add_Or_Modify_Event.class.getSimpleName();

    public Button bQuit;
    public Button bCorrect;
    public EditText eventTitle;
    public EditText eventPlace;
    public LinearLayout startTime;
    public LinearLayout endTime;
    public TextView startTimeShow;
    public TextView endTimeShow;

    public int isFullday = 0;
    public Bundle bundle;

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

    public void init(){
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

        bundle = this.getIntent().getExtras();
    }

    public abstract void addEventToTable();

    private void pickStartTime() {
        pickTime(startTimeShow);
    }

    private void pickEndTime() {
        pickTime(endTimeShow);
    }

    private void pickTime(final TextView tv){
        new TimePickerDialog(this, 2, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv.setText(stringTime(hourOfDay, minute));
            }
        }, digitalHour(tv.getText().toString()), digitalMinute(tv.getText().toString()), true).show();
    }

    public String stringTime(int hour, int minute){
        return DateOperatorUtil.getTwoDigits(hour) + ":" + DateOperatorUtil.getTwoDigits(minute);
    }

    private int digitalHour(String time){
        String[] str = time.split(":");
        return Integer.parseInt(str[0]);
    }

    private int digitalMinute(String time){
        String[] str = time.split(":");
        return Integer.parseInt(str[1]);
    }
}
