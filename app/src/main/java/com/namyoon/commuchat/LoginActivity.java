package com.namyoon.commuchat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;

    private EditText idText;
    private EditText phoneText;

    private String userID;
    private String phoneNumber;

    private boolean isLoginClicked = false;

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.btn_login);
        registerButton = (Button) findViewById(R.id.btn_rgst);

        idText = (EditText) findViewById(R.id.et_id);
        phoneText = (EditText) findViewById(R.id.et_ph);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginClicked = true;
                phoneNumber = phoneText.getText().toString();
                userID = idText.getText().toString();
                if (isLoginClicked) {
                    userRef.child(phoneNumber).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userPhone = (String) dataSnapshot.getValue();
                            if (userPhone == null) {
                                Toast.makeText(LoginActivity.this, "no account found. register first.", Toast.LENGTH_SHORT).show();
                            } else if (isLoginClicked) {
                                userRef.child(phoneNumber).setValue(userID);
                                Toast.makeText(LoginActivity.this, "logged in.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginClicked = false;
                final boolean[] flag = {true};
                phoneNumber = phoneText.getText().toString();
                userID = idText.getText().toString();
                userRef.child(phoneNumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userPhone = (String) dataSnapshot.getValue();
                        if (userPhone == null) {
                            flag[0] = false;
                            userRef.child(phoneNumber).setValue(userID);
                            Toast.makeText(LoginActivity.this, "registered successfully.", Toast.LENGTH_SHORT).show();
                        }
                        if (flag[0] == true) {
                            Toast.makeText(LoginActivity.this, "you already have an account.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
