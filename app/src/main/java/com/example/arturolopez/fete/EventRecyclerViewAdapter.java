package com.example.arturolopez.fete;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arturolopez.fete.Utils.FullImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mDates;
    private ArrayList<String> mNames;
    private ArrayList<String> mImageUrls;
    private ArrayList<String> mPartyids;
    private Context mContext;

    private String uid;
    private String partyid;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef, mspecificUserRef;

    private String joinedPartyBoolean;


    public EventRecyclerViewAdapter(Context context, ArrayList<String> dates, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<String> partyids) {
        mDates = dates;
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
        mPartyids = partyids;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.date.setText(mDates.get(position));

        holder.name.setText(mNames.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mPartyids.get(position));
                Toast.makeText(mContext, mPartyids.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        ((ViewHolder) holder).bind(position);

        partyid = mPartyids.get(position);
        Log.d(TAG,"recyclerview Bind");
        Log.d(TAG,"partid " + partyid);


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null) {
                //user is signed in
                uid = user.getUid();
            }
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUserRef = mFirebaseDatabase.getReference().child("users");
            mspecificUserRef = mUserRef.child(uid);
            joinedPartyBoolean = "false";
            mspecificUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot childData : dataSnapshot.getChildren()){
                        for(DataSnapshot children : childData.getChildren()){
                            String key = children.getKey();
                            Log.d(TAG, "key " + key);
                            boolean exists = Objects.equals(partyid, key);
                            joinedPartyBoolean = "false";
                            if(exists){
                                Log.d(TAG, "party " + partyid + " exists");
                                joinedPartyBoolean = "true";
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_tv);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name_tv);
        }

        void bind(final int position){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("itemView clicked at position " + position);
                    Intent i = new Intent(mContext, SpecificEventActivity.class);
                    i.putExtra("partyid", mPartyids.get(position));
                    i.putExtra("partyBoolean", joinedPartyBoolean);
                    mContext.startActivity(i);
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("itemView clicked at position " + position);
                    Intent i = new Intent(mContext, SpecificEventActivity.class);
                    i.putExtra("partyid", mPartyids.get(position));
                    i.putExtra("partyBoolean", joinedPartyBoolean);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
