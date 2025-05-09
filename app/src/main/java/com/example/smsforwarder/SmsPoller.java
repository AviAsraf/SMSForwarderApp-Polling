package com.example.smsforwarder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

public class SmsPoller {
    private static long lastMessageId = -1;

    public static void checkInbox(Context context, String gmailAddress, String appPassword) {
        try {
            Uri inboxUri = Telephony.Sms.Inbox.CONTENT_URI;
            Cursor cursor = context.getContentResolver().query(
                    inboxUri,
                    null,
                    null,
                    null,
                    Telephony.Sms.DEFAULT_SORT_ORDER
            );

            if (cursor != null && cursor.moveToFirst()) {
                long messageId = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms._ID));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                long timestampMillis = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
                String receivedTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new java.util.Date(timestampMillis));

                if (messageId > lastMessageId) {
                    lastMessageId = messageId;

                    UILogger.log("📩 New SMS from " + address + ": " + body);

                    String emailBody = "📩 Message from: " + address + "\n"
                            + "🕒 Received at: " + receivedTime + "\n\n"
                            + body;

                    new Thread(() -> {
                        try {
                            GMailSender sender = new GMailSender(gmailAddress, appPassword);
                            sender.sendMail("SMS from " + address, emailBody, gmailAddress, gmailAddress);
                            UILogger.log("✅ Email sent");
                        } catch (Exception e) {
                            UILogger.log("❌ Email error: " + e.getMessage());
                            Log.e("SmsPoller", "Email error", e);
                        }
                    }).start();
                }

                cursor.close();
            }
        } catch (Exception e) {
            UILogger.log("❌ Polling failed: " + e.getMessage());
            Log.e("SmsPoller", "Polling failed", e);
        }
    }
}
