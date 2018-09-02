package com.demo.zhang.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener{
    private final String TAG = MainActivity.class.getSimpleName();

    private TextView showLunar;
    private Button bAdd;
    private LinearLayout eventContainer;
    private CalendarView calendarView;
    private Calendar calendar;

    private String currDate;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showLunar = (TextView)findViewById(R.id.showLunar);
        bAdd = (Button)findViewById(R.id.bAddEvents);
        eventContainer = (LinearLayout)findViewById(R.id.eventContainer);
        calendarView = (CalendarView)findViewById(R.id.calendarView);

        calendar = Calendar.getInstance();
        currDate = dateToString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        date = calendar.getTime();
//        Toast.makeText(MainActivity.this, currDate, Toast.LENGTH_SHORT).show();

        bAdd.setOnClickListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currDate = dateToString(year, month + 1, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                try {
                    date = simpleDateFormat.parse(currDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(MainActivity.this, currDate, Toast.LENGTH_SHORT).show();
                showFocusDateEvents();
                showFocusDateLunar(date);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFocusDateEvents();
        showFocusDateLunar(date);
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

    @SuppressLint("ResourceAsColor")
    private void showFocusDateEvents() {
        eventContainer.removeAllViews();
        CalendarDatabase cd = new CalendarDatabase(this);
        cd.open();
        Cursor cursor = cd.search_Event(currDate);
        if(cursor.moveToFirst()){
            do{
                View event = View.inflate(this, R.layout.events,null);
                ImageView imgDote = (ImageView)event.findViewById(R.id.eventDote);
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

                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd HH:mm");
                try {
                    Date date = simpledateformat.parse(cursor.getString(cursor.getColumnIndex(CalendarDatabase.DATE)).toString() + " " + endTime);
                    if(System.currentTimeMillis() > date.getTime()){
                        tEventTitle.setTextColor(R.color.finishedEvent);
                        tEventPlace.setTextColor(R.color.finishedEvent);
                        tStartTime.setTextColor(R.color.finishedEvent);
                        tEndTime.setTextColor(R.color.finishedEvent);
                        imgDote.setImageResource(R.drawable.ic_dote_finish);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

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

    private void showFocusDateLunar(Date date){
        Lunar lunar = new Lunar(date);
        showLunar.setText("农历 " + lunar.animalsYear() + "年 " + lunar.toString());
    }

    private String dateToString(int year, int month, int day){
        return year + DateOperatorUtil.getTwoDigits(month) + DateOperatorUtil.getTwoDigits(day);
    }
}
