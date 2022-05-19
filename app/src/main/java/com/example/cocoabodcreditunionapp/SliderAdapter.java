package com.example.cocoabodcreditunionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;

    }

    //Arrays

    public int[] slide_images={

            R.drawable.ic_statement,
            R.drawable.ic_vault,
            R.drawable.ic_webapp

    };


    public String[] slide_headings={

            "STATEMENTS","WITHDRAWALS","WEB APP"

    };

    public  String[] slide_descs={
            "Access your loan and \n" +
                    "general cewa statements",
            "Make partial or full\n" +
                    "withdrawal requests\n" +
                    "and see your contribution\n" +
                    "balance",
            "Use cewa from your personal\n" +
                    "computer at\n" +
                    "www.cewa.com "

    };

    @Override
    public int getCount() {

        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slidelayout,container,false);


        ImageView slideImageView=(ImageView) view.findViewById(R.id.slideImage);
        TextView slideHeading= (TextView) view.findViewById(R.id.slideHeader);
        TextView slideDescription= (TextView) view.findViewById(R.id.slideDesc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);
        return view;

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
