package com.example.cocoabodcreditunionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class GeneralStatements extends AppCompatActivity {

    private static final String TAG = "GeneralStatements";
    ImageView backButton;
    String FromMonthSelected;
    String FromYearSelected;
    String fromDate;

    String ToMonthSelected;
    String ToYearSelected;
    String toDateSelected;
    Spinner genYearStartSelect;
    Spinner genMonthStartSelect;
    Spinner genYearEndSelect;
    Spinner genMonthEndSelect;
    String monthTo;
    String monthFrom;
    String yearTo;
    String yearFrom;
    Button ViewFullGenStatement;
    ProgressBar generalStatementProgressBar;
    Handler handler;
    RelativeLayout genStatementPreview;
    ImageView genSearch;
    TextView genBalHeader;


    public void backIntent(){

        Intent intent=new Intent(GeneralStatements.this,MainActivity.class);
        startActivity(intent);

    }

    public void showPreview(){

        generalStatementProgressBar.setVisibility(View.VISIBLE);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                genStatementPreview.setVisibility(View.VISIBLE);
                generalStatementProgressBar.setVisibility(View.GONE);

            }
        },2000);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_statements);

        backButton=findViewById(R.id.genStatementBack);
        genMonthStartSelect=findViewById(R.id.genStatementStartMonthSelector);
        genYearStartSelect=findViewById(R.id.genStatementStartYearSelector);
        genYearEndSelect=findViewById(R.id.genStatementEndYearSelector);
        genMonthEndSelect=findViewById(R.id.genStatementEndMonthSelector);
        ViewFullGenStatement=findViewById(R.id.viewFullGenStatement);
        generalStatementProgressBar=findViewById(R.id.genStatementProgressbar);
        genStatementPreview=findViewById(R.id.generalStatementPreview);
        genSearch=findViewById(R.id.genSearch);
        genBalHeader=findViewById(R.id.genStatementAmt);

        genSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreview();
            }
        });


        SharedPreferences UserDetails=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");
        String ContributionBal=UserDetails.getString("contributionBal","n/a");
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");
        String email=UserDetails.getString("email", "n/a");
        String staffId=UserDetails.getString("staff_number", "n/a");



       genBalHeader.setText(ContributionBal);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIntent();
            }
        });



        Calendar thisMonth = Calendar.getInstance();
        int thisMonthInt = thisMonth.get(Calendar.MONTH);


        String[] items2 = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, items2) {


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genMonthStartSelect.setAdapter(spinnerArrayAdapter2);
        genMonthStartSelect.setSelection(thisMonthInt);

        genMonthStartSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                monthFrom = selectedItemTextPlain;
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
                        selectedItemText = "01";
                        break;

                    case 1:
                        selectedItemText = "02";
                        break;

                    case 2:
                        selectedItemText = "03";
                        break;
                    case 3:
                        selectedItemText = "04";
                        break;
                    case 4:
                        selectedItemText = "05";
                        break;
                    case 5:
                        selectedItemText = "06";
                        break;
                    case 6:
                        selectedItemText = "07";
                        break;
                    case 7:
                        selectedItemText = "08";
                        break;
                    case 8:
                        selectedItemText = "09";
                        break;
                    case 9:
                        selectedItemText = "10";
                        break;
                    case 10:
                        selectedItemText = "11";
                        break;
                    case 11:
                        selectedItemText = "12";
                        break;


                }


                FromMonthSelected = selectedItemText;
                Log.d(TAG, "onItemSelected: fromMonth"+FromMonthSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Calendar thisYear = Calendar.getInstance();
        int thisYearInt = thisYear.get(Calendar.YEAR);
        String thisYearString = Integer.toString(thisYearInt);

        ArrayList<Integer> years = new ArrayList<>();
        years.add(2000);


        for (int i = 2000; i < thisYearInt; i++) {
            years.add(i);
        }

        int yearrange = years.get(years.size() - 1);

        if (yearrange != thisYearInt) {
            years.add(thisYearInt);
        } else {
            Log.d(TAG, "onCreate: Year is up to date");
        }


        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, years) {


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                    return super.getDropDownView(position, convertView, parent);

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;


            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genYearStartSelect.setAdapter(spinnerArrayAdapter);
        genYearStartSelect.setSelection(spinnerArrayAdapter.getPosition(yearrange + 1));

        genYearStartSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer selectedItemText = (Integer) parent.getItemAtPosition(position);
                String selectedItemString = selectedItemText.toString();
                // If user change the default selection
                // First item is disable and it is used for hint
//                    ((TextView) view).setTextColor(Color.WHITE);
//                    if(position > 0){
//                        // Notify the selected item text
//
//                        Toast.makeText
//                                (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                                .show();
//
//
//                    }


                FromYearSelected = selectedItemString;
                Log.d(TAG, "onItemSelected: From Year selected"+FromMonthSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        Calendar thisMonthEnd = Calendar.getInstance();
        int thisMonthIntEnd = thisMonthEnd.get(Calendar.MONTH);


        String[] items2End = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter2End = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, items2End) {


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter2End.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genMonthEndSelect.setAdapter(spinnerArrayAdapter2);
        genMonthEndSelect.setSelection(thisMonthIntEnd);

        genMonthEndSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                monthTo = selectedItemTextPlain;
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
                        selectedItemText = "01";
                        break;

                    case 1:
                        selectedItemText = "02";
                        break;

                    case 2:
                        selectedItemText = "03";
                        break;
                    case 3:
                        selectedItemText = "04";
                        break;
                    case 4:
                        selectedItemText = "05";
                        break;
                    case 5:
                        selectedItemText = "06";
                        break;
                    case 6:
                        selectedItemText = "07";
                        break;
                    case 7:
                        selectedItemText = "08";
                        break;
                    case 8:
                        selectedItemText = "09";
                        break;
                    case 9:
                        selectedItemText = "10";
                        break;
                    case 10:
                        selectedItemText = "11";
                        break;
                    case 11:
                        selectedItemText = "12";
                        break;


                }


                ToMonthSelected = selectedItemText;
                Log.d(TAG, "onItemSelected:toMonthSelected"+ToMonthSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });














        Calendar thisYearEnd = Calendar.getInstance();
        int thisYearIntEnd = thisYearEnd.get(Calendar.YEAR);
        String thisYearStringEnd = Integer.toString(thisYearIntEnd);

        ArrayList<Integer> yearsEnd = new ArrayList<>();
        yearsEnd.add(2000);


        for (int i = 2000; i < thisYearInt; i++) {
            yearsEnd.add(i);
        }

        int yearrangeEnd = yearsEnd.get(yearsEnd.size() - 1);

        if (yearrangeEnd != thisYearIntEnd) {
            years.add(thisYearInt);
        } else {
            Log.d(TAG, "onCreate: Year is up to date");
        }


        ArrayAdapter<Integer> spinnerArrayAdapterEnd = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, years) {


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                    return super.getDropDownView(position, convertView, parent);

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;


            }
        };

        spinnerArrayAdapterEnd.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
      genYearEndSelect.setAdapter(spinnerArrayAdapter);
      genYearEndSelect.setSelection(spinnerArrayAdapter.getPosition(yearrange + 1));

        genYearEndSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer selectedItemText = (Integer) parent.getItemAtPosition(position);
                String selectedItemString = selectedItemText.toString();
                // If user change the default selection
                // First item is disable and it is used for hint
//                    ((TextView) view).setTextColor(Color.WHITE);
//                    if(position > 0){
//                        // Notify the selected item text
//
//                        Toast.makeText
//                                (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                                .show();
//
//
//                    }


                ToYearSelected = selectedItemString;
                Log.d(TAG, "onItemSelected: ToYearSelected"+ToYearSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        ViewFullGenStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDate=FromYearSelected+"-"+FromMonthSelected;
                toDateSelected=ToYearSelected+"-"+ToMonthSelected;
                Intent intent=new Intent(GeneralStatements.this,FullGeneralStatement.class);
                intent.putExtra("from",fromDate);
                intent.putExtra("to",toDateSelected);
                intent.putExtra("monthFrom",monthFrom);
                intent.putExtra("monthTo",monthTo);
                intent.putExtra("yearFrom",FromYearSelected);
                intent.putExtra("yearTo",ToYearSelected);
                Log.d(TAG, "onClick: "+fromDate);
                Log.d(TAG, "onClick: "+toDateSelected);
                startActivity(intent);
            }
        });











    }









}
