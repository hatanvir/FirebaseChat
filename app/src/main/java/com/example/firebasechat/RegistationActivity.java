package com.example.firebasechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistationActivity extends AppCompatActivity {
    EditText nameEt,emailEt,passEt;
    Button signUpBt;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);

        nameEt = findViewById(R.id.editText3);
        emailEt = findViewById(R.id.editText4);
        passEt = findViewById(R.id.editText5);

        signUpBt = findViewById(R.id.signUpBt);

        firebaseAuth = FirebaseAuth.getInstance();


        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(emailEt.getText().toString(),passEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").push();
                            databaseReference.child("Name").setValue(nameEt.getText().toString());
                            databaseReference.child("Email").setValue(emailEt.getText().toString());
                            databaseReference.child("Password").setValue(passEt.getText().toString());
                            Intent intent = new Intent(RegistationActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(RegistationActivity.this, "Failed to register user  "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
