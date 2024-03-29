package com.ciprian.eventcalendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.MONTH;

public class CreateNewEvent extends AppCompatActivity {

    Button btnAddEventInDB,etDateEvent, btnTime ;
    EditText etNameEvent,  etLocationEvent, etMember;
    Calendar mycalendar = Calendar.getInstance();

    private final int day = mycalendar.get(Calendar.DAY_OF_MONTH);
    private final int month = mycalendar.get(Calendar.MONTH);
    private final int year = mycalendar.get(Calendar.YEAR);

    public static final int requestCodeForShowMembers = 999;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == requestCodeForShowMembers)
        {
            if(resultCode == RESULT_OK)
            {
                etMember.setText(data.getStringExtra("memberName"));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        btnAddEventInDB = findViewById(R.id.btnAddEventInDB);
        etNameEvent = findViewById(R.id.etNameEvent);
        etDateEvent = findViewById(R.id.etDateEvent);
        etLocationEvent = findViewById(R.id.etLocationEvent);
        btnTime = findViewById(R.id.btnTime);
        etMember = findViewById(R.id.etMember);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        tvLoad = findViewById(R.id.tvLoad);

        if(!MyApplication.admin)
        {
            etMember.setVisibility(View.GONE);
        }
        if(MyApplication.admin)
        {
            etMember.setVisibility(View.VISIBLE);
        }

        etMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateNewEvent.this,
                        com.ciprian.eventcalendar.ShowMembers.class);
                startActivityForResult(intent, requestCodeForShowMembers);

            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateNewEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        btnTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        etDateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }

                    void DateDialog() {
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mycalendar.set(MONTH, monthOfYear);
                                etDateEvent.setText(dayOfMonth + "-" + mycalendar.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault()) + "-" + year);
                            }
                        };

                        DatePickerDialog dpDialog = new DatePickerDialog(CreateNewEvent.this, listener, year, month, day);
                        dpDialog.show();
                    }
            });



        btnAddEventInDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etDateEvent.getText().toString().isEmpty() || etNameEvent.getText().toString().isEmpty() || etLocationEvent.getText().toString().isEmpty()
                || btnTime.getText().toString().isEmpty())
                {
                    Toast.makeText(CreateNewEvent.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showProgress(true);

                    String timeEvent = btnTime.getText().toString().trim();
                    String nameEvent = etNameEvent.getText().toString().trim();
                    String dateEvent = etDateEvent.getText().toString().trim();
                    String locationEvent = etLocationEvent.getText().toString().trim();
                    String memberName = etMember.getText().toString().trim();
                    int monthValue = mycalendar.get(MONTH);

                    Event event = new Event();

                    event.setMonthValue(monthValue);
                    event.setTimeEvent(timeEvent);
                    event.setDateEvent(dateEvent);
                    event.setLocationEvent(locationEvent);
                    event.setNameEvent(nameEvent);
                    event.setMemberEvent(memberName);

                    Backendless.Persistence.save(event, new AsyncCallback<Event>() {
                        @Override
                        public void handleResponse(Event response) {
                            Toast.makeText(CreateNewEvent.this, "Successfully created a new event", Toast.LENGTH_SHORT).show();
                            CreateNewEvent.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(CreateNewEvent.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
