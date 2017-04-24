package com.crocias.firegroupchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Chat_Room extends AppCompatActivity {
    TextView tvChat;
    Button bSend;
    EditText etChat;
    String user_name, room_name;
    DatabaseReference root;
    String temp_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room);
        tvChat = (TextView) findViewById(R.id.tvChat);
        bSend = (Button) findViewById(R.id.bSend);
        etChat = (EditText) findViewById(R.id.etChat);

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();

        setTitle(room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object>  map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg", etChat.getText().toString());

                message_root.updateChildren(map2);


            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chat_msg, chat_user_name;
    private void append(DataSnapshot datasnapshot){
        Iterator i = datasnapshot.getChildren().iterator();
        while (i.hasNext()){
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            tvChat.append(chat_user_name+" : "+chat_msg+"\n");
        }

    }

}
