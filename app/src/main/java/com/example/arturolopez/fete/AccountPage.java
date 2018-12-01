package com.example.arturolopez.fete;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AccountPage extends AppCompatActivity {

    private static final String TAG = "CreateEventActivity";

    private EditText nameText;
    private EditText emailText;
    private EditText numberText;
    private EditText bioText;
    private Button submitButton;
    private ImageView userImageView;

    private String username, email, number, bio;
    private String uid, key;
    private String imageUrl;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;             //entry point for our app to access the database
    private DatabaseReference mUsersReference;
    private DatabaseReference mspecificUserRef;
    private StorageReference mountainsRef;
    private MyUser thisUser;

    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        nameText = findViewById(R.id.name_tv);
        emailText = findViewById(R.id.email_tv);
        numberText = findViewById(R.id.phone_tv);
        bioText = findViewById(R.id.bio_tv);
        submitButton = findViewById(R.id.submit_tv);
        userImageView = findViewById(R.id.image_view);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            submitChanges();
            }
        });

        if(userImageView != null){
            userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }
            });
        }

        getUserImage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prefillInfo();
        getUserImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefillInfo();
        getUserImage();
    }


    public void prefillInfo(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            //get userid and email
            email = user.getEmail();
            uid = user.getUid();
            //fill in emailText
            emailText.setText(email);
            Log.d(TAG,"userid " + uid);
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = mFirebaseDatabase.getReference().child("users");
        mspecificUserRef = mUsersReference.child(uid);
        mspecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisUser = dataSnapshot.getValue(MyUser.class);
                if(thisUser.username != null){
                    nameText.setText(thisUser.username);
                }
                if(thisUser.phone != null){
                    numberText.setText(thisUser.phone);
                }
                if(thisUser.bio != null) {
                    bioText.setText(thisUser.bio);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void submitChanges(){
        username = nameText.getText().toString();
        number = numberText.getText().toString();
        bio = bioText.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            //user is signed in
            uid = user.getUid();
            //get email, to make sure it doesn't change
            email = emailText.getText().toString();
            //push user to Firebase
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUsersReference = mFirebaseDatabase.getReference().child("users");
            mspecificUserRef = mUsersReference.child(uid);
            mspecificUserRef.child("username").setValue(username);
            mspecificUserRef.child("email").setValue(email);
            mspecificUserRef.child("phone").setValue(number);
            mspecificUserRef.child("bio").setValue(bio);
            //confirm update to user
            Toast.makeText(AccountPage.this, "account updated",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                userImageView.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data2 = baos.toByteArray();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mUsersReference = mFirebaseDatabase.getReference().child("users");
                mspecificUserRef = mUsersReference.child(uid);
                String key = mUsersReference.push().getKey();
                mountainsRef = storageRef.child("users").child(key);
                UploadTask uploadTask = mountainsRef.putBytes(data2);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        submitButton.setEnabled(false);
                        mountainsRef.getDownloadUrl().addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        submitButton.setEnabled(true);
                                    }
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        Log.e("EventActivity Exception", exception.toString());
                                    }
                                });

                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user != null){
                            //user is signed in
                            uid = user.getUid();
                            mFirebaseDatabase = FirebaseDatabase.getInstance();
                            mUsersReference = mFirebaseDatabase.getReference().child("users");
                            mspecificUserRef = mUsersReference.child(uid);
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            mspecificUserRef.child("img").setValue(downloadUrl.toString());
                            Log.d(TAG,"imgUrl set: " + downloadUrl);
                            Log.d(TAG,"upload success");
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
        mspecificUserRef = mUsersReference.child(uid);
        mspecificUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("img").getValue() != null){
                    imageUrl = dataSnapshot.child("img").getValue().toString();
                }
                Picasso.get().load(imageUrl).into(userImageView, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"picassoimage success");
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.d(TAG,"picassoimage failed. error: " + ex.getMessage());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
