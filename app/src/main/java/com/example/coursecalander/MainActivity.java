package com.example.coursecalander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity implements viewDateInfo.OnArrayListUpdatedListener{
    private boolean userScrolling = false;
    private static final int COURSE_ADD_REQUEST_CODE = 1001;
    private static final int COURSE_DETAILS = 1002;
    ArrayList<String>courses=new ArrayList<>();
    ArrayList<Integer>attended=new ArrayList<>();
    ArrayList<Integer>TotalClass=new ArrayList<>();
    int startmonth,startdate,startyear,courduration;
    ListView listViewNames;
    ListView listViewAttended;
    ListView listViewTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calenderView);
         listViewNames = findViewById(R.id.viewNameCourses);
         listViewAttended = findViewById(R.id.viewAttendedCourses);
         listViewTotal = findViewById(R.id.viewTotalClasses);

        if (isFirstTimeLaunch()) {

            addcourses();
            showWelcomeLayout();

        } else {
            // Continue with your app's main functionality

            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
            startdate = preferences.getInt("date", 0);
            startmonth = preferences.getInt("month", 0);
            startyear = preferences.getInt("year", 0);
            courduration = preferences.getInt("duration", 0);

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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                viewDateInfo dialogFragment = new viewDateInfo();

                // Pass the ArrayList as an argument to the fragment
                Bundle args = new Bundle();
                args.putSerializable("courses", courses);
                args.putSerializable("attended", attended);
                args.putSerializable("TotalClass", TotalClass);
                dialogFragment.setArguments(args);
                Log.d("Check Duration",Integer.toString(courduration));

                // Show the dialog
                dialogFragment.show(getSupportFragmentManager(), "viewDateInfo");

            }
        });

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
        if (requestCode==COURSE_ADD_REQUEST_CODE&&resultCode==RESULT_OK){


//            Intent Rintent = getIntent();
//            startmonth = Rintent.getIntExtra("month", 0); // Replace defaultValue with the appropriate default value
//            startdate = Rintent.getIntExtra("date", 0);
//            startyear = Rintent.getIntExtra("year", 0);
//            courduration = Rintent.getIntExtra("duration", 0);




            // Set a flag in SharedPreferences to indicate that it's no longer the first launch
//            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("is_first_time_launch", false);
//
//            editor.putInt("startmonth",startmonth);
//            editor.putInt("startdate",startdate);
//            editor.putInt("startyear",startyear);
//            editor.putInt("courseduration",courduration);
//
//            editor.apply();
        }


    }






    private boolean isFirstTimeLaunch() {
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return preferences.getBoolean("is_first_time_launch", true);
    }

    private void showWelcomeLayout() {
        // Show the welcome layout
        //setContentView(R.layout.activity_welcome_layout);
        Intent intent = new Intent(MainActivity.this, welcomeLayout.class);
        startActivityForResult(intent, COURSE_DETAILS);

        //Intent intent = new Intent( MainActivity.this,welcomeLayout.class);
       // startActivity(intent);

        // Retrieve the extras from the Intent
//        Intent Rintent = getIntent();
//         startmonth = Rintent.getIntExtra("month", 0); // Replace defaultValue with the appropriate default value
//         startdate = Rintent.getIntExtra("date", 0);
//         startyear = Rintent.getIntExtra("year", 0);
//         courduration = Rintent.getIntExtra("duration", 0);
//
//
//
//
//        // Set a flag in SharedPreferences to indicate that it's no longer the first launch
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_first_time_launch", false);
//
//        editor.putInt("startmonth",startmonth);
//        editor.putInt("startdate",startdate);
//        editor.putInt("startyear",startyear);
//        editor.putInt("courseduration",courduration);
//
        editor.apply();
    }

    private void addcourses(){
//        Intent intent = new Intent( MainActivity.this,CourseAdd.class);
//        startActivity(intent);

        Intent intent = new Intent(MainActivity.this, CourseAdd.class);
        startActivityForResult(intent, COURSE_ADD_REQUEST_CODE);




    }

    @Override
    public void onArrayListUpdated(ArrayList<Integer> updatedAttended, ArrayList<Integer> updatedTotalClass) {
        attended=updatedAttended;
        TotalClass=updatedTotalClass;
    }


    public class Day {
        private int day;
        private int month;
        private int year;
        boolean isPlaced=false;
        private ArrayList<String> photos;
        // Assuming you want to store photo file paths as strings

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


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Back to main", TextUtils.join(", ", attended));


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

    void backFromFragment(){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("month", startmonth);
        editor.putInt("date", startdate);
        editor.putInt("year", startyear);
        editor.putInt("duration", courduration);

        Gson gson = new Gson();
        String json = gson.toJson(courses);
        String totalclass=gson.toJson(TotalClass);
        String attend= gson.toJson(attended);


        editor.putString("courses", json);
        editor.putString("totalclass",totalclass);
        editor.putString("attend",attend);

        //Log.d("Comeback",)
        // Commit the changes to SharedPreferences
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("month", startmonth);
        editor.putInt("date", startdate);
        editor.putInt("year", startyear);
        editor.putInt("duration", courduration);

        Gson gson = new Gson();
        String json = gson.toJson(courses);
        String totalclass=gson.toJson(TotalClass);
        String attend= gson.toJson(attended);


        editor.putString("courses", json);
        editor.putString("totalclass",totalclass);
        editor.putString("attend",attend);

        // Commit the changes to SharedPreferences
        editor.apply();

    }
}