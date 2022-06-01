package com.example.cocoabodcreditunionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoanApplication extends AppCompatActivity {

    ImageView backButton;
    Button requestButton;
    Spinner loanSelect;
    String typeOfLoan;
    String periodInstall;
    Spinner periodSelect;



    String hiredDate;
    long numDaysAsMember;
    Double ContributionBal;
    String pBearerToken;
    TextView loanRequestInfo;
    EditText AmountET;
    EditText PurposeET;
    Button loanRequest;
    TextView LoanLimitTv;
    Double LoanBalance;
    Double LoanRequestlimit;
    String ContributionBalTxt;
    Double amountRequested;
    DecimalFormat df=new DecimalFormat("#,###.00");
    AlertDialog.Builder inProgressrequestAlert;


    private static final String TAG = "loanApplication";

    public void backIntent(){

        Intent intent=new Intent(LoanApplication.this,MainActivity.class);
        startActivity(intent);

    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public Boolean validateUserForLoan() {


        Calendar calendar = Calendar.getInstance();

        Date now = new Date();
        calendar.setTime(now);

        try {
            Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(hiredDate);

            numDaysAsMember = getDateDiff(date1, now, TimeUnit.DAYS);
            Log.d(TAG, "validateUserForLoan: " + numDaysAsMember);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (numDaysAsMember > 182.5 && ContributionBal > 200) {
            Log.d(TAG, "validateUserForLoan: " + numDaysAsMember);
            return true;
        } else {

            loanRequestInfo.setText("Your account is not legible for Withdrawal.You have to be a member for at least 6 months and have a contribution of at least Ghc200 to make a partial withdrawal request");
            loanRequestInfo.setTextColor(Color.RED);
            loanRequest.setEnabled(false);
            AmountET.setEnabled(false);
            loanSelect.setEnabled(false);
            periodSelect.setEnabled(false);
            PurposeET.setEnabled(false);

            //Toast.makeText(PartialWithdrawal.this, "You have to be a member for at least 6 and have a contribution of at least Ghc200 months to make a partial withdrawal request", Toast.LENGTH_SHORT).show();
            return false;
        }


    }


    public void calcWithdrawalLimit(Boolean validUser) {


        if (validUser) {

            if (ContributionBal==0){
                LoanLimitTv.setText("NOT AVAILABLE");
            }

            else{
                LoanRequestlimit = ContributionBal * 3;
                LoanLimitTv.setText("GHS "+df.format(LoanRequestlimit));
            }


            Log.d(TAG, "LoanBalance: " + LoanBalance);
            Log.d(TAG, "calcWithdrawalLimit: " + LoanRequestlimit);

        }else {
            LoanLimitTv.setText("Not Available");
        }

    }

    public void makeLoanRequest(){
        amountRequested=Double.valueOf(AmountET.getText().toString());


        if (amountRequested<=LoanRequestlimit){

            Log.d(TAG, "loanRequest: "+pBearerToken);

            StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.loanRequest, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.optString("success");
                        String error=object.optString("error");
                        Log.d(TAG, "onResponse: "+success);

                        if (!success.isEmpty()){
                            successDialog Success = new successDialog();
                            Success.showDialog(LoanApplication.this);
                        }
                        else if(!error.isEmpty()){
                            inProgressrequestAlert =new AlertDialog.Builder(LoanApplication.this);
                            inProgressrequestAlert.setTitle("Pending Request");
                            inProgressrequestAlert.setIcon(R.drawable.reqeuestfailed);
                            inProgressrequestAlert.setMessage("You can only make one request at a time");
                            inProgressrequestAlert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            inProgressrequestAlert.show();
                            loanRequestInfo.setText(error);
                            loanRequestInfo.setTextColor(Color.RED);
                            requestButton.setEnabled(false);
                            LoanLimitTv.setText("Not Available");
                        }




                    } catch ( JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: "+error);

                    Toast.makeText(LoanApplication.this, "Loan Request failed , check internet connection and try again", Toast.LENGTH_SHORT).show();

                }
            }  )
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params1 = new HashMap<String, String>();
                    params1.put("amount", AmountET.getText().toString());
                    params1.put("purpose",PurposeET.getText().toString());
                    params1.put("type",typeOfLoan);
                    params1.put("months",periodInstall);


                    return params1;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + pBearerToken);

                    return headers;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }else{
            failedDialog failedDialog = new failedDialog();
            failedDialog.showDialog(LoanApplication.this);
        }


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);

        backButton=findViewById(R.id.loanBack);
        requestButton=findViewById(R.id.loanRequestBtn);
        loanSelect=findViewById(R.id.loanTypeSelect);
        periodSelect=findViewById(R.id.periodET);
        PurposeET=findViewById(R.id.purposeET);
        AmountET=findViewById(R.id.loanAmtET);
        loanRequestInfo=findViewById(R.id.loanInstruction);
        LoanLimitTv=findViewById(R.id.loanLimitAmt);

//        loanRequest=findViewById(R.id.loanRequestBtn);

        SharedPreferences UserDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        hiredDate = UserDetails.getString("hired_date", "n/a");
        ContributionBalTxt = UserDetails.getString("contributionBal", "n/a");
        ContributionBal = Double.valueOf(ContributionBalTxt);
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");
        LoanBalance = Double.valueOf(loanBal);
        pBearerToken=UserDetails.getString("token","n/a");

        validateUserForLoan();
        calcWithdrawalLimit(validateUserForLoan());

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( typeOfLoan.isEmpty() || typeOfLoan.equals("Select a loan type")){

                    Toast.makeText(LoanApplication.this, "Select a loan type", Toast.LENGTH_SHORT).show();
                }
                else if(AmountET.getText().toString().isEmpty()){
                    Toast.makeText(LoanApplication.this, "Enter an amount", Toast.LENGTH_SHORT).show();
                }

                else if (periodInstall.isEmpty() || periodInstall.equals("Select a period of installment")){

                    Toast.makeText(LoanApplication.this, "Select an installment period", Toast.LENGTH_SHORT).show();
                }
                else if (PurposeET.getText().toString().isEmpty()){
                    Toast.makeText(LoanApplication.this, "Enter the purpose for withdrawal", Toast.LENGTH_SHORT).show();
                } else {
                    makeLoanRequest();
                }


            }
        });




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIntent();
            }
        });


//        requestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                successDialog Success= new successDialog();
//                Success.showDialog(LoanApplication.this);
//            }
//        });


        int thisMonthInt = 0;

        String[] items2 = new String[]{"Select a loan type", "Normal Loan", "Special Loan"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, items2) {

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
                tv.setTextColor(Color.BLACK);
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
        loanSelect.setAdapter(spinnerArrayAdapter2);
        loanSelect.setSelection(thisMonthInt);

        loanSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                typeOfLoan = selectedItemTextPlain;
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

                switch (position) {
                    case 0:
                        selectedItemText = "0";
                        break;

                    case 1:
                        typeOfLoan = "Normal";
                        break;

                    case 2:
                        typeOfLoan = "Special";
                        break;


                }


//                typeOfWithdrawal = selectedItemText;
                Log.d(TAG, "onItemSelected: item selected" + typeOfLoan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        int thisLoanInt = 0;

        String[] items3 = new String[]{"Select a period of installment", "1 year", "2 years","3 years","4 years"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, items3) {

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
                tv.setTextColor(Color.BLACK);
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
        periodSelect.setAdapter(spinnerArrayAdapter3);
        periodSelect.setSelection(thisLoanInt);

        periodSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                periodInstall = selectedItemTextPlain;
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

                switch (position) {
                    case 0:
                        selectedItemText = "0";
                        break;

                    case 1:
                        periodInstall = "1";
                        break;

                    case 2:
                        periodInstall = "2";
                        break;

                    case 3:
                        periodInstall = "3";
                        break;

                    case 4:
                        periodInstall = "4";
                        break;

                }


//                typeOfWithdrawal = selectedItemText;
                Log.d(TAG, "onItemSelected: item selected" + periodInstall);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }
}