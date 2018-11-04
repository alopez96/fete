package com.example.arturolopez.fete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyPartiesActivity extends AppCompatActivity {

    private static final String TAG = "MyPartiesActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef, mspecificUserRef;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parties);

        getMyParties();
    }

    private void getMyParties(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            //user is signed in
            uid = user.getUid();
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUserRef.child(uid);
        Log.d(TAG,"uid: " + uid);
        mspecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
