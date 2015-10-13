package com.walmart.instagramclient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "6e020842abe84299929d81525c997ed1";
    ArrayList<InstagramPhoto> photos;
    //InstagramPhotoAdapter aPhotos;
    InstagramPhotoRecycleAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<InstagramPhoto>();

        RecyclerView lvPhotos = (RecyclerView) findViewById(R.id.lvPhotos);
        aPhotos = new InstagramPhotoRecycleAdapter(photos);
        lvPhotos.setAdapter(aPhotos);
        lvPhotos.setLayoutManager(new LinearLayoutManager(this));
        fetchPopularPhotos();


        /*
        //Create the adapter
        aPhotos = new InstagramPhotoAdapter(this,photos);
        //Find ListView
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        //Attach the adapter
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
        */

        //Setup Refresh
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               fetchPopularPhotoAsync(0);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    //Initial loading of Photos
    public void fetchPopularPhotos() {

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Create network client
        AsyncHttpClient client = new AsyncHttpClient();
       //Trigger GET Request
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("Debug==>", response.toString());
                //Iterate "data" objects in the response
                JSONArray photosJson = null;
                try {
                    photosJson = response.getJSONArray("data"); //getting the data objects (photo objects) from response
                    //Iterate through each photo objects
                    for (int i = 0; i < photosJson.length(); i++) {
                        //Get data object (photo object) from the array at that position
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        //decode the attributes of json into data model
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.setUsername(photoJson.getJSONObject("user").getString("username"));
                        if ( photoJson.getJSONObject("caption") != null) {
                            photo.setCaption(photoJson.getJSONObject("caption").getString("text"));
                        } else {
                            photo.setCaption("");
                        }

                        photo.setImageUrl(photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        photo.setLikesCount(photoJson.getJSONObject("likes").getInt("count"));
                        photo.setType(photoJson.getString("type"));
                        photo.setProfileImageUrl(photoJson.getJSONObject("user").getString("profile_picture"));
                        photo.setCreatedTime(photoJson.getString("created_time"));
                        Log.i("Created time:" , photoJson.getString("created_time"));


                                //Add decoded values to photo array
                                photos.add(photo);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Refresh adapter
                aPhotos.notifyDataSetChanged();
            }

            //onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), "Network failure to retrieve photos from Instagram", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }
    //When swipe is done, refresh photos
    public void fetchPopularPhotoAsync(int page) {

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Create network client
        AsyncHttpClient client = new AsyncHttpClient();
        //Trigger GET Request
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Clear out old items
                aPhotos.clear();
               //Log.i("Debug==>", response.toString());
                //Iterate "data" objects in the response
                JSONArray photosJson = null;
                try {
                    photosJson = response.getJSONArray("data"); //getting the data objects (photo objects) from response
                    //Iterate through each photo objects
                    for (int i = 0; i < photosJson.length(); i++) {
                        //Get data object (photo object) from the array at that position
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        //decode the attributes of json into data model
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.setUsername(photoJson.getJSONObject("user").getString("username"));
                        //photo.setCaption(photoJson.getJSONObject("caption").getString("text"));
                        if ( photoJson.getJSONObject("caption") != null) {
                            photo.setCaption(photoJson.getJSONObject("caption").getString("text"));
                        } else {
                            photo.setCaption("");
                        }

                        photo.setImageUrl(photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        photo.setLikesCount(photoJson.getJSONObject("likes").getInt("count"));
                        photo.setType(photoJson.getString("type"));
                        photo.setProfileImageUrl(photoJson.getJSONObject("user").getString("profile_picture"));
                        photo.setCreatedTime(photoJson.getString("created_time"));
                        //Add decoded values to photo array
                        photos.add(photo);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Refresh adapter
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
            //onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast toast = Toast.makeText(getApplicationContext(), "Network failure to retrieve photos from Instagram", Toast.LENGTH_SHORT);
                toast.show();

            }

        });

    }

}
