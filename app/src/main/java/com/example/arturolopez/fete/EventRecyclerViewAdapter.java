package com.example.arturolopez.fete;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mDates;
    private ArrayList<String> mNames;
    private ArrayList<String> mImageUrls;
    private ArrayList<String> mPartyids;
    private Context mContext;

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
                    mContext.startActivity(i);
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("itemView clicked at position " + position);
                    Intent i = new Intent(mContext, SpecificEventActivity.class);
                    i.putExtra("partyid", mPartyids.get(position));
                    mContext.startActivity(i);
                }
            });
        }
    }
}
