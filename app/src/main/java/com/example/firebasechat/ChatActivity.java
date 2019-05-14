package com.example.firebasechat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {
    Button sendBt;
    EditText msgEt;
    String reciverEmail;
    String sender;
    ArrayList<Integer> arrayList;
    ArrayList<String> msgList;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    ArrayList<String> key;
    int c;
    int cc=0;
    ArrayList<Integer> t=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendBt = findViewById(R.id.sendBt);
        msgEt = findViewById(R.id.editText6);

        arrayList = new ArrayList<>();
        msgList = new ArrayList<>();
        reciverEmail = getIntent().getStringExtra("email");
        sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        listView = findViewById(R.id.chatLv);

        Toast.makeText(this, "r email "+sender, Toast.LENGTH_SHORT).show();
        key = new ArrayList<>();

        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgEt.getText().toString().length()!=0){
                    setup(1);
                }
                else {
                    Toast.makeText(ChatActivity.this, "Empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });
        try{
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, msgList);
        }catch (Exception e){}
        setup(1);

    }

    private void setup(final int k) {
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() == 0) {
                        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Chat").push();
                        databaseReference3.child("Email_1").setValue(reciverEmail);
                        databaseReference3.child("Email_2").setValue(sender);
                    } else {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey());
                            Log.d("set","1");
                            checkingRef(reference, d);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
        }
    }

    private void checkingRef(final DatabaseReference reference, final DataSnapshot d) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    //Log.d()
                    if ((dataSnapshot.child("Email_1").getValue().equals(sender) && dataSnapshot.child("Email_2").getValue().equals(reciverEmail))
                            || (dataSnapshot.child("Email_1").getValue().equals(reciverEmail) && dataSnapshot.child("Email_2").getValue().equals(sender))) {
                        Log.d("Email",sender+"  "+reciverEmail);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey()).push();

                        Toast.makeText(ChatActivity.this, "Found", Toast.LENGTH_SHORT).show();
                        t.add(1);
                        arrayList.add(1);

                        if (msgEt.getText().toString().length()!=0){
                            databaseReference.child("Message").setValue(msgEt.getText().toString());
                            msgEt.setText("");
                        }else {
                            Toast.makeText(ChatActivity.this, "Empty message", Toast.LENGTH_SHORT).show();
                        }
                        c++;
                        cc=0;
                        if (c==1){
                            conversation(FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey()));
                        }
                    }else {
                        arrayList.add(0);
                        c=0;
                        t.add(0);
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(this, "" + arrayList.size(), Toast.LENGTH_SHORT).show();
        if(t.contains(1)){

        }
        else {
            newId();
        }
    }

    private void newId() {
        if (t.contains(0)){
            Toast.makeText(ChatActivity.this, "Not Found else", Toast.LENGTH_SHORT).show();
            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Chat").push();
            databaseReference3.child("Email_1").setValue(reciverEmail);
            databaseReference3.child("Email_2").setValue(sender);

            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Chat").child(databaseReference3.getKey()).push();
            if (msgEt.getText().toString().length()!=0){
                databaseReference4.child("Message").setValue(msgEt.getText().toString());
                msgEt.setText("");
            }else {
                Toast.makeText(this, "Empty message", Toast.LENGTH_SHORT).show();
            }
            t.add(1);
       }
    }

    private void conversation(DatabaseReference databaseReference) {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    Log.d("keyyy",(d.getValue().toString()));
                    msgList.add(d.getValue().toString());
                    listView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*private void redingMsg(final DatabaseReference databaseReference) {
        Toast.makeText(this, "Arraylist size"+msgList.size(), Toast.LENGTH_SHORT).show();
        Log.d("msg",databaseReference.getKey());
       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("reading","1");
                String message = (String) dataSnapshot.child("Message").getValue();
                msgList.add(message);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(ChatActivity.this, "Hiii", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
