package com.example.arturolopez.fete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.util.LogTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPartiesActivity extends AppCompatActivity {

    private static final String TAG = "MyPartiesActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef, mspecificUserRef;

    private String uid;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mpartyids = new ArrayList<>();

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
                for (DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {
                    ArrayList<String> litList = new ArrayList<>();
                    litList.clear();
                    for(DataSnapshot children : childDataSnapshot.getChildren()){
                        mpartyids.add(children.getKey());
                        Log.d(TAG, childDataSnapshot.getKey() + ": " + children.getKey());
                    }
                }
                Log.d(TAG,"my parties " + mpartyids);
                initImageBitmaps();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        MyPartiesRecyclerViewAdapter adapter = new MyPartiesRecyclerViewAdapter(mNames, mImageUrls, this);
        adapter.load(mpartyids);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
