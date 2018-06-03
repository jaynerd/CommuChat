package com.namyoon.commuchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            SmsMessage[] messages;
            String s1;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                SmsMessage smsMessage = messages[0];
                s1 = smsMessage.getOriginatingAddress() + ": " + smsMessage.getMessageBody();

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RECEIVED_ACTION");
                broadcastIntent.putExtra("message", s1);
                context.sendBroadcast(broadcastIntent);
            }


        }

    }
}
