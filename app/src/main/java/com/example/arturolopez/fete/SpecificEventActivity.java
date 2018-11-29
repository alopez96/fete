package com.example.arturolopez.fete;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturolopez.fete.Utils.FullImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpecificEventActivity extends AppCompatActivity {

    private static final String TAG = "SpecificEventActivity";

    private ImageView imageView;
    private TextView nameTV;
    private TextView dateTV;
    private TextView addressTV;
    private TextView priceTV;
    private TextView descTV;
    private Button joinButton;
    private Button leaveButton;
    private Button payButton;
    private Button chatButton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPartyRef, mspecificPartyRef;

    private DatabaseReference mUserRef, mspecificUserRef;

    private Party thisParty;
    private String name, date, hostname, price, address, desc, imageurl, partyid;

    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_event);

        imageView = findViewById(R.id.image_view);
        nameTV = findViewById(R.id.name_tv);
        dateTV = findViewById(R.id.date_tv);
        addressTV = findViewById(R.id.address_tv);
        priceTV = findViewById(R.id.price_tv);
        descTV  = findViewById(R.id.desc_tv);
        joinButton = findViewById(R.id.join_btn);
        leaveButton = findViewById(R.id.leave_btn);
        payButton = findViewById(R.id.payment_btn);
        chatButton = findViewById(R.id.chat_btn);

        String partyBoolean;
        boolean partyJoinedTrue;
        partyid = getIntent().getStringExtra("partyid");
        partyBoolean = getIntent().getStringExtra("partyBoolean");
        Log.d(TAG,"partyid " + partyid);
        Log.d(TAG,"partyBoolean " + partyBoolean);

        partyJoinedTrue = false;
        if(partyBoolean != null){
            partyJoinedTrue = Objects.equals("true", partyBoolean);
            if(partyJoinedTrue){
                Log.d(TAG,"you have joined this party.");
                joinButton.setVisibility(View.GONE);
            }
            else{
                Log.d(TAG,"you have NOT joined this party.");
                leaveButton.setVisibility(View.GONE);
                chatButton.setVisibility(View.GONE);
            }
        }

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SpecificEventActivity.this, ChatActivity.class);
                Log.d(TAG, partyid);
                if(partyid != null){
                    i.putExtra("paryid", partyid);
                    startActivity(i);
                }
            }
        });

        getPartyInfo();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SpecificEventActivity.this, FullImageView.class);
                i.putExtra("url", imageurl);
                i.putExtra("type", "image");
                startActivity(i);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParty();
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    //user is signed in
                    uid = user.getUid();
                }
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mUserRef = mFirebaseDatabase.getReference().child("users");
                mspecificUserRef = mUserRef.child(uid);
                Log.d(TAG,"uid2: " + uid);
                Log.d(TAG,"partyid2: " + partyid);
                mspecificUserRef.child("parties").child(partyid).removeValue();
                Toast.makeText(SpecificEventActivity.this, "You have left party!",Toast.LENGTH_SHORT).show();
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SpecificEventActivity.this, PaymentActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    private void getPartyInfo(){
        //get adress and date of parties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPartyRef = mFirebaseDatabase.getReference().child("parties");
        mspecificPartyRef = mPartyRef.child(partyid);
        mspecificPartyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisParty = dataSnapshot.getValue(Party.class);
                if(thisParty.partyid != null){
                    imageurl = thisParty.imageUrl;
                    name = thisParty.partyName;
                    date = thisParty.date;
                    hostname = thisParty.hostName;
                    price = thisParty.price;
                    address = thisParty.address;
                    desc = thisParty.description;
                    populateInfo(name, date, hostname, price, address, desc);
                }
                Picasso.get().load(imageurl).into(imageView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateInfo(String name, String date, String hostname, String price, String address, String desc){
        nameTV.setText(name);
        if(!date.isEmpty()){
            dateTV.setText("when: " + date);
            dateTV.setVisibility(View.VISIBLE);
        }
        else{
            dateTV.setVisibility(View.GONE);
        }
        if(!address.isEmpty()){
            addressTV.setText("where: "+ address);
            addressTV.setVisibility(View.VISIBLE);
        }
        else{
            addressTV.setVisibility(View.GONE);
        }
        if(!price.isEmpty()){
            priceTV.setText("price: " + price);
            priceTV.setVisibility(View.VISIBLE);
        }
        else{
            priceTV.setVisibility(View.GONE);
        }
        if(!desc.isEmpty()){
            descTV.setText("host " + hostname + " left these notes: " + desc);
            descTV.setVisibility(View.VISIBLE);
        }
        else{
            descTV.setVisibility(View.INVISIBLE);
        }

    }

    private void addParty(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            //user is signed in
            uid = user.getUid();
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUserRef.child(uid);
        mspecificUserRef.child("parties").child(partyid).setValue("true");
        Toast.makeText(this, "You have joined this party!",Toast.LENGTH_SHORT).show();
    }

    private void removeParty(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            //user is signed in
            uid = user.getUid();
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUserRef.child(uid);
        mspecificUserRef.child("parties").child(partyid).removeValue();
        System.out.println("removed from MyParties");
    }
}
