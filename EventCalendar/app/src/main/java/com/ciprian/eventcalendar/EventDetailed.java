package com.ciprian.eventcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.MONTH;

public class EventDetailed extends AppCompatActivity {

    TextView tvShowEventDetailedFromListView, etShowEventDetailLocation;
    Button btnDate;
    Calendar mycalendar = Calendar.getInstance();
    private final int day = mycalendar.get(Calendar.DAY_OF_MONTH);
    private final int month = mycalendar.get(MONTH);
    private final int year = mycalendar.get(Calendar.YEAR);

    MenuItem saveEvent, editEvent, deleteEvent;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        saveEvent = menu.findItem(R.id.saveEvent);
        editEvent = menu.findItem(R.id.editEvent);
        deleteEvent = menu.findItem(R.id.deleteEvent);

        deleteEvent.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_delete_round));
        editEvent.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_edit_round));
        saveEvent.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_save_round));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        final String whereClause = "objectId = '" + MyApplication.objectId + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        switch (item.getItemId()) {

            case R.id.saveEvent:
                if (MyApplication.editPressed) {
                    showProgress(true);
                    Event event = new Event();
                    if (tvShowEventDetailedFromListView.getText().toString().isEmpty() || btnDate.getText().toString().isEmpty()
                            || etShowEventDetailLocation.getText().toString().isEmpty()) {
                        Toast.makeText(EventDetailed.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    } else {

                        event.setNameEvent(tvShowEventDetailedFromListView.getText().toString().trim());
                        event.setDateEvent(btnDate.getText().toString().trim());
                        event.setLocationEvent(etShowEventDetailLocation.getText().toString().trim());

                        Backendless.Persistence.of(Event.class).remove(whereClause, new AsyncCallback<Integer>() {
                            @Override
                            public void handleResponse(Integer response) {

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(EventDetailed.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        Backendless.Persistence.of(Event.class).save(event, new AsyncCallback<Event>() {
                            @Override
                            public void handleResponse(Event response) {
                                EventDetailed.this.finish();
                                Toast.makeText(EventDetailed.this, "Event modified", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(EventDetailed.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        MyApplication.editPressed = false;

                    }
                } else {
                    Toast.makeText(this, "Nothing was modified", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.editEvent:
                MyApplication.editPressed = true;
                tvShowEventDetailedFromListView.setEnabled(true);
                etShowEventDetailLocation.setEnabled(true);
                btnDate.setEnabled(true);
                break;

            case R.id.deleteEvent:

                AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailed.this);
                builder.setMessage("Do you want to remove this event?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgress(true);

                        Backendless.Persistence.of(Event.class).remove(whereClause, new AsyncCallback<Integer>() {
                            @Override
                            public void handleResponse(Integer response) {
                                Toast.makeText(EventDetailed.this, "Event removed", Toast.LENGTH_SHORT).show();
                                EventDetailed.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(EventDetailed.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detailed);

        tvShowEventDetailedFromListView = findViewById(R.id.tvShowEventDetailedFromListView);
        etShowEventDetailLocation = findViewById(R.id.etShowEventDetailLocation);
        btnDate = findViewById(R.id.btnDate);

        tvShowEventDetailedFromListView.setEnabled(false);
        etShowEventDetailLocation.setEnabled(false);
        btnDate.setEnabled(false);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        tvLoad = findViewById(R.id.tvLoad);

        final String whereClause = "objectId = '" + MyApplication.objectId + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        showProgress(true);

        Backendless.Persistence.of(Event.class).find(queryBuilder, new AsyncCallback<List<Event>>() {
            @Override
            public void handleResponse(List<Event> response) {
                List<Event> events;
                events = response;
                tvShowEventDetailedFromListView.setText(events.get(0).getNameEvent());
                etShowEventDetailLocation.setText(events.get(0).getLocationEvent());
                btnDate.setText(events.get(0).getDateEvent());

                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(EventDetailed.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }

            void DateDialog() {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mycalendar.set(MONTH, month);
                        btnDate.setText(day + "-" + mycalendar.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault()) + "-" + year);
                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(EventDetailed.this, listener, year, month, day);
                dpDialog.show();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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
    }
}
