package com.example.smsforwarder;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private final Handler handler = new Handler();
    private int countdownSeconds = 15;
    private Runnable pollingRunnable;

    private TextView countdownView;
    private EditText emailField, passwordField;
    private Button saveButton, editButton;


    private void startPolling() {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdownSeconds <= 0) {
                    SmsPoller.checkInbox(
                            MainActivity.this,
                            emailField.getText().toString(),
                            passwordField.getText().toString()
                    );
                    countdownSeconds = 15;
                } else {
                    countdownSeconds--;
                }
                countdownView.setText("Next check in: " + countdownSeconds + "s");
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(pollingRunnable);
    }

    private void stopPolling() {
        handler.removeCallbacksAndMessages(null);
        countdownView.setText("Polling paused.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("sms_forwarder", MODE_PRIVATE);

        UILogger.logView = findViewById(R.id.logView);
        UILogger.log("App launched");

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        countdownView = findViewById(R.id.countdownView);
        saveButton = findViewById(R.id.saveButton);
        editButton = findViewById(R.id.editButton);

        // Link to Gmail app password page
        findViewById(R.id.passwordLink).setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://myaccount.google.com/apppasswords")));
        });

        // Load saved values
        String savedEmail = prefs.getString("email", "");
        String savedPassword = prefs.getString("password", "");
        emailField.setText(savedEmail);
        passwordField.setText(savedPassword);

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            emailField.setEnabled(false);
            passwordField.setEnabled(false);
            UILogger.log("📦 Loaded credentials");
            startPolling();
        }

        saveButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                prefs.edit()
                        .putString("email", email)
                        .putString("password", password)
                        .apply();

                emailField.setEnabled(false);
                passwordField.setEnabled(false);
                UILogger.log("✅ Credentials saved");
                startPolling();
            } else {
                UILogger.log("❌ Email or password is empty");
            }
        });

        editButton.setOnClickListener(v -> {
            stopPolling();
            emailField.setEnabled(true);
            passwordField.setEnabled(true);
            UILogger.log("✏️ Edit mode enabled");
        });

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.INTERNET
        }, PERMISSION_REQUEST_CODE);
    }
}
