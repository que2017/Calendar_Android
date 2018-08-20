package com.demo.zhang.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEvent extends Activity implements View.OnClickListener {
    private Button bQuit;
    private Button bCorrect;
    private EditText eventTitle;
    private EditText eventPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        bQuit = (Button)findViewById(R.id.eventQuit);
        bCorrect = (Button)findViewById(R.id.eventCorrect);
        eventTitle = (EditText)findViewById(R.id.eventTitle);
        eventPlace = (EditText)findViewById(R.id.eventPlace);

        bQuit.setOnClickListener(this);
        bCorrect.setOnClickListener(this);
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
        }
    }

    private void addEventToTable() {
    }
}
