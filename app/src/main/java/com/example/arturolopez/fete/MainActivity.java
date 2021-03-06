package com.example.arturolopez.fete;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.arturolopez.fete.Utils.FullImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.StatsSnapshot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mPartyids = new ArrayList<>();

    private CircleImageView Selfie;
    private String partyid;

    private String imageUrl;
    private String uid;


    private FirebaseAuth mAuth;
    private DatabaseReference mPartyRef;
    private DatabaseReference mUsersReference, mSpecificUserRef;
    private FirebaseDatabase mFirebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //load image for userImage
        Selfie = findViewById(R.id.image_view);
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/realtime-156710.appspot.com/o/admin%2Fplace-holder-2.png?alt=media&token=a158c22a-d264-4863-b83b-48bfe69cae36";
//        Picasso.get().load(imageUrl).into(Selfie);
        Selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AccountPage.class);
                startActivity(i);
            }
        });


        getImages();
        getUserImage();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Create a new Party
        Intent i = new Intent(MainActivity.this, CreateEventActivity.class);
        startActivity(i);

        //noinspection SimplifiableIfStatement
        if (id == R.id.create_event_btn) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_my_parties) {
            Intent i = new Intent(MainActivity.this, MyPartiesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_all_users) {
            Intent i = new Intent(MainActivity.this, AllUsersActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_friends) {
            Intent i = new Intent(MainActivity.this, FriendsActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_messages) {
            Intent i = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(i);

        } else if (id == R.id.action_settings) {
            //sign out
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this,"logged out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecyclerView();
        getUserImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
        getUserImage();
    }


    private void getImages(){
        //get adress and date of parties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPartyRef = mFirebaseDatabase.getReference().child("parties");
        mPartyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count " ,""+ dataSnapshot.getChildrenCount());
                for (DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {
                    final String name = childDataSnapshot.child("partyName").getValue().toString();
                    final String date = childDataSnapshot.child("date").getValue().toString();
                    final String imageUrl = childDataSnapshot.child("imageUrl").getValue().toString();
                    final String partyid = childDataSnapshot.child("partyid").getValue().toString();
                    mDates.add(date);
                    mNames.add(name);
                    mImageUrls.add(imageUrl);
                    mPartyids.add(partyid);
                }
                initRecyclerView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
    }


    private void initRecyclerView(){
        Log.d(TAG, "MainACtivity: initRecyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(this, mDates, mNames, mImageUrls, mPartyids);
        recyclerView.setAdapter(adapter);
    }

    private void getUserImage(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            //user is signed in
            uid = user.getUid();
        }
        else{
            return;
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = mFirebaseDatabase.getReference().child("users");
        mSpecificUserRef = mUsersReference.child(uid);
        mSpecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("img").getValue() != null){
                    imageUrl = dataSnapshot.child("img").getValue().toString();
                }
                Picasso.get().load(imageUrl).into(Selfie, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"picassoimage success");
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.d(TAG,"picassoimage failed");
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
