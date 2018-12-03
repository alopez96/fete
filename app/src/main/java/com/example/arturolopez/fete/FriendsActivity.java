package com.example.arturolopez.fete;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FriendsActivity extends AppCompatActivity {

    private static final String TAG = "FriendsActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef, mspecificUserRef;

    private String uid;
    private TextView noFriendsText;
    private ImageView sadFace;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mfriendsids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        noFriendsText = findViewById(R.id.no_parties_tv);
        sadFace = findViewById(R.id.sad_image);

        noFriendsText.setVisibility(View.GONE);
        sadFace.setVisibility(View.GONE);

        TextView toolbarText = findViewById(R.id.toolbar_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbarText!=null && toolbar!=null) {
            toolbarText.setText(R.string.my_friends);
            setSupportActionBar(toolbar);
        }

        getMyFriends();
    }

    private void getMyFriends(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            //user is signed in
            uid = user.getUid();
            Log.d(TAG, "thisuid: " + uid);
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUserRef.child(uid);


        mspecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {
                    ArrayList<String> myPartiesList = new ArrayList<>();
                    myPartiesList.clear();
                    for(DataSnapshot children : childDataSnapshot.getChildren()){
                        if(children.getKey().contains("-")){
                            //ignore userid
                        }
                        else{
                            //add party id
                            mfriendsids.add(children.getKey());
                            Log.d(TAG, childDataSnapshot.getKey() + ": " + children.getKey());
                        }
                    }
                }
                Log.d(TAG,"my friends " + mfriendsids);
                initRecyclerView();
                Log.d(TAG, "partySize " + mfriendsids.size());
                if(mfriendsids.size() == 0){
                    noFriendsText.setVisibility(View.VISIBLE);
                    sadFace.setVisibility(View.VISIBLE);
                }
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
        FriendsRecyclerViewAdapter adapter = new FriendsRecyclerViewAdapter(mNames, mImageUrls, this);
        adapter.loadFriends(mfriendsids, uid);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
