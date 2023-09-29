package com.example.coursecalander;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class ImageFragment extends DialogFragment {
    private static final int REQUEST_CODE_SELECT_IMAGES = 111;
    private ActivityResultLauncher<Intent> gallery;
    int currentPosition=0;


    MainActivity.Day obj;
    ListView listView;
    ArrayList<String>courses;


    public ImageFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_image,container,false);
        listView=view.findViewById(R.id.courseSelection);

        Bundle arg=getArguments();
        if (arg!=null){
            obj = (MainActivity.Day) arg.getSerializable("Object");
            courses=(ArrayList<String>) arg.getSerializable("courses");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, courses);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openGallery(i);

            }
        });







        // Inflate the layout for this fragment
        return view;
    }






    private void openGallery(int position) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryLauncher.launch(intent);
        // Store the position so that you can associate selected images with it later
        currentPosition = position;
    }


    // Define a result launcher for the gallery
    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ArrayList<Uri> selectedImages = new ArrayList<>();
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            int itemCount = clipData.getItemCount();
                            for (int i = 0; i < itemCount; i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                selectedImages.add(imageUri);
                            }
                        } else if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            selectedImages.add(imageUri);
                        }

                        // Store the selected images in the HashMap
//                        if(selectedImages!=null){
//                            obj.photos.add(currentPosition, selectedImages);}
                        if (selectedImages != null) {
                                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

// Convert Uri objects to strings before serialization
                            ArrayList<String>tmp=new ArrayList<>();
                            for (Uri cur:selectedImages){
                                if (cur != null) {
                                    tmp.add(cur.toString());
                                }
                            }


                            ArrayList<String> currentPhotos = obj.p.get(currentPosition);
                            currentPhotos.addAll(tmp);
                            obj.p.set(currentPosition, currentPhotos);
                        }

                        Log.d("Size of photos:",String.valueOf(obj.p.get(currentPosition).size()));
                        viewDateInfo a=new viewDateInfo();
                        a.backfromImageFragemnt(obj);
                        dismiss();

                        // Now you have the selected images for the current position
                        // You can access them using selectedImagesMap.get(currentPosition)
                    }
                }
            }
    );

}