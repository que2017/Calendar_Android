package com.demo.zhang.calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bAdd;
    private LinearLayout eventContainer;
    private CalendarView calendarView;

    private String currDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAdd = (Button)findViewById(R.id.bAddEvents);
        eventContainer = (LinearLayout)findViewById(R.id.eventContainer);
        calendarView = (CalendarView)findViewById(R.id.calendarView);

        Calendar calendar = Calendar.getInstance();
        currDate = dateToString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
//        Toast.makeText(MainActivity.this, currDate, Toast.LENGTH_SHORT).show();

        bAdd.setOnClickListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currDate = dateToString(year, month + 1, dayOfMonth);
//                Toast.makeText(MainActivity.this, currDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bAddEvents:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddEvent.class);
                Bundle bundle = new Bundle();
                bundle.putString("currDate", currDate);
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
                break;
        }
    }

    private void addEvents() {
        View event = View.inflate(this, R.layout.events,null);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShowEvent.class);
                MainActivity.this.startActivity(intent);
            }
        });
        eventContainer.addView(event);
    }

    private String dateToString(int year, int month, int day){
        return year + DateOperatorUtil.getTwoDigits(month) + DateOperatorUtil.getTwoDigits(day);
    }
}
