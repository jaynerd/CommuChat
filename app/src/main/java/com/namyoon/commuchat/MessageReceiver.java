package com.namyoon.commuchat;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            SmsMessage[] messages;
            String s1;

            messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            SmsMessage smsMessage = messages[0];
            s1 = smsMessage.getOriginatingAddress() + ": " + smsMessage.getMessageBody();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("android.provider.Telephony.SMS_RECEIVED");
            broadcastIntent.putExtra("message", s1);
            context.sendBroadcast(broadcastIntent);

        }
        Toast.makeText(context, "RECEIVED", Toast.LENGTH_LONG).show();
    }
}
