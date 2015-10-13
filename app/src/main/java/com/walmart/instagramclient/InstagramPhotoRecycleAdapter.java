package com.walmart.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by sgovind on 10/11/15.
 */
public class InstagramPhotoRecycleAdapter extends RecyclerView.Adapter<InstagramPhotoRecycleAdapter.ViewHolder> {


    //Define ViewHolder pattern
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCaption;
        public ImageView ivPhoto;
        public TextView tvUsername;
        public ImageView ivProfilePicture;
        public TextView tvCreatedTime;
        public TextView tvLikesCount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            ivProfilePicture = (ImageView) itemView.findViewById(R.id.ivProfilePhoto);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tvCreatedTime);
            tvLikesCount = (TextView) itemView.findViewById(R.id.tvLikesCount);

        }
    }

    //Pass the Json array in the constructor

    private List<InstagramPhoto> mPhotos;
    public InstagramPhotoRecycleAdapter(List<InstagramPhoto> photos) {
        mPhotos = photos;

    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the item photo layout
        View itemView = inflater.inflate(R.layout.item_photo, parent, false);

        //Return new View Holder instance
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;

    }
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(InstagramPhotoRecycleAdapter.ViewHolder holder, int position) {

        //Get the data model at the postion
        InstagramPhoto photo = mPhotos.get(position);
        Context context = holder.ivPhoto.getContext();
        //set the item views based on the model

        if ( photo.getCaption().length() > 0 ) {
            holder.tvCaption.setText(photo.getCaption());
        }
        holder.tvUsername.setText(photo.getUsername());
        Picasso.with(context).load(photo.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.ivPhoto);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        //Log.i("ProfileURL:", photo.getProfileImageUrl());
        Picasso.with(context).load(photo.getProfileImageUrl()).fit().placeholder(R.mipmap.ic_launcher).
                transform(transformation).into(holder.ivProfilePicture);

        //Convert Timestamp
        Long longCreatedTimeInMillis = Long.valueOf(photo.getCreatedTime())*1000;

        /*CharSequence createdTime = DateUtils.getRelativeTimeSpanString(longCreatedTime*1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME);*/

        holder.tvCreatedTime.setText(getAbbrevidatedTimeSpan(longCreatedTimeInMillis));

        //if (createdTime >= DateUtils.HOUR_IN_MILLIS)
        //Log.v("Abbr Time:",getAbbrevidatedTimeSpan(longCreatedTime*1000));

        holder.tvLikesCount.setText(String.format("%,d", photo.getLikesCount()) + " likes");
       // Log.v("string formatter:", String.format("%,d", photo.getLikesCount()));


    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void clear() {
        mPhotos.clear();
        notifyDataSetChanged();
    }


    //Get Abbreviated Data String

    private static final String ABBR_YEAR = "y";
    private static final String ABBR_WEEK = "w";
    private static final String ABBR_DAY = "d";
    private static final String ABBR_HOUR = "h";
    private static final String ABBR_MINUTE = "m";
    public static String getAbbrevidatedTimeSpan(long timeMillis) {
        long span = Math.max(System.currentTimeMillis()-timeMillis, 0);
        if (span >= DateUtils.YEAR_IN_MILLIS) {
            return (span / DateUtils.YEAR_IN_MILLIS) + ABBR_YEAR;
        }
        if (span >= DateUtils.WEEK_IN_MILLIS) {
            return (span / DateUtils.WEEK_IN_MILLIS) + ABBR_WEEK;
        }
        if (span >= DateUtils.DAY_IN_MILLIS) {
            return (span / DateUtils.DAY_IN_MILLIS) + ABBR_DAY;
        }
        if (span >= DateUtils.HOUR_IN_MILLIS) {
            return (span / DateUtils.HOUR_IN_MILLIS) + ABBR_HOUR;
        }
        return (span / DateUtils.MINUTE_IN_MILLIS) + ABBR_MINUTE;



    }


}
