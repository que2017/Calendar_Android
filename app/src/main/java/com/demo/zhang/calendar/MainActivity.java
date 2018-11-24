package com.demo.zhang.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.zhang.util.CalendarUtil;
import com.demo.zhang.util.ConstantUtil;
import com.demo.zhang.util.DateOperatorUtil;

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
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

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
        checkCalendarReadPermission();
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
                bundle.putLong(ConstantUtil.DATE, (date.getTime() / ConstantUtil.ONE_DAY) * ConstantUtil.ONE_DAY);
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case ConstantUtil.CALENDAR_READ_PERMISSION:
                if(grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请开启日历读取权限！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void showFocusDateEvents() {
        eventContainer.setAdapter(null);
//        CalendarDatabase cd = new CalendarDatabase(this);
//        cd.open();
//        Cursor cursor = cd.search_Event(date.getTime(), date.getTime() + ONE_DAY);
        Cursor cursor = CalendarUtil.queryGoogleCalendar(this, date.getTime(), date.getTime() + ConstantUtil.ONE_DAY);
        if (null == cursor) {
            return;
        }
        JSONArray jsonArray = new JSONArray();
        try {
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();

                    long id = cursor.getLong(cursor.getColumnIndex(ConstantUtil._ID));
                    String eventTitle = cursor.getString(cursor.getColumnIndex(ConstantUtil.TITLE));
                    String eventPlace = cursor.getString(cursor.getColumnIndex(ConstantUtil.EVENT_LOCATION));
                    long startTime = cursor.getLong(cursor.getColumnIndex(ConstantUtil.DSTSRT));
                    long endTime = cursor.getLong(cursor.getColumnIndex(ConstantUtil.DEND));

//                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd HH:mm");

//                    long eventEndTime = cursor.getLong(cursor.getColumnIndex(ConstantUtil.DEND));
                    jsonObject.put(ConstantUtil.KEY_ROWID, id);
                    jsonObject.put(ConstantUtil.EVENT_TITLE, eventTitle);
                    jsonObject.put(ConstantUtil.EVENT_PLACE, eventPlace);
                    jsonObject.put(ConstantUtil.START_TIME, startTime);
                    jsonObject.put(ConstantUtil.END_TIME, endTime);
                    jsonObject.put(ConstantUtil.DATE, simpleDateFormat.parse(currDate).getTime());
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        eventListAdapter = new EventListAdapter(MainActivity.this, jsonArray);
        eventContainer.setAdapter(eventListAdapter);
//        cd.close();
    }

    private void showFocusDateLunar(Date date){
        Lunar lunar = new Lunar(date);
        showLunar.setText("农历 " + lunar.animalsYear() + "年 " + lunar.toString());
    }

    private String dateToString(int year, int month, int day){
        return year + DateOperatorUtil.getTwoDigits(month) + DateOperatorUtil.getTwoDigits(day);
    }

    public void checkCalendarReadPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, ConstantUtil.CALENDAR_READ_PERMISSION);
        }
    }
}
