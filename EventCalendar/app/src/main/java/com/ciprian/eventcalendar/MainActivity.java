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
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.push.DeviceRegistrationResult;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnCreateNewEvent, btnPassedEvents, btnShowAllEvents, btnTestNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        btnPassedEvents = findViewById(R.id.btnPassedEvents);
        btnCreateNewEvent = findViewById(R.id.btnCreateNewEvent);
        btnShowAllEvents = findViewById(R.id.btnShowAllEvents);
        btnTestNotification = findViewById(R.id.btnTestNotification);

        btnTestNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Backendless.Messaging.pushWithTemplate("user", new AsyncCallback<MessageStatus>() {
                    @Override
                    public void handleResponse(MessageStatus response) {
                        Toast.makeText(MainActivity.this, "sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(MainActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        List<String> channels = new ArrayList<>();
        channels.add("default");
        Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
            @Override
            public void handleResponse(DeviceRegistrationResult response) {
                Toast.makeText(MainActivity.this, "Device registered!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this, "Error registering " + fault.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

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
