package com.example.cocoabodcreditunionapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;


public class CardBackFragmentLoan extends Fragment {


    View view;
    Context context;
    TextView contributionAmt;
    TextView contributionTextLabel;
    DecimalFormat df=new DecimalFormat("#,###.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_card_back_loan, container, false);
        initUI();
        return view;
    }

    public void initUI(){

        context=getContext();
        contributionAmt=view.findViewById(R.id.lbuserAccountBalance);
        contributionTextLabel=view.findViewById(R.id.memberIdTitle);

        SharedPreferences UserDetails=getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");
        String ContributionBal=UserDetails.getString("contributionBal","n/a");
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");


        Double ContributionBalance=Double.valueOf(loanBal);
        contributionAmt.setText("GHS "+df.format(ContributionBalance));


    }
}