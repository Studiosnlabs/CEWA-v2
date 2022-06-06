package com.example.cocoabodcreditunionapp;

import static java.lang.Thread.sleep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.time.Duration;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PartialWithdrawal extends AppCompatActivity  implements Runnable {

    ImageView backButton;
    Spinner type;
    String typeOfWithdrawal;
    Button withDrawRequest;
    String hiredDate;
    long numDaysAsMember;
    Double ContributionBal;
    String ContributionBalTxt;
    Double LoanBalance;
    TextView WithdrawalLimitTV;
    Double WithdrawalRequestlimit;
    TextView WithdrawalInfo;
    EditText AmountET;
    EditText PurposeET;
    String RequestInProgress;
    Double amountRequested;
    String pBearerToken;
    AlertDialog.Builder inProgressrequestAlert;
    DecimalFormat df=new DecimalFormat("#,###.00");

    CountDownTimer expiry;
    String timerActive="";


    public void expireApp() {
        expiry = new CountDownTimer(100000, 1000) {
            @Override
            public void onTick(long l) {
                timerActive = "active";

                Log.d(TAG, "onTick: counting down to time out");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: app killed");
                finish();


            }
        }.start();
    }


    private static final String TAG = "Withdrawal";

//PARTIAL COMPLETE

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

            WithdrawalInfo.setText("Your account is not legible for Withdrawal.You have to be a member for at least 6 months and have a contribution of at least Ghc200 to make a partial withdrawal request");
            WithdrawalInfo.setTextColor(Color.RED);
            withDrawRequest.setEnabled(false);
            AmountET.setEnabled(false);
            type.setEnabled(false);
            PurposeET.setEnabled(false);

            //Toast.makeText(PartialWithdrawal.this, "You have to be a member for at least 6 and have a contribution of at least Ghc200 months to make a partial withdrawal request", Toast.LENGTH_SHORT).show();
            return false;
        }


    }


    public void calcWithdrawalLimit(Boolean validUser) {


        if (validUser) {

            if (LoanBalance==0){
                WithdrawalRequestlimit=ContributionBal-200;
                WithdrawalLimitTV.setText("GHS "+df.format(WithdrawalRequestlimit));
            }

            else{
                WithdrawalRequestlimit = ContributionBal - (0.33 * LoanBalance);
                WithdrawalLimitTV.setText("GHS "+df.format(WithdrawalRequestlimit));
            }


            Log.d(TAG, "LoanBalance: " + LoanBalance);
            Log.d(TAG, "calcWithdrawalLimit: " + WithdrawalRequestlimit);

        }else {
            WithdrawalLimitTV.setText("Not Available");
        }

    }


    public void makeWithdrawalRequest(){
        amountRequested=Double.valueOf(AmountET.getText().toString());


        if (amountRequested<=WithdrawalRequestlimit){

            Log.d(TAG, "setNewPassword: "+pBearerToken);

            StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.withdrawal_request, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.optString("success");
                        String error=object.optString("error");
                        Log.d(TAG, "onResponse: "+success);

                        if (!success.isEmpty()){
                            successDialog Success = new successDialog();
                            Success.showDialog(PartialWithdrawal.this);
                        }
                        else if(!error.isEmpty()){
                            inProgressrequestAlert =new AlertDialog.Builder(PartialWithdrawal.this);
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
                            WithdrawalInfo.setText(error);
                            WithdrawalInfo.setTextColor(Color.RED);
                            withDrawRequest.setEnabled(false);
                            WithdrawalLimitTV.setText("Not Available");
                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: "+error);

                    Toast.makeText(PartialWithdrawal.this, "Withdrawal Request failed , check internet connection and try again", Toast.LENGTH_SHORT).show();

                }
            }  )
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params1 = new HashMap<String, String>();
                    params1.put("amount", AmountET.getText().toString());
                    params1.put("purpose",PurposeET.getText().toString());
                    params1.put("type",typeOfWithdrawal);


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
            failedDialog.showDialog(PartialWithdrawal.this);
        }


    }






    public void backIntent() {

        Intent intent = new Intent(PartialWithdrawal.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_withdrawal);

        backButton = findViewById(R.id.pwithdrawalBack);
        type = findViewById(R.id.withdrawTypeSelect);
        withDrawRequest = findViewById(R.id.partialWithdrawalRequestBtn);
        WithdrawalLimitTV=findViewById(R.id.withDrawalLimitAmt);
        WithdrawalInfo=findViewById(R.id.WithdrawalInfo);
        AmountET=findViewById(R.id.pAmtET);
        PurposeET=findViewById(R.id.purposeET);


        SharedPreferences UserDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        hiredDate = UserDetails.getString("hired_date", "n/a");
        ContributionBalTxt = UserDetails.getString("contributionBal", "n/a");
        ContributionBal = Double.valueOf(ContributionBalTxt);
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");
        LoanBalance = Double.valueOf(loanBal);
        pBearerToken=UserDetails.getString("token","n/a");

        validateUserForLoan();
        calcWithdrawalLimit(validateUserForLoan());


        withDrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( typeOfWithdrawal.isEmpty() || typeOfWithdrawal.equals("Select a withdrawal type")){

                    Toast.makeText(PartialWithdrawal.this, "Select a withdrawal type", Toast.LENGTH_SHORT).show();
                }
                else if(AmountET.getText().toString().isEmpty()){
                    Toast.makeText(PartialWithdrawal.this, "Enter an amount", Toast.LENGTH_SHORT).show();
                }
                else if (PurposeET.getText().toString().isEmpty()){
                    Toast.makeText(PartialWithdrawal.this, "Enter the purpose for withdrawal", Toast.LENGTH_SHORT).show();
                } else {
                    makeWithdrawalRequest();
                }


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIntent();
            }
        });


        int thisMonthInt = 0;

        String[] items2 = new String[]{"Select a withdrawal type", "Partial Withdrawal", "Complete Withdrawal"};

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
        type.setAdapter(spinnerArrayAdapter2);
        type.setSelection(thisMonthInt);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                typeOfWithdrawal = selectedItemTextPlain;
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
                        typeOfWithdrawal = "PARTIAL";
                        break;

                    case 2:
                        typeOfWithdrawal = "COMPLETE";
                        break;


                }


//                typeOfWithdrawal = selectedItemText;
                Log.d(TAG, "onItemSelected: item selected" + typeOfWithdrawal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PartialWithdrawal.this, MainActivity.class);
        startActivity(intent);
      //  super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {


        Log.d(TAG, "onPause: app has paused");
        super.onPause();
    }

    @Override
    protected void onStop() {
//        CalendarForm obj = new CalendarForm();
//        t = new Thread(obj);
//        t.start();
//        Log.d(TAG, "onStop: app has stopped");
//        expireApp();
        super.onStop();
    }

    @Override
    public void run() {
        try {
            sleep(1 * 60 * 1000);
            Log.d(TAG, "run: timer has started");
            // Wipe your valuable data here
            System.exit(0);
        } catch (InterruptedException e) {
            Log.d(TAG, "run: timer did not start");
            return;
        }
    }

    @Override
    protected void onDestroy() {

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);

        super.onDestroy();
    }







}