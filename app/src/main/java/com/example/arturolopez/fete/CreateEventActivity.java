package com.example.arturolopez.fete;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateEventActivity extends AppCompatActivity {

    private EditText PartyName;
    private EditText PartyDate;
    private EditText Price;
    private EditText Address;
    private EditText Description;
    private Button Submit;
    private Button Cancel;
    private ImageView EventImageButton;

    private String partyname, date, host, price, address, descr, partyid, imageUrl;
    private String uid;

    private Party thisParty;

    private FirebaseDatabase mFirebaseDatabase;             //entry point for our app to access the database
    private DatabaseReference mPartyReference;
    private DatabaseReference mspecificPartyRef;
    private FirebaseAuth mAuth;

    private DatabaseReference mUsersReference;
    private DatabaseReference mspecificUserRef;
    private MyUser thisUser;

    private ProgressDialog pd;

    public static final int GET_FROM_GALLERY = 3;

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
        EventImageButton = findViewById(R.id.event_image_tv);
        imageUrl = "https://icon-icons.com/icons2/602/PNG/512/SLR_Camera_icon-icons.com_55815.png";
        Picasso.get().load(imageUrl).into(EventImageButton);
        EventImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

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

        pd = new ProgressDialog(CreateEventActivity.this);
        pd.setMessage("Submitting...");
        pd.show();

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
                thisParty = new Party(partyname, date, host, price, address, descr, partyid, imageUrl);
                mspecificPartyRef.setValue(thisParty);
                Toast.makeText(CreateEventActivity.this, "party created",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pd.dismiss();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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
                EventImageButton.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data2 = baos.toByteArray();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mPartyReference = mFirebaseDatabase.getReference().child("parties");
                String key = mPartyReference.push().getKey();
                StorageReference mountainsRef = storageRef.child("parties").child(partyid);
                UploadTask uploadTask = mountainsRef.putBytes(data2);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
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

}
