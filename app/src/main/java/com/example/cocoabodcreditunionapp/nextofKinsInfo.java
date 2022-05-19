package com.example.cocoabodcreditunionapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class nextofKinsInfo extends Fragment {

    View view;
    Context context;
    EditText name_NOK1,percentage_NOK1,relation_NOK1;
    EditText name_NOK2,percentage_NOK2,relation_NOK2;
    EditText name_NOK3,percentage_NOK3,relation_NOK3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_nextof_kins_info, container, false);
        initUI();
        return view;

    }

    public void initUI(){

        context=getContext();
        name_NOK1=view.findViewById(R.id.regNOKName1ET);
        percentage_NOK1=view.findViewById(R.id.regNOKPercentageET);
        relation_NOK1=view.findViewById(R.id.regNOKRelation1ET);

        name_NOK2=view.findViewById(R.id.regNOKName2ET);
        percentage_NOK2=view.findViewById(R.id.regNOK2PercentageET);
        relation_NOK2=view.findViewById(R.id.regNOKRelation2ET);

        name_NOK3=view.findViewById(R.id.regNOKName3ET);
        percentage_NOK3=view.findViewById(R.id.regNOK3PercentageET);
        relation_NOK3=view.findViewById(R.id.regNOKRelation3ET);


    }

}