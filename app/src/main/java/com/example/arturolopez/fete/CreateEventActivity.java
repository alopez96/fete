package com.example.arturolopez.fete;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class CreateEventActivity extends AppCompatActivity {

    private EditText PartyName;
    private EditText PartyDate;
    private EditText Price;
    private EditText Address;
    private EditText Description;
    private Button Submit;
    private Button Cancel;

    private String partyname, date, host, price, address, descr, partyid;
    private String uid;

    private Party thisParty;

    private FirebaseDatabase mFirebaseDatabase;             //entry point for our app to access the database
    private DatabaseReference mPartyReference;
    private DatabaseReference mspecificPartyRef;
    private FirebaseAuth mAuth;

    private DatabaseReference mUsersReference;
    private DatabaseReference mspecificUserRef;
    private MyUser thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PartyName = findViewById(R.id.party_name_tv);
        PartyDate = findViewById(R.id.date_tv);
        Price = findViewById(R.id.price_tv);
        Address = findViewById(R.id.address_tv);
        Description = findViewById(R.id.desc_tv);
        Submit = findViewById(R.id.submit_event_tv);
        Cancel = findViewById(R.id.cancel_event_tv);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEvent();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateEventActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(CreateEventActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void submitEvent(){
        partyname = PartyName.getText().toString();
        price = Price.getText().toString();
        date = PartyDate.getText().toString();
        address = Address.getText().toString();
        descr = Description.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            uid = currentUser.getUid();
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUsersReference.child(uid);
        mspecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                host = dataSnapshot.child("email").getValue().toString();
                Log.d("host",host);
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mPartyReference = mFirebaseDatabase.getReference().child("parties");
                partyid = mPartyReference.push().getKey();
                mspecificPartyRef = mPartyReference.child(partyid);
                thisParty = new Party(partyname, date, host, price, address, descr, partyid);
                mspecificPartyRef.setValue(thisParty);

                Toast.makeText(CreateEventActivity.this, "party created",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
