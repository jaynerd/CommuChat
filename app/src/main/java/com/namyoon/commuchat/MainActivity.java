package com.namyoon.commuchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Button startButton;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> userList = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");

    private String userID;
    private String targetID;
    private String targetPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Welcome!");
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("name");

        listView = (ListView) findViewById(R.id.list);
        startButton = (Button) findViewById(R.id.btn_create);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(arrayAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {
                    list.add(((DataSnapshot) i.next()).getKey().toString());
                }
                userList.clear();
                userList.addAll(list);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                targetID = ((TextView) view).getText().toString();
                reference.child(targetID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        targetPhone = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (targetID == null) {
                    Toast.makeText(MainActivity.this, "select a user.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "commuting.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("user_name", userID);
                    intent.putExtra("target_phone", targetPhone);
                    startActivity(intent);
                }
            }
        });
    }
}
