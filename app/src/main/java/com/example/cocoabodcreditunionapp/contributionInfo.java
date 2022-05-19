package com.example.cocoabodcreditunionapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class contributionInfo extends Fragment {
    View view;
    Context context;
    EditText MinContribution, EntranceFee, Shares;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contribution_info, container, false);
        initUI();
        return view;
    }


    public void initUI() {

        context = getContext();
        MinContribution = view.findViewById(R.id.regContributionET);
        EntranceFee = view.findViewById(R.id.entranceFeeET);
        Shares = view.findViewById(R.id.regSharesET);

    }


}