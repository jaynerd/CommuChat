package com.namyoon.commuchat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ChatActivity extends AppCompatActivity {
    private String userName;
    private String contactNum;
    private String sChat;
    private IntentFilter intentFilter;
    private Button buttonSendMessage;
    private EditText message;
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView displayMsg = (TextView) findViewById(R.id.textViewChat);
            displayMsg.setText(intent.getExtras().getString("message"));
            Toast.makeText(ChatActivity.this, intent.getExtras().getString("message"), Toast.LENGTH_SHORT).show();


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);

        sChat = "";
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(intentReceiver, intentFilter);
        buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
        userName = intent.getExtras().get("user_name").toString();
        contactNum = intent.getExtras().get("target_phone").toString();

        message = (EditText) findViewById(R.id.textViewMessage);

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String sMessage = message.getText().toString();
                sChat += "You: " + sMessage + "\n";
                TextView displayMsg = (TextView) findViewById(R.id.textViewChat);
                displayMsg.setText(sChat);
                sendMessage(contactNum, sMessage);

            }
        });

    }

    protected void sendMessage(String sContactNum, String sMessage) {

        String notifySent = "Message Sent";

        try {

            PendingIntent sentPI = PendingIntent.getBroadcast(ChatActivity.this, 0, new Intent(notifySent), 0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sContactNum, null, sMessage, sentPI, null);

        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Enter a phone number and message", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver((intentReceiver));
        super.onPause();
    }
}
