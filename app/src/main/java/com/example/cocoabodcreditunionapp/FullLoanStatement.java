package com.example.cocoabodcreditunionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.JsPromptResult;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullLoanStatement extends AppCompatActivity {

    TextView StaffName,MemberID,Division,StaffId,Contact;
    private static final String TAG = "FullLoanStatement";
    ArrayList<String> Dates = new ArrayList<>();
    ArrayList<String> loanAmount = new ArrayList<>();
    ArrayList<String> debts = new ArrayList<>();
    ArrayList<String> installments = new ArrayList<>();
    ArrayList<String> Balances = new ArrayList<>();
    ArrayList<LoanModel> arrayList;
    RecyclerView recyclerView;
    LoanModel loanModel;
    LoanAdapter loanAdapter;
    ScrollView loanContScroll;
    TextView loadingTxt;
    String pBearerToken;
    String FromDate;
    String ToDate;

    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    RelativeLayout payslipCons;
    TextView loanMonthYearTitle;

    public void getDivision(JSONObject response){

        try {
            JSONObject division=response.getJSONObject("division");
            String divisionName=division.optString("name");
            Division.setText(divisionName);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getDate(JSONObject response){

        try {
            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject date = transactionArray.getJSONObject(i);



                Dates.add(date.optString("payment_date"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void getLoanAmount(JSONObject response){

        try {
            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject date = transactionArray.getJSONObject(i);



                loanAmount.add(date.optString("installment_amount"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getDebt(JSONObject response){


        try {
            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject date = transactionArray.getJSONObject(i);



                debts.add(date.optString("repayment_amount"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void getOutstandingBalance(JSONObject response){

        try {
            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject date = transactionArray.getJSONObject(i);



                Balances.add(date.optString("repayment_amount"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getInstallments(JSONObject response){
        try {
            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject date = transactionArray.getJSONObject(i);



                installments.add(date.optString("installment_amount"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_loan_statement);

        StaffName=findViewById(R.id.loanStaffName);
        MemberID=findViewById(R.id.loanMemberId);
        Division=findViewById(R.id.loanDivision);
        StaffId=findViewById(R.id.loanstaffId);
        Contact=findViewById(R.id.loanContact);
        loanMonthYearTitle=findViewById(R.id.MonthYearTitle);
        loanContScroll=findViewById(R.id.scrollView2);
        loadingTxt=findViewById(R.id.loadingtxt);
        recyclerView=findViewById(R.id.recycler_view);

        SharedPreferences UserDetails=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");
        String ContributionBal=UserDetails.getString("contributionBal","n/a");
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");
        String email=UserDetails.getString("email", "n/a");
        String staffId=UserDetails.getString("staff_number", "n/a");
        pBearerToken=UserDetails.getString("token","n/a");

        StaffName.setText(UserName);
        MemberID.setText(MemberId);
        Contact.setText(email);
        StaffId.setText(staffId);

        FromDate=getIntent().getStringExtra("from");
        ToDate=getIntent().getStringExtra("to");

        String fromDate= getIntent().getStringExtra("monthFrom") + " "+ getIntent().getStringExtra("yearFrom");
        String toDate=getIntent().getStringExtra("monthTo")+ " "+ getIntent().getStringExtra("yearTo");
        loanMonthYearTitle.setText("Loan Statement from "+ fromDate+" to " +toDate);




        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.genStatement,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject message = object.optJSONObject("message");

                            assert message != null;
                            getDate(message);
                            getLoanAmount(message);
                            getDebt(message);
                            getInstallments(message);
                            getOutstandingBalance(message);
                            getDivision(message);


                            arrayList = new ArrayList<>();

                            int arraysize = Balances.size();

                            for (int position = 0; position < arraysize; position++) {
                                DecimalFormat df = new DecimalFormat("#,###.00");


                                String date = Dates.get(position);

                                String totalAmount = loanAmount.get(position);
                                Double totalAmtD = Double.parseDouble(totalAmount);
                                String totalAmountF = df.format(totalAmtD);

                                String debt = debts.get(position);
                                Double debtAmtD = Double.parseDouble(totalAmount);
                                String debtAmountF = df.format(totalAmtD);

                                String installmentAmount = installments.get(position);
                                Double installmentAmtD = Double.parseDouble(totalAmount);
                                String installmentAmountF = df.format(totalAmtD);

                                String Balance = Balances.get(position);
                                Double balanceAmtD = Double.parseDouble(Balance);
                                String balanceAmountF = df.format(balanceAmtD);
                                Log.d(TAG, "onResponse: " + balanceAmountF);




                                //String hour=hours.get(position);
                                //String balance=balances.get(position);
                                //String rate=rates.get(position);

                                LoanModel bill = new LoanModel(date, totalAmountF, debtAmountF, installmentAmountF,balanceAmountF);
                                arrayList.add(bill);
                                setRecyclerView();


                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                              Log.d(TAG, "EmpPay: "+response);
                        loanContScroll.setVisibility(View.VISIBLE);
                        loadingTxt.setVisibility(View.INVISIBLE);

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(TAG, "onErrorResponse: " + error.toString());
                        // Toast.makeText(PaySlipPage.this, error.toString(), Toast.LENGTH_LONG).show();
                        Toast toast = Toast.makeText(getApplicationContext(), "Server Error Please check internet connection and try again! ", Toast.LENGTH_SHORT);

                        toast.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params3 = new HashMap<String, String>();

                params3.put("from", FromDate);
                params3.put("to", ToDate);
                params3.put("statement_type", "LOAN");


                return params3;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + pBearerToken);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                // headers.put("Accept","application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);










    }



    private void setRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loanAdapter = new LoanAdapter(arrayList);
        recyclerView.setAdapter(loanAdapter);

    }


}