package com.example.cocoabodcreditunionapp;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoanStatements extends AppCompatActivity implements Runnable {

    private static final String TAG = "LoanStatements";
    ImageView backButton;
    String MonthSelected;
    String YearSelected;
    String toDateSelected;
    Spinner loanYearStartSelect;
    Spinner loanMonthStartSelect;
    Spinner loanYearEndSelect;
    Spinner loanMonthEndSelect;
    String ToMonthSelected;
    Handler handler;
    ProgressBar loanProgressBar;
    RelativeLayout loanStatementPreview;
    String monthTo;
    String monthFrom;
    String FromMonthSelected;
    String FromYearSelected;
    String fromDate;
    String ToYearSelected;
    TextView LoanBalanceHeader;
    String MonthP;
    String pBearerToken;
    Button viewFullStatement;
    ProgressBar CalProgressBar;
    Boolean timerStarted;
    CountDownTimer expiry;
    String timerActive = "";


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


    public void showPreview() {

        loanProgressBar.setVisibility(View.VISIBLE);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loanStatementPreview.setVisibility(View.VISIBLE);
                loanProgressBar.setVisibility(View.GONE);

            }
        }, 2000);

    }


    public void backIntent() {

        Intent intent = new Intent(LoanStatements.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_statements);

        backButton = findViewById(R.id.loansStatementBack);
        loanYearStartSelect = findViewById(R.id.loanStatementStartYearSelector);
        loanMonthStartSelect = findViewById(R.id.loanStatementStartMonthSelector);
        viewFullStatement = findViewById(R.id.loanSearch);
        loanMonthEndSelect = findViewById(R.id.loanStatementEndMonthSelector);
        loanYearEndSelect = findViewById(R.id.loanStatementEndYearSelector);
        loanProgressBar = findViewById(R.id.loanStatementProgressbar);
        loanStatementPreview = findViewById(R.id.loanstatementPreview);
        LoanBalanceHeader = findViewById(R.id.loanBalanceAmt);
        CalProgressBar = findViewById(R.id.loanStatementProgressbar);


        SharedPreferences UserDetails = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName = UserDetails.getString("userName", "n/a");
        String MemberId = UserDetails.getString("memberID", "n/a");
        String ContributionBal = UserDetails.getString("contributionBal", "n/a");
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");
        String email = UserDetails.getString("email", "n/a");
        String staffId = UserDetails.getString("staff_number", "n/a");
        pBearerToken = UserDetails.getString("token", "n/a");


        LoanBalanceHeader.setText(loanBal);


        viewFullStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.genStatement,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                CalProgressBar.setVisibility(View.VISIBLE);

                                try {
                                    JSONObject object = new JSONObject(response);
                                    JSONObject message = object.optJSONObject("message");


                                    if (message.isNull("Opening Balance")) {

                                        Toast.makeText(LoanStatements.this, "This statement record is not available", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }


                                    fromDate = FromYearSelected + "-" + FromMonthSelected;
                                    toDateSelected = ToYearSelected + "-" + ToMonthSelected;
                                    Intent intent = new Intent(LoanStatements.this, FullLoanStatement.class);
                                    intent.putExtra("from", fromDate);
                                    intent.putExtra("to", toDateSelected);
                                    intent.putExtra("monthFrom", monthFrom);
                                    intent.putExtra("monthTo", monthTo);
                                    intent.putExtra("yearFrom", FromYearSelected);
                                    intent.putExtra("yearTo", ToYearSelected);
                                    Log.d(TAG, "onClick: " + fromDate);
                                    Log.d(TAG, "onClick: " + toDateSelected);
                                    startActivity(intent);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//

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

                        params3.put("from", fromDate);
                        params3.put("to", toDateSelected);
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
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIntent();
            }
        });

        Calendar thisMonth = Calendar.getInstance();
        int thisMonthInt = thisMonth.get(Calendar.MONTH);


        String[] items2 = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, items2) {


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        loanMonthStartSelect.setAdapter(spinnerArrayAdapter2);
        loanMonthStartSelect.setSelection(thisMonthInt);

        loanMonthStartSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                monthFrom = selectedItemTextPlain;
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
                        selectedItemText = "01";
                        break;

                    case 1:
                        selectedItemText = "02";
                        break;

                    case 2:
                        selectedItemText = "03";
                        break;
                    case 3:
                        selectedItemText = "04";
                        break;
                    case 4:
                        selectedItemText = "05";
                        break;
                    case 5:
                        selectedItemText = "06";
                        break;
                    case 6:
                        selectedItemText = "07";
                        break;
                    case 7:
                        selectedItemText = "08";
                        break;
                    case 8:
                        selectedItemText = "09";
                        break;
                    case 9:
                        selectedItemText = "10";
                        break;
                    case 10:
                        selectedItemText = "11";
                        break;
                    case 11:
                        selectedItemText = "12";
                        break;


                }


                FromMonthSelected = selectedItemText;
                Log.d(TAG, "onItemSelected: item selected" + FromMonthSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//StartYearSelect


        Calendar thisYear = Calendar.getInstance();
        int thisYearInt = thisYear.get(Calendar.YEAR);
        String thisYearString = Integer.toString(thisYearInt);

        ArrayList<Integer> years = new ArrayList<>();
        years.add(2000);


        for (int i = 2000; i < thisYearInt; i++) {
            years.add(i);
        }

        int yearrange = years.get(years.size() - 1);

        if (yearrange != thisYearInt) {
            years.add(thisYearInt);
        } else {
            Log.d(TAG, "onCreate: Year is up to date");
        }


        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, years) {


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                    return super.getDropDownView(position, convertView, parent);

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;


            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        loanYearStartSelect.setAdapter(spinnerArrayAdapter);
        loanYearStartSelect.setSelection(spinnerArrayAdapter.getPosition(yearrange + 1));

        loanYearStartSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer selectedItemText = (Integer) parent.getItemAtPosition(position);
                String selectedItemString = selectedItemText.toString();
                // If user change the default selection
                // First item is disable and it is used for hint
//                    ((TextView) view).setTextColor(Color.WHITE);
//                    if(position > 0){
//                        // Notify the selected item text
//
//                        Toast.makeText
//                                (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                                .show();
//
//
//                    }


                FromYearSelected = selectedItemString;
                Log.d(TAG, "onItemSelected: Year selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        EndDateSelect


        Calendar thisMonthEnd = Calendar.getInstance();
        int thisMonthIntEnd = thisMonthEnd.get(Calendar.MONTH);


        String[] items2End = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

//      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  dropdown.setAdapter(adapter);

        final ArrayAdapter<String> spinnerArrayAdapter2End = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, items2End) {


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter2End.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        loanMonthEndSelect.setAdapter(spinnerArrayAdapter2);
        loanMonthEndSelect.setSelection(thisMonthIntEnd);

        loanMonthEndSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                String selectedItemTextPlain = (String) parent.getItemAtPosition(position);
                monthTo = selectedItemTextPlain;
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
                        selectedItemText = "01";
                        break;

                    case 1:
                        selectedItemText = "02";
                        break;

                    case 2:
                        selectedItemText = "03";
                        break;
                    case 3:
                        selectedItemText = "04";
                        break;
                    case 4:
                        selectedItemText = "05";
                        break;
                    case 5:
                        selectedItemText = "06";
                        break;
                    case 6:
                        selectedItemText = "07";
                        break;
                    case 7:
                        selectedItemText = "08";
                        break;
                    case 8:
                        selectedItemText = "09";
                        break;
                    case 9:
                        selectedItemText = "10";
                        break;
                    case 10:
                        selectedItemText = "11";
                        break;
                    case 11:
                        selectedItemText = "12";
                        break;


                }


                ToMonthSelected = selectedItemText;
                Log.d(TAG, "onItemSelected: item selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Calendar thisYearEnd = Calendar.getInstance();
        int thisYearIntEnd = thisYearEnd.get(Calendar.YEAR);
        String thisYearStringEnd = Integer.toString(thisYearIntEnd);

        ArrayList<Integer> yearsEnd = new ArrayList<>();
        yearsEnd.add(2000);


        for (int i = 2000; i < thisYearInt; i++) {
            yearsEnd.add(i);
        }

        int yearrangeEnd = yearsEnd.get(yearsEnd.size() - 1);

        if (yearrangeEnd != thisYearIntEnd) {
            years.add(thisYearInt);
        } else {
            Log.d(TAG, "onCreate: Year is up to date");
        }


        ArrayAdapter<Integer> spinnerArrayAdapterEnd = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, years) {


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                    return super.getDropDownView(position, convertView, parent);

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;


            }
        };

        spinnerArrayAdapterEnd.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        loanYearEndSelect.setAdapter(spinnerArrayAdapter);
        loanYearEndSelect.setSelection(spinnerArrayAdapter.getPosition(yearrange + 1));

        loanYearEndSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer selectedItemText = (Integer) parent.getItemAtPosition(position);
                String selectedItemString = selectedItemText.toString();
                // If user change the default selection
                // First item is disable and it is used for hint
//                    ((TextView) view).setTextColor(Color.WHITE);
//                    if(position > 0){
//                        // Notify the selected item text
//
//                        Toast.makeText
//                                (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                                .show();
//
//
//                    }


                ToYearSelected = selectedItemString;
                Log.d(TAG, "onItemSelected: Year selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoanStatements.this, MainActivity.class);
        startActivity(intent);
        //  super.onBackPressed();
    }


    @Override
    protected void onResume() {
       if (timerActive.equals("active")){

           expiry.cancel();
           Log.d(TAG, "onResume: timer canceled");
       } else{
           super.onResume();
       }

    }


    @Override
    protected void onPause() {

        if (!this.isFinishing()){
            Log.d(TAG, "onPause: app has paused");
            super.onPause();
        } else {

            Log.d(TAG, "onStop: app has stopped");
            expireApp();

        }


    }

    @Override
    protected void onStop() {
//        CalendarForm obj = new CalendarForm();
//        t = new Thread(obj);
//        t.start();

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