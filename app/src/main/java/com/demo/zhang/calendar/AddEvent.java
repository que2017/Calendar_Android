package com.demo.zhang.calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddEvent extends Activity implements View.OnClickListener {
    private Button bQuit;
    private Button bCorrect;
    private EditText eventTitle;
    private EditText eventPlace;
    private LinearLayout startTime;
    private LinearLayout endTime;
    private TextView startTimeShow;
    private TextView endTimeShow;

    private String currDate;

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
    }

    private void pickStartTime() {
        pickTime(startTimeShow);
    }

    private void pickEndTime() {
        pickTime(endTimeShow);
    }

    private void pickTime(final TextView tv){
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, 2, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv.setText(DateOperatorUtil.getTwoDigits(hourOfDay) + ":" + DateOperatorUtil.getTwoDigits(minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}
