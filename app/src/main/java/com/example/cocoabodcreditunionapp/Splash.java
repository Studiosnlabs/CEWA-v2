package com.example.cocoabodcreditunionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    Handler handler;
    String  userName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences UserToken=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        userName=UserToken.getString("userName","n/a");

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!userName.isEmpty() && !userName.equals("n/a")){

                    Intent intent=new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Intent intent=new Intent(Splash.this, WelcomeScreen.class);
                    startActivity(intent);
                    finish();
                }


            }
        },5000);

    }

    }



