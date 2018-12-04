package com.example.arturolopez.fete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private String uid;
    private String imageUrl;
    private String name;
    private String bio;

    private ImageView Selfie;
    private TextView NameText;
    private TextView BioText;
    private Button AddButton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef, mspecificUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Selfie = findViewById(R.id.selfie_view);
        NameText = findViewById(R.id.name_tv);
        BioText = findViewById(R.id.bio_tv);
        AddButton = findViewById(R.id.add_btn);

        uid = getIntent().getStringExtra("uid");
        Log.d(TAG,"uid " + uid);


        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    getUserData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    private void getUserData(){
        //get adress and date of parties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUserRef.child(uid);
        mspecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("img").getValue() != null){
                    imageUrl = dataSnapshot.child("img").getValue().toString();
                    if(!imageUrl.isEmpty()){
                        Picasso.get().load(imageUrl).into(Selfie);
                    }
                    else{
                        Selfie.setVisibility(View.INVISIBLE);
                    }
                }
                if(dataSnapshot.child("username").getValue() != null){
                    name = dataSnapshot.child("username").getValue().toString();
                    NameText.setText(name);
                }
                if(dataSnapshot.child("bio").getValue() != null){
                    bio = dataSnapshot.child("bio").getValue().toString();
                    BioText.setText(bio);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void sendRequest(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String thisuid = "";
        if(user != null) {
            //user is signed in
            thisuid = user.getUid();
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUserRef.child(uid);
        mspecificUserRef.child("friends").child(thisuid).setValue("true");
        Toast.makeText(this, "Now following!",Toast.LENGTH_SHORT).show();
        Intent a = new Intent(ProfileActivity.this, AllUsersActivity.class);
        startActivity(a);
    }
}
