package com.demo.zhang.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.zhang.util.CalendarUtil;
import com.demo.zhang.util.ConstantUtil;
import com.demo.zhang.util.DateOperatorUtil;

import java.util.Calendar;

public class ShowEvent extends Activity implements View.OnClickListener {
    private Button showEventBack;
    private Button editEvent;
    private Button deleteEvent;
    private TextView showTitle;
    private TextView showPlace;
    private TextView showTime;

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_event);

        showEventBack = (Button)findViewById(R.id.showEventBack);
        editEvent = (Button)findViewById(R.id.editEvent);
        deleteEvent = (Button)findViewById(R.id.deleteEvent);
        showEventBack = (Button)findViewById(R.id.showEventBack);
        showTitle = (TextView)findViewById(R.id.tShowTitle);
        showPlace = (TextView)findViewById(R.id.tShowPlace);
        showTime = (TextView)findViewById(R.id.tShowTime);

        bundle = this.getIntent().getExtras();
        showTitle.setText((String)bundle.get(ConstantUtil.EVENT_TITLE));
        showPlace.setText((String)bundle.get(ConstantUtil.EVENT_PLACE));
//        showTime.setText((String)bundle.get(CalendarDatabase.START_TIME) + " - " + (String)bundle.get(CalendarDatabase.END_TIME));
        showTime.setText(DateOperatorUtil.getShowTime((long)bundle.get(ConstantUtil.START_TIME)) + "\n"
                + DateOperatorUtil.getShowTime((long)bundle.get(ConstantUtil.END_TIME)));

        showEventBack.setOnClickListener(this);
        editEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.showEventBack:
                this.finish();
                break;
            case R.id.editEvent:
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(this, ModifyEvent.class);
                this.startActivity(intent);
                this.finish();
                break;
            case R.id.deleteEvent:
//                CalendarDatabase cd = new CalendarDatabase(this);
//                cd.open();
//                cd.delete_Event((String)bundle.get(ConstantUtil.KEY_ROWID));
//                cd.close();
                CalendarUtil.deleteGoogleCalendar(this, (long) bundle.get(ConstantUtil.KEY_ROWID));
                this.finish();
                break;
        }
    }
}
