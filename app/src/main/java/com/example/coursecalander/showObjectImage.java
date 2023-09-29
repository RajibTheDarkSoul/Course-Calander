package com.example.coursecalander;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.coursecalander.MainActivity;
import com.example.coursecalander.R;

import java.util.ArrayList;

public class showObjectImage extends DialogFragment {

    private MainActivity.Day day;

    // Required empty public constructor
    public showObjectImage() {
    }

    // Create an instance of the dialog fragment and pass the Day object
//    public static showObjectImage newInstance(MainActivity.Day day) {
//        showObjectImage fragment = new showObjectImage();
//        Bundle args = new Bundle();
//        args.putParcelable("day", day);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = (MainActivity.Day) getArguments().getSerializable("Object");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_object_image, container, false);

        if (getArguments() != null) {
            day = (MainActivity.Day) getArguments().getSerializable("Object");
        }

        GridView gridView = view.findViewById(R.id.gridViewImage);
        // Create an adapter to display the photos in the grid
        PhotoGridAdapter adapter = new PhotoGridAdapter(requireContext(), day.retrievePhotos(day));
        gridView.setAdapter(adapter);

        return view;
    }
}
//
//public class ImageAdapter extends BaseAdapter {
//        private Context mContext;
//        private ArrayList<Uri> mImageUris;
//
//        public ImageAdapter(Context context, ArrayList<Uri> imageUris) {
//            mContext = context;
//            mImageUris = imageUris;
//        }
//
//        @Override
//        public int getCount() {
//            return mImageUris.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mImageUris.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView;
//            if (convertView == null) {
//                // If convertView is null, inflate the grid_item.xml layout
//                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.fragment_show_object_image, parent, false);
//                imageView = convertView.findViewById(R.id.gridImageView);
//            } else {
//                // Reuse the recycled view
//                imageView = convertView.findViewById(R.id.gridImageView);
//            }
//
//            // Load the image from the Uri into the ImageView
//            Uri imageUri = mImageUris.get(position);
//            Glide.with(mContext).load(imageUri).into(imageView);
//
//            return convertView;
//        }
//    }
//
//}