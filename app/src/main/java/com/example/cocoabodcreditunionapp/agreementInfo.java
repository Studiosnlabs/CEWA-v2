package com.example.cocoabodcreditunionapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


public class agreementInfo extends Fragment {


    Context context;

    CheckBox AgreeCheck;
  View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_agreement_info, container, false);
        initUI();

        return  view;
    }


    public void initUI(){


        context=getContext();




        AgreeCheck=view.findViewById(R.id.regAgree);




    }

}