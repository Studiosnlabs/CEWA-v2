package com.example.cocoabodcreditunionapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;


public class contactInfo extends Fragment {

 View view;
 EditText Address,HouseNo,Location,Phone,Email;
 Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       view= inflater.inflate(R.layout.fragment_contact_info, container, false);
        initUI();
       return view;
    }


  public void initUI(){

       context=getContext();
       Address=view.findViewById(R.id.regAddressET);
       HouseNo=view.findViewById(R.id.regHouseNoET);
       Location=view.findViewById(R.id.regLocationET);
       Phone=view.findViewById(R.id.regPhoneET);
       Email=view.findViewById(R.id.regEmailET);






  }



}