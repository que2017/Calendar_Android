package com.demo.zhang.calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity implements View.OnClickListener{
    private final String TAG = MainActivity.class.getSimpleName();

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
                showTodayEvents();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTodayEvents();
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

    private void showTodayEvents() {
        eventContainer.removeAllViews();
        CalendarDatabase cd = new CalendarDatabase(this);
        cd.open();
        Cursor cursor = cd.search_Event(currDate);
        if(cursor.moveToFirst()){
            do{
                View event = View.inflate(this, R.layout.events,null);
                TextView tEventTitle = (TextView) event.findViewById(R.id.tEventTitle);
                TextView tEventPlace = (TextView) event.findViewById(R.id.tEventPlace);
                TextView tStartTime = (TextView) event.findViewById(R.id.tStartTime);
                TextView tEndTime = (TextView) event.findViewById(R.id.tEndTime);

                String id = cursor.getString(cursor.getColumnIndex(CalendarDatabase.KEY_ROWID));
                String eventTitle = cursor.getString(cursor.getColumnIndex(CalendarDatabase.EVENT_TITLE));
                String eventPlace = cursor.getString(cursor.getColumnIndex(CalendarDatabase.EVENT_PLACE));
                String startTime = cursor.getString(cursor.getColumnIndex(CalendarDatabase.START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(CalendarDatabase.END_TIME));

                tEventTitle.setText(eventTitle);
                tEventPlace.setText(eventPlace);
                tStartTime.setText(startTime);
                tEndTime.setText(endTime);

                final Bundle bundle = new Bundle();
                bundle.putString(CalendarDatabase.KEY_ROWID, id);
                bundle.putString(CalendarDatabase.EVENT_TITLE, eventTitle);
                bundle.putString(CalendarDatabase.EVENT_PLACE, eventPlace);
                bundle.putString(CalendarDatabase.START_TIME, startTime);
                bundle.putString(CalendarDatabase.END_TIME, endTime);
                event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, ShowEvent.class);
                    MainActivity.this.startActivity(intent);
                    }
                });
                eventContainer.addView(event);
            }while(cursor.moveToNext());
        }
        cd.close();

    }

    private String dateToString(int year, int month, int day){
        return year + DateOperatorUtil.getTwoDigits(month) + DateOperatorUtil.getTwoDigits(day);
    }
}
