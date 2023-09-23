package com.example.coursecalander;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class welcomeLayout extends AppCompatActivity {
    int month,date,year,duration;
    Button start;
    EditText M,D,Y,Dura;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_layout);

        SharedPreferences sharedPreferences = getSharedPreferences("FirstSaved", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        start=findViewById(R.id.buttonStart) ;

        M = findViewById(R.id.editTextMonth);
        D = findViewById(R.id.editTextDate);
        Y = findViewById(R.id.editTextYear);
        Dura = findViewById(R.id.editTextDuration);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m=M.getText().toString();
                String d=D.getText().toString();
                String y=Y.getText().toString();
                String dur=Dura.getText().toString();

                Log.d("MyApp", "Button clicked");

                if (m.equals("") || d.equals("") || y.equals("") || dur.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter all data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                month=Integer.parseInt(m);
                date=Integer.parseInt(d);
                year=Integer.parseInt(y);
                duration=Integer.parseInt(dur);


                editor.putInt("Smonth", month);
                editor.putInt("Sdate", date);
                editor.putInt("Syear", year);
                editor.putInt("duration", duration);

                // Commit the changes to SharedPreferences
                editor.apply();

                Intent intent = new Intent(welcomeLayout.this, MainActivity.class);
                intent.putExtra("month", month);
                intent.putExtra("date", date);
                intent.putExtra("year", year);
                intent.putExtra("duration", duration);

                //startActivity(intent);
                finish();


            }
        });


    }

}