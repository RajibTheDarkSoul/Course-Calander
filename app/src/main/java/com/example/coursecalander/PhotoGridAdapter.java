package com.example.coursecalander;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class PhotoGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ArrayList<Uri>> photos;
    private ArrayList<Uri> tmp=new ArrayList<>();

    public PhotoGridAdapter(Context context, ArrayList<ArrayList<Uri>> photos) {
        this.context = context;
        this.photos = photos;
        for (ArrayList<Uri> u : photos) {
            this.tmp.addAll(u);
        }
        Log.d("tmp array size",Integer.toString(tmp.size()));
    }

    @Override
    public int getCount() {
        return tmp.size();
       // return sz;
       // return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return tmp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.imageingrid, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.gridImageView);

       // ArrayList<Uri> currentPhotos = photos.get(position);

        // Check if there are any photos in the current ArrayList
//        if (!currentPhotos.isEmpty()) {
//            // Load all photos from the ArrayList using Glide
//            for (Uri photoUri : currentPhotos) {
//
////                Glide.with(context)
////                        .load(photoUri)
////
////                        .into(imageView);
//
//            }
//        } else {
//            // Handle the case where there are no photos in the ArrayList
//            // You can set a placeholder image or hide the ImageView as needed.
//            Log.d("No photos","at adapter");
//        }


        imageView.setImageURI(tmp.get(position));

        return convertView;
    }

}
