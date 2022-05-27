package com.example.cocoabodcreditunionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class LoanApplication extends AppCompatActivity {

    ImageView backButton;
    Button requestButton;
    Spinner loanSelect;
    String typeOfLoan;

    private static final String TAG = "loanApplication";

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
        loanSelect=findViewById(R.id.loanTypeSelect);

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


        int thisMonthInt = 0;

        String[] items2 = new String[]{"Select a loan type", "Normal Loan", "Special Loan"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, items2) {

            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;

            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        loanSelect.setAdapter(spinnerArrayAdapter2);
        loanSelect.setSelection(thisMonthInt);

        loanSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                typeOfLoan = selectedItemTextPlain;
//                // If user change the default selection
//                // First item is disable and it is used for hint
//                ((TextView) view).setTextColor(Color.WHITE);
//                if(position > 0){
//                    // Notify the selected item text
//
//                    Toast.makeText
//                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                            .show();
//
//                    YearThumbnail.setText(selectedItemText.toUpperCase());
//                }
                ;

                switch (position) {
                    case 0:
                        selectedItemText = "0";
                        break;

                    case 1:
                        typeOfLoan = "Normal";
                        break;

                    case 2:
                        typeOfLoan = "Special";
                        break;


                }


//                typeOfWithdrawal = selectedItemText;
                Log.d(TAG, "onItemSelected: item selected" + typeOfLoan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }
}