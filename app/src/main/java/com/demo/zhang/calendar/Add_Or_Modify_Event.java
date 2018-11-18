package com.demo.zhang.calendar;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.demo.zhang.util.ConstantUtil;
import com.demo.zhang.util.DateOperatorUtil;

public abstract class Add_Or_Modify_Event extends Activity implements View.OnClickListener {
    public final String TAG = Add_Or_Modify_Event.class.getSimpleName();

    public Button bQuit;
    public Button bCorrect;
    public TextView interfaceTitle;
    public EditText eventTitle;
    public EditText eventPlace;
    public LinearLayout startTime;
    public LinearLayout endTime;
    public TextView startTimeShow;
    public TextView endTimeShow;

    public int isFullday = 0;
    public long startTimeMillis;
    public long endTimeMillis;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case ConstantUtil.CALENDAR_WRITE_PERMISSION:
                if(grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请开启日历写入权限！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    public void init(){
        setContentView(R.layout.add_event);

        bQuit = (Button)findViewById(R.id.eventQuit);
        bCorrect = (Button)findViewById(R.id.eventCorrect);
        interfaceTitle = (TextView)findViewById(R.id.add_modify_event);
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
        new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv.setText(stringTime(hourOfDay, minute));
            }
        }, digitalHour(tv.getText().toString()), digitalMinute(tv.getText().toString()), true).show();
    }

    public String stringTime(int hour, int minute){
        return DateOperatorUtil.getTwoDigits(hour) + ":" + DateOperatorUtil.getTwoDigits(minute);
    }

    public int digitalHour(String time){
        String[] str = time.split(":");
        return Integer.parseInt(str[0]);
    }

    public int digitalMinute(String time){
        String[] str = time.split(":");
        return Integer.parseInt(str[1]);
    }

    public void checkCalendarReadPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, ConstantUtil.CALENDAR_WRITE_PERMISSION);
        }
    }
}
