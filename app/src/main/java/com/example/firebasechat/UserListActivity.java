package com.example.firebasechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firebasechat.Adapters.UserListAdapter;
import com.example.firebasechat.MOdels.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    ArrayList<User> userArrayList;
    User user;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = findViewById(R.id.userListView);

        userArrayList = new ArrayList<>();
        final UserListAdapter userListAdapter = new UserListAdapter(this,userArrayList);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        try{
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        //Toast.makeText(UserListActivity.this, ""+d.child("Name").getValue(), Toast.LENGTH_SHORT).show();
                        String name = d.child("Name").getValue().toString();
                        String email = d.child("Email").getValue().toString();
                        Toast.makeText(UserListActivity.this, ""+name, Toast.LENGTH_SHORT).show();
                        user = new User(name,email);
                        userArrayList.add(user);
                        listView.setAdapter(userListAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){}


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserListActivity.this,ChatActivity.class);
                intent.putExtra("email",userArrayList.get(position).getEmail());
                Toast.makeText(UserListActivity.this, "selected  "+userArrayList.get(position).getEmail(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
