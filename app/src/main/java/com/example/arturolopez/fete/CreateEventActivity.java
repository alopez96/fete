package com.example.arturolopez.fete;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEventActivity extends AppCompatActivity {

    private EditText PartyName;
    private EditText HostName;
    private EditText Price;
    private EditText Address;
    private EditText Description;
    private Button Submit;

    private String partyname, host, price, address, descr, partyid;

    private Party thisParty;

    private FirebaseDatabase mFirebaseDatabase;             //entry point for our app to access the database
    private DatabaseReference mPartyReference;
    private DatabaseReference mspecificPartyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PartyName = findViewById(R.id.party_name_tv);
        HostName = findViewById(R.id.host_name);
        Price = findViewById(R.id.price_tv);
        Address = findViewById(R.id.address_tv);
        Description = findViewById(R.id.desc_tv);
        Submit = findViewById(R.id.submit_tv);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEvent();
            }
        });
    }

    public void submitEvent(){
        partyname = PartyName.getText().toString();
        host = HostName.getText().toString();
        price = Price.getText().toString();
        address = Address.getText().toString();
        descr = Description.getText().toString();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPartyReference = mFirebaseDatabase.getReference().child("parties");
        partyid = mPartyReference.push().getKey();
        mspecificPartyRef = mPartyReference.child(partyid);

        thisParty = new Party(partyname, host, price, address, descr, partyid);

        mspecificPartyRef.setValue(thisParty);

        Toast.makeText(this, "party created",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
