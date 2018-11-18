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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener{
    private final String TAG = MainActivity.class.getSimpleName();

    private TextView showLunar;
    private Button bAdd;
    private ListView eventContainer;
    private CalendarView calendarView;
    private Calendar calendar;

    private EventListAdapter eventListAdapter;
    private String currDate;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showLunar = (TextView)findViewById(R.id.showLunar);
        bAdd = (Button)findViewById(R.id.bAddEvents);
        eventContainer = (ListView)findViewById(R.id.eventContainer);
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

    private void showFocusDateEvents() {
        eventContainer.setAdapter(null);
        CalendarDatabase cd = new CalendarDatabase(this);
        cd.open();
        Cursor cursor = cd.search_Event(currDate);
        JSONArray jsonArray = new JSONArray();
        if(cursor.moveToFirst()){
            do{
                JSONObject jsonObject = new JSONObject();

                String id = cursor.getString(cursor.getColumnIndex(CalendarDatabase.KEY_ROWID));
                String eventTitle = cursor.getString(cursor.getColumnIndex(CalendarDatabase.EVENT_TITLE));
                String eventPlace = cursor.getString(cursor.getColumnIndex(CalendarDatabase.EVENT_PLACE));
                String startTime = cursor.getString(cursor.getColumnIndex(CalendarDatabase.START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(CalendarDatabase.END_TIME));

                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd HH:mm");
                try {
                    Date date = simpledateformat.parse(cursor.getString(cursor.getColumnIndex(CalendarDatabase.DATE)).toString() + " " + endTime);
                    jsonObject.put(CalendarDatabase.KEY_ROWID, id);
                    jsonObject.put(CalendarDatabase.EVENT_TITLE, eventTitle);
                    jsonObject.put(CalendarDatabase.EVENT_PLACE, eventPlace);
                    jsonObject.put(CalendarDatabase.START_TIME, startTime);
                    jsonObject.put(CalendarDatabase.END_TIME, endTime);
                    jsonObject.put(CalendarDatabase.DATE, date.getTime());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }while(cursor.moveToNext());
            eventListAdapter = new EventListAdapter(MainActivity.this, jsonArray);
            eventContainer.setAdapter(eventListAdapter);
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
