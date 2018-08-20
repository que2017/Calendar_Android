package com.demo.zhang.calendar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bAdd;
    private LinearLayout eventContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAdd = (Button)findViewById(R.id.bAddEvents);
        eventContainer = (LinearLayout)findViewById(R.id.eventContainer);

        bAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bAddEvents:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddEvent.class);
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
}
