package com.example.cocoabodcreditunionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoarding extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private Button NextBtn;

    private Button BackBtn;

    private int mCurrentPage;

    private static final String TAG = "OnBoarding";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);


        mSlideViewPager= findViewById(R.id.slideViewPager);
        mDotLayout=findViewById(R.id.dotsLayout);
        NextBtn=findViewById(R.id.NextBtn);
        BackBtn=findViewById(R.id.PreviousBtn);

        sliderAdapter=new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addsDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);


        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                if (mCurrentPage==2){
                    Intent intent=new Intent(OnBoarding.this,login.class);
                    startActivity(intent);

                }
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSlideViewPager.setCurrentItem(mCurrentPage-1);

            }
        });

    }

    public void addsDotsIndicator(int position){
        mDots=new TextView[3];
        mDotLayout.removeAllViews();

        for (int i=0; i<mDots.length; i++){

            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparentGrey));

            mDotLayout.addView(mDots[i]);


        }

        if (mDots.length>0){

            mDots[position].setTextColor(getResources().getColor(R.color.darkBlue));

        }

    }

    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addsDotsIndicator(position);
            mCurrentPage=position;

            if (position==0){
                NextBtn.setEnabled(true);
                BackBtn.setEnabled(false);
                BackBtn.setVisibility(View.INVISIBLE);

                NextBtn.setText("Next");
                BackBtn.setText("");

            }else if(position==mDots.length-1){

                NextBtn.setEnabled(true);
                BackBtn.setEnabled(true);
                BackBtn.setVisibility(View.VISIBLE);

                NextBtn.setText("Finish");
                BackBtn.setText("Back");

            } else{

                NextBtn.setEnabled(true);
                BackBtn.setEnabled(true);
                BackBtn.setVisibility(View.VISIBLE);

                NextBtn.setText("Next");
                BackBtn.setText("Back");

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };




}