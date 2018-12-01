package com.example.arturolopez.fete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

    private ImageView Selfie;
    private TextView Name;
    private Button AddButton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef, mspecificUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Selfie = findViewById(R.id.selfie_view);
        Name = findViewById(R.id.name_tv);
        AddButton = findViewById(R.id.add_btn);

        uid = getIntent().getStringExtra("uid");
        Log.d(TAG,"uid " + uid);


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
                if(dataSnapshot.child("name").getValue() != null){
                    name = dataSnapshot.child("name").getValue().toString();
                    Name.setText(name);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
