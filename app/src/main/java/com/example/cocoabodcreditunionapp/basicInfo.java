package com.example.cocoabodcreditunionapp;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class basicInfo extends Fragment {


    View view;
    Spinner Gender;
    Spinner Division;
    String gender;
    Context context;
    EditText staffId;
    EditText fullName;
    String regStaffId;
    String regFullName;
    String division;
    int Subsidiary;

    private static final String TAG = "basicInfo";









    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_basicinfo, container, false);
        initUI();
        return view;
    }


    private void initUI(){

        context=getContext();

        Gender=view.findViewById(R.id.genderSelect);
        staffId=view.findViewById(R.id.regStaffidET);
        fullName=view.findViewById(R.id.regFullNameET);
        Division=view.findViewById(R.id.departmentSelect);


        int thisMonthInt = 0;

        String[] items2 = new String[]{"Select a Gender","Male", "Female"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
//      Gender.setAdapter(adapter);

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                context, R.layout.support_simple_spinner_dropdown_item, items2) {


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
        Gender.setAdapter(spinnerArrayAdapter2);
        Gender.setSelection(thisMonthInt);

        Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                gender = selectedItemTextPlain;
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


                switch (position){

                    case 1:gender="M"; break;
                    case 2:gender="F"; break;

                    default:gender="0";break;


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] items = new String[]{"Select a subsidiary","Head Office","Seed Production Division","Cocoa Health And Extension Division","Quality Control Company Limited","Cocoa Marketing Company Limited"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, R.layout.support_simple_spinner_dropdown_item, items) {
            @Override
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
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    Subsidiary=0;
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Division.setAdapter(spinnerArrayAdapter);

        Division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
//                ((TextView) view).setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.placeHolder));
                if (position > 0) {
                    // Notify the selected item text

                    // ((TextView) view).setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.white));
                    Log.d(TAG, "onItemSelected: " + selectedItemText);
                    division=selectedItemText;

                }

                switch (position){

                    case 1:Subsidiary=1;
                    break;

                    case 2:Subsidiary=2;
                    break;

                    case 3:Subsidiary=3;
                            break;

                    case 4:Subsidiary=4;
                        break;

                    case 5:Subsidiary=5;
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onPause() {
        super.onPause();

        regFullName=fullName.getText().toString();
        regStaffId=staffId.getText().toString();

    }

    @Override
    public void onStop() {
        super.onStop();

        regFullName=fullName.getText().toString();
        regStaffId=staffId.getText().toString();
    }


}