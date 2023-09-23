package com.example.coursecalander;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {
    private boolean userScrolling = false;
    private static final int COURSE_ADD_REQUEST_CODE = 1001;
    ArrayList<String>courses=new ArrayList<>();
    ArrayList<Integer>attended=new ArrayList<>();
    ArrayList<Integer>TotalClass=new ArrayList<>();
    int startmonth,startdate,startyear,courduration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calenderView);
        ListView listViewNames = findViewById(R.id.viewNameCourses);
        ListView listViewAttended = findViewById(R.id.viewAttendedCourses);
        ListView listViewTotal = findViewById(R.id.viewTotalClasses);

        if (isFirstTimeLaunch()) {

            addcourses();
            showWelcomeLayout();

        } else {
            // Continue with your app's main functionality

            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
            startdate = preferences.getInt("startdate", 0);
            startmonth = preferences.getInt("startmonth", 0);
            startyear = preferences.getInt("startyear", 0);
            courduration = preferences.getInt("courseduration", 0);

            String json = preferences.getString("courses", null);
            String attend = preferences.getString("attend", null);
            String total = preferences.getString("totalclass", null);

            if (json != null) {
                // Convert JSON string back to ArrayList
                Gson gson = new Gson();
                courses = gson.fromJson(json, new TypeToken<ArrayList<String>>() {}.getType());
                if (courses != null) {
                    Log.d("Data Retrieval", "Courses: " + courses.toString());
                } else {
                    Log.d("Data Retrieval", "Courses ArrayList is null");
                }
            }
            if (attend != null) {
                Gson gson = new Gson();
                attended = gson.fromJson(attend, new TypeToken<ArrayList<Integer>>() {}.getType());
            }
            if (total != null) {
                Gson gson = new Gson();
                TotalClass = gson.fromJson(total, new TypeToken<ArrayList<Integer>>() {}.getType());
            }
        }

        // Call setViews after data retrieval
        setViews(listViewNames, listViewAttended, listViewTotal);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COURSE_ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> returnedCourses = data.getStringArrayListExtra("courses");
            if (returnedCourses != null) {
                //courses.clear(); // Clear the existing data
                //courses.addAll(returnedCourses); // Update with the new data
                // Refresh your ListView here or call setViews again
                courses=returnedCourses;


                if (courses!=null){
                    int n=courses.size();
                    createNums(n);}



                //SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                //SharedPreferences.Editor editor = preferences.edit();

                Gson gson = new Gson();
                String json = gson.toJson(courses);
                String totalclass=gson.toJson(TotalClass);
                String attend= gson.toJson(attended);

                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("courses", json);
                editor.putString("totalclass",totalclass);
                editor.putString("attend",attend);
                editor.apply();

                Intent mtent=new Intent(MainActivity.this, MainActivity.class);
                startActivity(mtent);
                finish();
            }
        }
    }






    private boolean isFirstTimeLaunch() {
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return preferences.getBoolean("is_first_time_launch", true);
    }

    private void showWelcomeLayout() {
        // Show the welcome layout
        //setContentView(R.layout.activity_welcome_layout);

        Intent intent = new Intent( MainActivity.this,welcomeLayout.class);
        startActivity(intent);

        // Retrieve the extras from the Intent
        Intent Rintent = getIntent();
         startmonth = Rintent.getIntExtra("month", 0); // Replace defaultValue with the appropriate default value
         startdate = Rintent.getIntExtra("date", 0);
         startyear = Rintent.getIntExtra("year", 0);
         courduration = Rintent.getIntExtra("duration", 0);




        // Set a flag in SharedPreferences to indicate that it's no longer the first launch
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_first_time_launch", false);

        editor.putInt("startmonth",startmonth);
        editor.putInt("startdate",startdate);
        editor.putInt("startyear",startyear);
        editor.putInt("courseduration",courduration);

        editor.apply();
    }

    private void addcourses(){
//        Intent intent = new Intent( MainActivity.this,CourseAdd.class);
//        startActivity(intent);

        Intent intent = new Intent(MainActivity.this, CourseAdd.class);
        startActivityForResult(intent, COURSE_ADD_REQUEST_CODE);


        //Intent Nintent=getIntent();
        //courses=Nintent.getStringArrayListExtra("courses");
//        courses = getIntent().getStringArrayListExtra("courses");
//        if (courses!=null){
//        int n=courses.size();
//        createNums(n);}
//
//
//
//        //SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
//        //SharedPreferences.Editor editor = preferences.edit();
//
//        Gson gson = new Gson();
//        String json = gson.toJson(courses);
//        String totalclass=gson.toJson(TotalClass);
//        String attend= gson.toJson(attended);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("courses", json);
//        editor.putString("totalclass",totalclass);
//        editor.putString("attend",attend);
//        editor.apply();


    }



    public class Day {
        private int day;
        private int month;
        private int year;
        private ArrayList<String> photos; // Assuming you want to store photo file paths as strings

        // Constructor to initialize day, month, and year
        public Day(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
            this.photos = new ArrayList<>();
        }

        // Getter and setter methods for day, month, and year
        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        // Methods to add and retrieve photos
        public void addPhoto(String photoPath) {
            photos.add(photoPath);
        }


        public ArrayList<String> getPhotos() {
            return photos;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//            if (resultCode == RESULT_OK) {
//                courses = data.getStringArrayListExtra("courses");
//
//                // Now you have the courses ArrayList from CourseAdd
//                // Do something with the received data
//            }
//
//    }


    private void createNums(int n){
         // Change this to the desired size
        for (int i = 0; i < n; i++) {
            attended.add(0);
            TotalClass.add(0);
        }
    }

    private void setViews(ListView listViewNames,ListView listViewAttended, ListView listViewTotal){

        if (courses!=null){
        ArrayAdapter<String> adapterCourseName = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
            adapterCourseName.notifyDataSetChanged();
        listViewNames.setAdapter(adapterCourseName);}




        if (attended!=null) {
            ArrayList<String> StringAttend = new ArrayList<>();
            for (Integer value : attended) {
                StringAttend.add(String.valueOf(value));
            }
        ArrayAdapter<String> adapterAttend = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, StringAttend);
            if (StringAttend!=null){
                adapterAttend.notifyDataSetChanged();
        listViewAttended.setAdapter(adapterAttend);}}



            if (TotalClass != null) {
                ArrayList<String> StringTotal = new ArrayList<>();
                for (Integer value : TotalClass) {
                    StringTotal.add(String.valueOf(value));
                }

                ArrayAdapter<String> adapterTotal = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, StringTotal);
                if (StringTotal != null) {
                    adapterTotal.notifyDataSetChanged();
                    listViewTotal.setAdapter(adapterTotal);
                }
            }
        }







}