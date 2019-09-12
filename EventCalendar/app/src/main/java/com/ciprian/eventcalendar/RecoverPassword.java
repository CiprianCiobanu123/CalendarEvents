package com.ciprian.eventcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RecoverPassword extends AppCompatActivity {

    EditText etRecoveryMail;
    Button btnRecoveryMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        btnRecoveryMail = findViewById(R.id.btnRecoveryMail);
        etRecoveryMail = findViewById(R.id.etRecoveryMail);

        btnRecoveryMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etRecoveryMail.getText().toString().isEmpty())
                {
                    Toast.makeText(RecoverPassword.this, "Please enter your email adress ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email = etRecoveryMail.getText().toString().trim();
                    Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            Toast.makeText(RecoverPassword.this, "Please verify your mail", Toast.LENGTH_SHORT).show();
                            RecoverPassword.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(RecoverPassword.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
