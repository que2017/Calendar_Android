package com.demo.zhang.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class EventListAdapter extends BaseAdapter {
    private JSONArray mJSONArray;
    private Context mContext;

    public EventListAdapter(Context context, JSONArray jsonArray){
        mJSONArray = jsonArray;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mJSONArray.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View event, ViewGroup parent) {
        JSONObject temp;

        event = LayoutInflater.from(mContext).inflate(R.layout.events, parent, false);
        ImageView imgDote = (ImageView)event.findViewById(R.id.eventDote);
        TextView tEventTitle = (TextView) event.findViewById(R.id.tEventTitle);
        TextView tEventPlace = (TextView) event.findViewById(R.id.tEventPlace);
        TextView tStartTime = (TextView) event.findViewById(R.id.tStartTime);
        TextView tEndTime = (TextView) event.findViewById(R.id.tEndTime);

        try {
            temp = (JSONObject)mJSONArray.get(position);
            tEventTitle.setText((String)temp.get(CalendarDatabase.EVENT_TITLE));
            tEventPlace.setText((String)temp.get(CalendarDatabase.EVENT_PLACE));
            tStartTime.setText((String)temp.get(CalendarDatabase.START_TIME));
            tEndTime.setText((String)temp.get(CalendarDatabase.END_TIME));
            if(System.currentTimeMillis() > (long)temp.get(CalendarDatabase.DATE)){
                tEventTitle.setTextColor(R.color.finishedEvent);
                tEventPlace.setTextColor(R.color.finishedEvent);
                tStartTime.setTextColor(R.color.finishedEvent);
                tEndTime.setTextColor(R.color.finishedEvent);
                imgDote.setImageResource(R.drawable.ic_dote_finish);
            }

            final Bundle bundle = new Bundle();
            bundle.putString(CalendarDatabase.KEY_ROWID, (String) temp.get(CalendarDatabase.KEY_ROWID));
            bundle.putString(CalendarDatabase.EVENT_TITLE, (String)temp.get(CalendarDatabase.EVENT_TITLE));
            bundle.putString(CalendarDatabase.EVENT_PLACE, (String)temp.get(CalendarDatabase.EVENT_PLACE));
            bundle.putString(CalendarDatabase.START_TIME, (String)temp.get(CalendarDatabase.START_TIME));
            bundle.putString(CalendarDatabase.END_TIME, (String)temp.get(CalendarDatabase.END_TIME));
            event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(mContext, ShowEvent.class);
                    mContext.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return event;
    }
}
