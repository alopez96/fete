package com.example.arturolopez.fete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountPage extends AppCompatActivity {

    private EditText nameText;
    private EditText emailText;
    private EditText numberText;
    private EditText bioText;
    private Button submitButton;

    private String username, email, number, bio;
    private String uid, key;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;             //entry point for our app to access the database
    private DatabaseReference mUsersReference;
    private DatabaseReference mspecificUserRef;
    private MyUser thisUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        nameText = findViewById(R.id.name_tv);
        emailText = findViewById(R.id.email_tv);
        numberText = findViewById(R.id.phone_tv);
        bioText = findViewById(R.id.bio_tv);
        submitButton = findViewById(R.id.submit_tv);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            submitChanges();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        prefillInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefillInfo();
    }


    public void prefillInfo(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            //get userid and email
            email = user.getEmail();
            uid = user.getUid();
            //fill in emailText
            emailText.setText(email);
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUsersReference.child(uid);
        mspecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisUser = dataSnapshot.getValue(MyUser.class);
                if(thisUser.username != null){
                    nameText.setText(thisUser.username);
                }
                if(thisUser.phone != null){
                    numberText.setText(thisUser.phone);
                }
                if(thisUser.bio != null) {
                    bioText.setText(thisUser.bio);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void submitChanges(){
        username = nameText.getText().toString();
        number = numberText.getText().toString();
        bio = bioText.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            //user is signed in
            uid = user.getUid();
            //get email, to make sure it doesn't change
            email = emailText.getText().toString();
            //push user to Firebase
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUsersReference = mFirebaseDatabase.getReference().child("users");
            mspecificUserRef = mUsersReference.child(uid);
            key = mspecificUserRef.getKey();
            thisUser = new MyUser(username, email, number, bio, uid, key);
            mspecificUserRef.setValue(thisUser);
            //confirm update to user
            Toast.makeText(AccountPage.this, "account updated",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}
