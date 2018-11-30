package com.example.arturolopez.fete;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswdActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswdActivity";

    private EditText emailText;

    private Button submitButton;

    private String email;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;             //entry point for our app to access the database
    private DatabaseReference mUsersReference;
    private DatabaseReference mspecificUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);

        TextView toolbarText = findViewById(R.id.toolbar_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbarText!=null && toolbar!=null) {
            toolbarText.setText(R.string.my_parties);
            setSupportActionBar(toolbar);
        }

        emailText = findViewById(R.id.email_tv);
        submitButton = findViewById(R.id.submit_tv);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth != null){
            Log.d(TAG,"mauth is not null");
        }
        else{
            Log.d(TAG,"mauth is null");
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString().trim();
                Log.d(TAG, "EMAIL: " + email);
                if (TextUtils.isEmpty(email) || !email.contains("@")) {
                    Toast.makeText(getApplication(), "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswdActivity.this, "Email Sent!",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(ForgotPasswdActivity.this,
                                            task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
