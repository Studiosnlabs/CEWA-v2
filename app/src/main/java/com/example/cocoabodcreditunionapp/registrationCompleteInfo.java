package com.example.cocoabodcreditunionapp;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class registrationCompleteInfo extends Fragment {

   View view;
   ProgressBar progressBar;
   ImageView registerSuccessImg;
   TextView registerSuccessTxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

           view=inflater.inflate(R.layout.fragment_registration_complete_info, container, false);
            initUI();
           return view;

    }

    public void initUI(){

        progressBar=view.findViewById(R.id.registerProgressbar);
        registerSuccessImg=view.findViewById(R.id.regCompleteCheck);
        registerSuccessTxt=view.findViewById(R.id.regCompleteMsg);

    }



}