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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsViewHolder> {

    private static final String TAG = "MyPartiesRecyclerViewAd";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mPartyids = new ArrayList<>();
    private Context mContext;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPatyRef, mspecificPartyRef;


    public void loadFriends(ArrayList<String> mFriendsIds){
        Log.d(TAG,"friendsList " + mFriendsIds);
        mPartyids = mFriendsIds;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPatyRef = mFirebaseDatabase.getReference().child("users");
        for(String friendid : mFriendsIds){
            Log.d(TAG,"friendid " + friendid);
            mspecificPartyRef = mPatyRef.child(friendid);
            mspecificPartyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "child hostname " + dataSnapshot.child("hostName").getValue());
                    if(dataSnapshot.child("username").getValue() != null) {
                        Log.d(TAG, "child hostname " + dataSnapshot.child("hostName").getValue());
                        mImages.add("https://firebasestorage.googleapis.com/v0/b/realtime-156710.appspot.com/o/admin%2Fplace-holder-2.png?alt=media&token=a158c22a-d264-4863-b83b-48bfe69cae36");
                        mImageNames.add(dataSnapshot.child("username").getValue().toString());
                        notifyDataSetChanged();
                    }
                    else{
                        mImages.add("https://firebasestorage.googleapis.com/v0/b/realtime-156710.appspot.com/o/admin%2Fplace-holder-2.png?alt=media&token=a158c22a-d264-4863-b83b-48bfe69cae36");
                        mImageNames.add(dataSnapshot.child("email").getValue().toString());
                        notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        }
    }


    public FriendsRecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mImages, Context mContext) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_friends, parent, false);
        FriendsViewHolder holder = new FriendsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.name.setText(mImageNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"partyid " + mPartyids.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        RelativeLayout parentLayout;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name_tv);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}