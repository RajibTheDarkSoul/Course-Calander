package com.example.coursecalander;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CourseAdd extends AppCompatActivity {
    ArrayList<String>courses=new ArrayList<>();;
    EditText courseName;
    Button add,submit;
    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);

        add=findViewById(R.id.buttonAddCourse);
        submit=findViewById(R.id.buttonSubmit);
        courseName=findViewById(R.id.editTextCourseName);
        listView=findViewById(R.id.courseListView);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cur=courseName.getText().toString();
                if (cur==""){
                    Toast.makeText(CourseAdd.this, "Enter a name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                courses.add(cur);
                Toast.makeText(CourseAdd.this, cur+" Course Added", Toast.LENGTH_SHORT).show();
                courseName.setText("");
                adapter.notifyDataSetChanged();

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//
//                // Put the ArrayList into the Intent as an extra
//                intent.putStringArrayListExtra("courses", courses);
//
//                // Set the result code to indicate success (you can define a result code)
//                setResult(RESULT_OK, intent);
//
//                Intent ntent = new Intent( CourseAdd.this,MainActivity.class);
//                ntent.putStringArrayListExtra("courses", courses);
//
//                startActivity(ntent);
//
//                // Finish the CourseAdd activity
//                finish();


                // In CourseAdd activity

                Intent intent = new Intent();
                intent.putStringArrayListExtra("courses", courses);
                setResult(RESULT_OK, intent);
                finish();



            }
        });




    }





}