package com.example.coursecalander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity implements viewDateInfo.OnArrayListUpdatedListener{
    private boolean userScrolling = false;
    private static final int COURSE_ADD_REQUEST_CODE = 1001;
    private static final int COURSE_DETAILS = 1002;
    ArrayList<String>courses=new ArrayList<>();
    ArrayList<Integer>attended=new ArrayList<>();
    ArrayList<Integer>TotalClass=new ArrayList<>();
    ArrayList<Day>Days;
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
            SharedPreferences.Editor ed= preferences.edit();
            //Log.d("Not firdt","not");

            startdate = preferences.getInt("date", 0);
            startmonth = preferences.getInt("month", 0);
            startyear = preferences.getInt("year", 0);
            courduration = preferences.getInt("duration", 0);

            String json = preferences.getString("courses", null);
            String attend = preferences.getString("attend", null);
            String total = preferences.getString("totalclass", null);
            String jso = preferences.getString("Saved_days", null);

            //Retrieving Days arraylist


// Convert the JSON string back to your ArrayList of custom objects



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

            //Creating object for the first time.
            if (noObjectCreated()){
                int t=courduration*38;
                Days=createDayObjects(startdate,startmonth,startyear,t);

            }

            ed.putBoolean("no_object_created",false);
            if (jso!=null){
                Gson gs= new Gson();
                Days = gs.fromJson(jso,new TypeToken<ArrayList<Day>>() {}.getType());}
            Log.d("Days","Days retrieved");

        }

        // Call setViews after data retrieval
        setViews(listViewNames, listViewAttended, listViewTotal);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                viewDateInfo dialogFragment = new viewDateInfo();
                Day dayObject=search(i2,i1,i);
                // Pass the ArrayList as an argument to the fragment
                Bundle args = new Bundle();
                args.putSerializable("courses", courses);
                args.putSerializable("attended", attended);
                args.putSerializable("TotalClass", TotalClass);
                args.putSerializable("dayObject", dayObject);
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

    private boolean noObjectCreated() {
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return preferences.getBoolean("no_object_created", true);
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


    @SuppressLint("ParcelCreator")
    public class Day implements Serializable, Parcelable {
        int day;
         int month;
         int year;
        boolean isPlaced=false;
         ArrayList<ArrayList<Uri>> photos=new ArrayList<>();
         ArrayList<String>courses;
        ArrayList<Integer>attended=new ArrayList<>();
        ArrayList<Integer>ClassPlaced=new ArrayList<>();

        // Assuming you want to store photo file paths as strings

        // Constructor to initialize day, month, and year
        public Day(int day, int month, int year,ArrayList<String>courses) {
            this.day = day;
            this.month = month;
            this.year = year;

            this.courses=courses;
            for(int i=0;i<courses.size();i++){
                this.attended.add(0);
                this.ClassPlaced.add(0);
                this.photos.add(new ArrayList<>());
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {

        }

        // Getter and setter methods for day, month, and year

//        public void addPhoto(String photoPath) {
//            photos.add(photoPath);
//        }
//
//
//        public ArrayList<String> getPhotos() {
//            return photos;
//        }
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
            for (Integer value : totalAttended()) {
                StringAttend.add(String.valueOf(value));
            }
        ArrayAdapter<String> adapterAttend = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, StringAttend);
            if (StringAttend!=null){
                adapterAttend.notifyDataSetChanged();
        listViewAttended.setAdapter(adapterAttend);}}



            if (TotalClass != null) {
                ArrayList<String> StringTotal = new ArrayList<>();
                for (Integer value : totalClasses()) {
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

    void backFromFragment(Day obj){
        setViews(listViewNames,listViewAttended,listViewTotal);
        Log.d("Cameback class",TextUtils.join(",",obj.ClassPlaced));

        Days.set(searchPos(obj.day, obj.month, obj.year),obj);


        Gson gson = new Gson();
        String js = gson.toJson(Days);

// Save the JSON string to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Saved_days", js);
        editor.apply();


        //Log.d("Updating object:",TextUtils.join(",", (Iterable) Days.get(searchPos(obj.day, obj.month, obj.year))));

       // Log.d("Check Arraylist",TextUtils.join(",",Days.get(searchPos(obj.day, obj.month, obj.year)).ClassPlaced));
        Log.d("array size",String.valueOf(Days.size()));


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

    public ArrayList<Day> createDayObjects(int startDay, int startMonth, int startYear, int numberOfDays) {
        ArrayList<Day> dayObjects = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, startMonth - 1, startDay); // Months are 0-based in Calendar

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < numberOfDays; i++) {
            dayObjects.add(new Day(
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1, // Adjust for 0-based months
                    calendar.get(Calendar.YEAR),courses
            ));

            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }

        return dayObjects;
    }
    Day search(int d,int m,int y){
        for (int i=0;i<Days.size();i++){
            if(Days.get(i).day==d&&Days.get(i).month==m&&Days.get(i).year==y){
                return Days.get(i);
            }
        }
        Days.add(new Day(d,m,y,courses));
        return Days.get(Days.size()-1);
    }

    int searchPos(int d,int m,int y){
        for (int i=0;i<Days.size();i++){
            if(Days.get(i).day==d&&Days.get(i).month==m&&Days.get(i).year==y){
                return i;
            }
        }
        Days.add(new Day(d,m,y,courses));

        return Days.size()-1;
    }

    ArrayList<Integer> totalAttended(){
        ArrayList<Integer> tmp=new ArrayList<>();
        for (int i=0;i<courses.size();i++){
            int tm=0;
            for(int j=0;j<Days.size();j++){
                tm+=Days.get(j).attended.get(i);
            }
            tmp.add(tm);
        }


        return tmp;
    }
    ArrayList<Integer> totalClasses(){
        ArrayList<Integer> tmp=new ArrayList<>();
        for (int i=0;i<courses.size();i++){
            int tm=0;
            for(int j=0;j<Days.size();j++){
                tm+=Days.get(j).ClassPlaced.get(i);
            }
            tmp.add(tm);
        }


        return tmp;
    }

}