package com.example.cocoabodcreditunionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class FullLoanStatement extends AppCompatActivity {

    TextView StaffName,MemberID,Division,StaffId,Contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_loan_statement);

        StaffName=findViewById(R.id.loanStaffName);
        MemberID=findViewById(R.id.loanMemberId);
        Division=findViewById(R.id.loanDivision);
        StaffId=findViewById(R.id.loanstaffId);
        Contact=findViewById(R.id.loanContact);

        SharedPreferences UserDetails=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");
        String ContributionBal=UserDetails.getString("contributionBal","n/a");
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");

        StaffName.setText(UserName);
        MemberID.setText(MemberId);








    }
}