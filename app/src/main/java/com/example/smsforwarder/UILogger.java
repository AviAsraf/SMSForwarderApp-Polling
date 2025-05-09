package com.example.smsforwarder;

import android.widget.TextView;

public class UILogger {
    public static TextView logView;

    public static void log(String message) {
        if (logView != null) {
            logView.post(() -> logView.append(message + "\n"));
        }
    }
}