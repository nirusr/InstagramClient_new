package com.walmart.instagramclient;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by sgovind on 10/10/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }


    public static class ViewHolder {
        TextView tvCaption;
        ImageView ivPhoto;
        TextView tvUsername;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item from this position
        InstagramPhoto photo = getItem(position);
        //Instatiante view holder to hold views
        ViewHolder viewHolder ;
        //Check recycled view exist, if not create (inflate) one
        if (convertView == null ) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);

            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if ( photo.getCaption().length() > 0 ) {
            viewHolder.tvCaption.setText(photo.getCaption());
        }
        viewHolder.tvUsername.setText(photo.getUsername());
        Picasso.with(getContext()).load(photo.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(viewHolder.ivPhoto);

        return convertView;
    }
}
