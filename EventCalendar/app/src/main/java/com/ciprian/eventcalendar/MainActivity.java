package com.ciprian.eventcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {

    Button btnCreateNewEvent, btnPassedEvents, btnShowAllEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPassedEvents = findViewById(R.id.btnPassedEvents);
        btnCreateNewEvent = findViewById(R.id.btnCreateNewEvent);
        btnShowAllEvents = findViewById(R.id.btnShowAllEvents);

        btnCreateNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateNewEvent.class));
            }
        });

        btnShowAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShowEvents.class));
            }
        });

    }
}
