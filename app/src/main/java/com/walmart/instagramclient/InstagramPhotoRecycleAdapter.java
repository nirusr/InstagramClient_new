package com.walmart.instagramclient;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        public ViewHolder(View itemView) {
            super(itemView);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);

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


    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void clear() {
        mPhotos.clear();
        notifyDataSetChanged();
    }

}
