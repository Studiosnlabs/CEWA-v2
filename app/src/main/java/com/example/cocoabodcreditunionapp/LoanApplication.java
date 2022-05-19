package com.example.cocoabodcreditunionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LoanApplication extends AppCompatActivity {

    ImageView backButton;
    Button requestButton;


    public void backIntent(){

        Intent intent=new Intent(LoanApplication.this,MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);

        backButton=findViewById(R.id.loanBack);
        requestButton=findViewById(R.id.loanRequestBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIntent();
            }
        });


        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog Success= new successDialog();
                Success.showDialog(LoanApplication.this);
            }
        });




    }
}