package com.example.arturolopez.fete;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "RecyclerViewAdapter";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    //vars
    private ArrayList<String> mDates = new ArrayList<>();

    private Button chatButton;
    private Button eventButton;
    private CircleImageView Selfie;
    private TextView TV;

    private String imageUrl;
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
        Picasso.get().load(imageUrl).into(Selfie);

        Selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "account page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, AccountPage.class);
                startActivity(i);
            }
        });

        chatButton = findViewById(R.id.chat_btn);
        eventButton = findViewById(R.id.create_event_btn);


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Chat", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(i);
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(i);
            }
        });
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

        if (id == R.id.nav_camera) {
            Toast.makeText(MainActivity.this, "chat", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("start", "this is on start method");

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("message");
        databaseReference.setValue("Hello, World!");

        getImages();
    }


    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mDates.add("today at 7pm");
        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("party on high St");

        mDates.add("tomorrow at 3pm");
        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Title 2");

        mDates.add("Weds at 1pm");
        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("night club at 8pm");

        mDates.add("today at 7pm");
        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("pizza place");

        mDates.add("today at 7pm");
        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mDates.add("today at 7pm");
        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");
        
        mDates.add("today at 7pm");
        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mDates.add("today at 7pm");
        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mDates.add("today at 7pm");
        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");

        initRecyclerView();

    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(this, mDates, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }
}
