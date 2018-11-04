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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mPartyids = new ArrayList<>();

    private Button chatButton;
    private Button eventButton;
    private CircleImageView Selfie;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPartyRef, mspecifiPartyRef;
    private Party thisParty;
    private String partyid;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        chatButton = findViewById(R.id.chat_btn);
        eventButton = findViewById(R.id.create_event_btn);
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
                Toast.makeText(MainActivity.this, "account page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, AccountPage.class);
                startActivity(i);
            }
        });

        chatButton.setVisibility(View.GONE);


        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(i);
            }
        });

        getImages();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    final String name = childDataSnapshot.child("address").getValue().toString();
                    final String date = childDataSnapshot.child("date").getValue().toString();
                    final String imageUrl = childDataSnapshot.child("imageUrl").getValue().toString();
                    final String partid = childDataSnapshot.child("partyid").getValue().toString();
                    mDates.add(date);
                    mNames.add(name);
                    mImageUrls.add(imageUrl);
                    mPartyids.add(partid);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(this, mDates, mNames, mImageUrls, mPartyids);
        recyclerView.setAdapter(adapter);
    }
}
