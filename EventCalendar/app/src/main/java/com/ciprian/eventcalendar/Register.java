package com.ciprian.eventcalendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import static android.view.View.GONE;

public class Register extends AppCompatActivity {

    EditText etName, etPassword, etPasswordRepeat, etPasswordAdmin;
    Button btnRegister;
    CheckBox cbAdmin;
    TextView tvMail;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private String isAdminText = "";
    private boolean requiresAdminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etPasswordRepeat = findViewById(R.id.etPasswordRepeat);
        tvMail = findViewById(R.id.tvMail);
        btnRegister = findViewById(R.id.btnRegister);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        tvLoad = findViewById(R.id.tvLoad);
        cbAdmin = findViewById(R.id.cbAdmin);
        etPasswordAdmin = findViewById(R.id.etPasswordAdmin);

        etPasswordAdmin.setVisibility(GONE);

        cbAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPasswordAdmin.getVisibility() == View.VISIBLE) {
                    etPasswordAdmin.setVisibility(GONE);
                    requiresAdminPassword = false;
                } else {
                    etPasswordAdmin.setVisibility(View.VISIBLE);
                    requiresAdminPassword = true;
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etPasswordRepeat.getText().toString().isEmpty() ||
                        tvMail.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (etPassword.getText().toString().trim().equals(etPasswordRepeat.getText().toString().trim())) {

                        showProgress(true);

                        String name = etName.getText().toString().trim();
                        String email = tvMail.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();
                        boolean isAdmin = false;

                        if (requiresAdminPassword) {
                            if (etPasswordAdmin.getText().toString().isEmpty()) {
                                Toast.makeText(Register.this, "Please enter a 6 digit password", Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            } else {
                                if (etPasswordAdmin.getText().toString().trim().equals("123456")) {
                                    Toast.makeText(Register.this, "You are an admin", Toast.LENGTH_SHORT).show();
                                    isAdmin = true;
                                    BackendlessUser user = new BackendlessUser();
                                    user.setProperty("admin", isAdmin);
                                    user.setEmail(email);
                                    user.setPassword(password);
                                    user.setProperty("name", name);

                                    final Member member = new Member();
                                    member.setName(name);

                                    Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                        @Override
                                        public void handleResponse(BackendlessUser response) {
                                            Toast.makeText(Register.this, "New user registered", Toast.LENGTH_SHORT).show();
                                            Backendless.Persistence.save(member, new AsyncCallback<Member>() {
                                                @Override
                                                public void handleResponse(Member response) {
                                                    Toast.makeText(Register.this, "Member saved", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(Register.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            Register.this.finish();
                                            startActivity(new Intent(Register.this, Login.class));
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(Register.this, "Error:" + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                    });
                                } else {
                                    Toast.makeText(Register.this, "6 digit code incorrect", Toast.LENGTH_SHORT).show();
                                    showProgress(false);
                                }
                            }
                        } else {
                            BackendlessUser user = new BackendlessUser();
                            user.setProperty("admin", isAdmin);
                            user.setEmail(email);
                            user.setPassword(password);
                            user.setProperty("name", name);

                            final Member member = new Member();
                            member.setName(name);

                            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {
                                    Toast.makeText(Register.this, "New user registered", Toast.LENGTH_SHORT).show();
                                    Backendless.Persistence.save(member, new AsyncCallback<Member>() {
                                        @Override
                                        public void handleResponse(Member response) {
                                            Toast.makeText(Register.this, "Member saved", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(Register.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Register.this.finish();
                                    startActivity(new Intent(Register.this, Login.class));
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(Register.this, "Error:" + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    showProgress(false);
                                }
                            });

                        }


                    } else {
                        Toast.makeText(Register.this, "Passwords are different", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : GONE);
                }
            });
            tvLoad.setVisibility(show ? View.VISIBLE : GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : GONE);
                }
            });
        } else {
// The ViewPropertyAnimator APIs are not available, so simply show
// and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : GONE);
            mLoginFormView.setVisibility(show ? GONE : View.VISIBLE);
        }
    }
}
