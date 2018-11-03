package com.example.arturolopez.fete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SpecificEventActivity extends AppCompatActivity {

    private static final String TAG = "SpecificEventActivity";

    private ImageView imageView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPartyRef, mspecifiPartyRef;

    private Party thisParty;
    private String name, date, hostname, price, address, desc, imageurl, partyid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_event);

        imageView = findViewById(R.id.image_view);

        partyid = getIntent().getStringExtra("partyid");
        Log.d(TAG, partyid);

        getImage();
    }

    private void getImage(){
        //get adress and date of parties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPartyRef = mFirebaseDatabase.getReference().child("parties");
        mspecifiPartyRef = mPartyRef.child(partyid);
        mspecifiPartyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisParty = dataSnapshot.getValue(Party.class);
                if(thisParty.partyid != null){
                    Log.d(TAG, thisParty.partyid);
                    imageurl = thisParty.imageUrl;
                }
                Picasso.get().load(imageurl).into(imageView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
