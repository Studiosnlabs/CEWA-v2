package com.example.cocoabodcreditunionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity {
    Button skip;
    Button Tour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        skip=findViewById(R.id.skipTour);
        Tour=findViewById(R.id.showMeAround);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(WelcomeScreen.this,login.class);
                startActivity(intent);


            }
        });


        Tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(WelcomeScreen.this,OnBoarding.class);
                startActivity(intent);


            }
        });
    }
}