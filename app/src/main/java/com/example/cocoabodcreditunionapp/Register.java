package com.example.cocoabodcreditunionapp;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaDrmResetException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterCewa";


    Fragment fragment;
    ImageView backBtn;
    FrameLayout Frame;

    Button nextButton;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    TextView regCount;
    TextView headerText;


    contributionInfo ContributionInfo;
    String MinContribution;
    String EntranceFee;
    String Shares;

    basicInfo BasicInfo;
    String Gender;
    String StaffId;
    String fullName;
    Integer division;

    contactInfo ContactInfo;
    String Address;
    String Location;
    String phone;
    String HouseNo;
    String Email;

    nextofKinsInfo NextofKinsInfo;
    String name_NOK1, percentage_NOK1, relation_NOK1;
    String name_NOK2, percentage_NOK2, relation_NOK2;
    String name_NOK3, percentage_NOK3, relation_NOK3;

    registrationCompleteInfo RegistrationComplete;
    agreementInfo AgreementInfo;

    TextView AgreementText;


    public Boolean checkEmail(String email) {

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();


    }




    public void registerUser() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.registerUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    String RegisterSuccess = object.getString("success");
                    regCount.setText("6/6");
                    headerText.setText("ALL \n DONE!");

                    nextButton.setText("LOGIN");
                    RegistrationComplete.progressBar.setVisibility(View.GONE);
                    RegistrationComplete.registerSuccessImg.setVisibility(View.VISIBLE);
                    RegistrationComplete.registerSuccessTxt.setVisibility(View.VISIBLE);

                    Log.d(TAG, "onResponse: " + RegisterSuccess);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "onErrorResponse: "+error);
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();


            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<String, String>();

                String Subsidiary = division.toString();

                params1.put("name", fullName);
                params1.put("email", Email);
                params1.put("staff_number", StaffId);
                params1.put("gender", Gender);
                params1.put("division", Subsidiary);
                params1.put("agreement", "1");
                params1.put("phone", phone);
                params1.put("minimum_contribution", MinContribution);
                params1.put("entrance_fee", EntranceFee);
                params1.put("shares", Shares);
                params1.put("kin_1_name", name_NOK1);
                params1.put("kin_1_relationship", relation_NOK1);
                params1.put("kin_1_percentage", percentage_NOK1);

                params1.put("kin_2_name", name_NOK2);
                params1.put("kin_2_relationship", relation_NOK2);
                params1.put("kin_2_percentage", percentage_NOK2);

                params1.put("kin_3_name", name_NOK3);
                params1.put("kin_3_relationship", relation_NOK3);
                params1.put("kin_3_percentage", percentage_NOK3);

                params1.put("address", Address);
                params1.put("house_no", HouseNo);
                params1.put("location", Location);


                return params1;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void addFragment() {


        fragmentManager = getSupportFragmentManager();

        fragment = fragmentManager.findFragmentById(R.id.reg_fragmentContainer);
        if (fragment instanceof basicInfo) {

            ContactInfo = new contactInfo();
            fragment = ContactInfo;


            StaffId = BasicInfo.staffId.getText().toString();
            fullName = BasicInfo.fullName.getText().toString();
            Gender = BasicInfo.gender;
            division = BasicInfo.Subsidiary;

            if (StaffId.isEmpty() || fullName.isEmpty() || Gender.equals("0") || division == 0) {

                Toast.makeText(this, "One or more fields are empty please fill the entire form", Toast.LENGTH_SHORT).show();

                return;
            }

            regCount.setText("2/6");
            headerText.setText("CONTACT INFO,\n HOW DO WE \nGET IN TOUCH?");
            Log.d(TAG, "addFragment: " + StaffId);
            Log.d(TAG, "addFragment: " + fullName);
            Log.d(TAG, "addFragment: " + Gender);
            Log.d(TAG, "addFragment: " + division);


        } else if (fragment instanceof contactInfo) {

            ContributionInfo = new contributionInfo();
            fragment = ContributionInfo;
            Address = ContactInfo.Address.getText().toString();
            Location = ContactInfo.Location.getText().toString();
            phone = ContactInfo.Phone.getText().toString();
            HouseNo = ContactInfo.HouseNo.getText().toString();
            Email = ContactInfo.Email.getText().toString();

            if (Address.isEmpty() || Location.isEmpty() || phone.isEmpty() || HouseNo.isEmpty() || Email.isEmpty()) {

                Toast.makeText(this, "One or more fields are empty please fill the entire form", Toast.LENGTH_SHORT).show();

                return;
            }
            if (!checkEmail(Email)) {
                Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.length() < 10) {

                Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                return;

            }


            regCount.setText("3/6");
            headerText.setText("HOW MUCH ,\n WILL YOU LIKE \n TO CONTRIBUTE?");


            Log.d(TAG, "addFragment: " + Address);
            Log.d(TAG, "addFragment: " + Location);
            Log.d(TAG, "addFragment: " + phone);
            Log.d(TAG, "addFragment: " + HouseNo);
            Log.d(TAG, "addFragment: " + Email);

        } else if (fragment instanceof contributionInfo) {
            NextofKinsInfo = new nextofKinsInfo();
            fragment = NextofKinsInfo;


            MinContribution = ContributionInfo.MinContribution.getText().toString();
            EntranceFee = ContributionInfo.EntranceFee.getText().toString();
            Shares = ContributionInfo.Shares.getText().toString();


            if (MinContribution.isEmpty() || EntranceFee.isEmpty() || Shares.isEmpty()) {

                Toast.makeText(this, "One or more fields are empty please fill the entire form", Toast.LENGTH_SHORT).show();

                return;
            }


            headerText.setText("WHO ARE YOUR,\n NEXT OF KINS");
            regCount.setText("4/6");


            Log.d(TAG, "addFragment: " + MinContribution);
            Log.d(TAG, "addFragment: " + EntranceFee);
            Log.d(TAG, "addFragment: " + Shares);


        } else if (fragment instanceof nextofKinsInfo) {

            AgreementInfo = new agreementInfo();
            fragment = AgreementInfo;

            name_NOK1 = NextofKinsInfo.name_NOK1.getText().toString();
            percentage_NOK1 = NextofKinsInfo.percentage_NOK1.getText().toString();
            relation_NOK1 = NextofKinsInfo.relation_NOK1.getText().toString();

            name_NOK2 = NextofKinsInfo.name_NOK2.getText().toString();
            percentage_NOK2 = NextofKinsInfo.percentage_NOK2.getText().toString();
            relation_NOK2 = NextofKinsInfo.relation_NOK2.getText().toString();

            name_NOK3 = NextofKinsInfo.name_NOK3.getText().toString();
            percentage_NOK3 = NextofKinsInfo.percentage_NOK3.getText().toString();
            relation_NOK3 = NextofKinsInfo.relation_NOK3.getText().toString();


            if (name_NOK1.isEmpty() || percentage_NOK1.isEmpty() || relation_NOK1.isEmpty()) {

                Toast.makeText(this, "Please fill all the fields for 1st Next of Kin", Toast.LENGTH_SHORT).show();

                return;
            }


            AgreementText.setVisibility(View.VISIBLE);

            AgreementText.setText(
                    "I " + fullName + " herby apply for membership of the above named C.E.W.A and agree to be bound by the regulations of the Association. I understand that to have successful society we must make regular savings,receive loans for good purposes and make regular loan repayments. I promise to save at least Ghc" + MinContribution + " every month,share of GHc" + Shares + " and an entrance fee of Ghc" + EntranceFee);


            headerText.setText("TERMS AND,\n CONDITIONS");
            regCount.setText("5/6");
            nextButton.setText("FINISH");


            Log.d(TAG, "addFragment: " + name_NOK1);
            Log.d(TAG, "addFragment: " + relation_NOK2);
            Log.d(TAG, "addFragment: " + percentage_NOK3);
            Log.d(TAG, "addFragment: " + name_NOK1);
            Log.d(TAG, "addFragment: " + percentage_NOK3);


        } else if (fragment instanceof agreementInfo) {
            RegistrationComplete = new registrationCompleteInfo();
            fragment = RegistrationComplete;


            if (AgreementInfo.AgreeCheck.isChecked()) {


                AgreementText.setVisibility(View.GONE);
                registerUser();


            } else {
                Toast.makeText(Register.this, "Check Agree to Complete registration", Toast.LENGTH_SHORT).show();
                return;
            }


        } else if (fragment instanceof registrationCompleteInfo) {
            Intent intent = new Intent(Register.this, login.class);
            startActivity(intent);
            finish();
        }


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.reg_fragmentContainer, fragment, "reg_fragment");
        fragmentTransaction.addToBackStack("signUpStack");
        fragmentTransaction.commit();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nextButton = findViewById(R.id.reg_nextBtn);
        regCount = findViewById(R.id.reg_count);
        backBtn = findViewById(R.id.reg_Back);
        headerText = findViewById(R.id.reg_headerText);
        AgreementText = findViewById(R.id.regAgreementText);

        BasicInfo = new basicInfo();

        Frame = findViewById(R.id.reg_fragmentContainer);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment();
            }
        });


        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.reg_fragmentContainer, BasicInfo);
        fragmentTransaction.commit();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = fragmentManager.findFragmentById(R.id.reg_fragmentContainer);
                if (fragment instanceof basicInfo) {
                    Intent intent = new Intent(Register.this, login.class);
                    startActivity(intent);
                    finish();
                } else if (fragment instanceof contactInfo) {
                    fragmentManager.popBackStack();
                    regCount.setText("1/6");
                } else if (fragment instanceof contributionInfo) {
                    fragmentManager.popBackStack();
                    regCount.setText("2/6");
                } else if (fragment instanceof nextofKinsInfo) {
                    fragmentManager.popBackStack();
                    regCount.setText("3/6");
                } else if (fragment instanceof agreementInfo) {
                    fragmentManager.popBackStack();
                    regCount.setText("4/6");
                } else if (fragment instanceof registrationCompleteInfo) {
                    fragmentManager.popBackStack();
                    regCount.setText("5/6");
                    nextButton.setText("FINISH");
                }


            }
        });


    }


    @Override
    public void onBackPressed() {


        Fragment fragment = fragmentManager.findFragmentById(R.id.reg_fragmentContainer);

        if (fragment instanceof basicInfo) {
            super.onBackPressed();
        } else if (fragment instanceof contactInfo) {
            fragmentManager.popBackStack();
            regCount.setText("1/6");
        } else if (fragment instanceof contributionInfo) {
            fragmentManager.popBackStack();
            regCount.setText("2/6");
        } else if (fragment instanceof nextofKinsInfo) {
            fragmentManager.popBackStack();
            regCount.setText("3/6");
        } else if (fragment instanceof agreementInfo) {
            fragmentManager.popBackStack();
            regCount.setText("4/6");
        } else if (fragment instanceof registrationCompleteInfo) {
            fragmentManager.popBackStack();
            regCount.setText("5/6");
        }


    }


}