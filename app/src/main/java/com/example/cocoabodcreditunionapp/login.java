package com.example.cocoabodcreditunionapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private static final String TAG = "LoginCewa";

    ImageView hideShowPassword;
    EditText PasswordET;
    EditText EmailET;
    String word;
    String email;
    Button Login;
    Handler handler;
    ProgressBar loginProgressBar;
    TextView registerHereBtn;
    String Name;
    Integer memberID;
    Double Contribution;
    String PasswordReset;
    String hiredDate;
    Double LoanBalance;
    String StaffNumber;
    String RequestInProgress;






    public void loginRequest(){

        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject object = null;
                JSONObject userObj=null;
                JSONObject memberObj=null;
                try {


                    object = new JSONObject(response);
                    String token = object.getString("token");
                    userObj=object.getJSONObject("user");
                    memberObj=userObj.getJSONObject("member");
                    String ErrorMess=object.optString("message");




                    Name=userObj.getString("name");
                    memberID=memberObj.getInt("id");
                    Contribution=object.getDouble("current_balance");
                    LoanBalance=object.getDouble("current_loan_balance");
                    PasswordReset= userObj.getString("password_reset");
                    hiredDate=memberObj.getString("hire_date");
                    email=userObj.getString("email");
                    StaffNumber=memberObj.getString("staff_number");


                    String ContributionTxt=Contribution.toString();
                    String MemberIdtxt=memberID.toString();
                    String LoanBalTxt=LoanBalance.toString();

                    SharedPreferences userDetails=getApplicationContext().getSharedPreferences("userDetails",getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor=userDetails.edit();
                    editor.putString("userName",Name);
                    editor.putString("contributionBal",ContributionTxt);
                    editor.putString("memberID",MemberIdtxt);
                    editor.putString("token",token);
                    editor.putString("hired_date",hiredDate);
                    editor.putString("current_loan_balance",LoanBalTxt);
                    editor.putString("email",email);
                    editor.putString("staff_number",StaffNumber);



                    editor.apply();

                    Log.d(TAG, "onResponse: "+Name);
                    Log.d(TAG, "onResponse: "+memberID);
                    Log.d(TAG, "onResponse: "+token);
                    Log.d(TAG, "onResponse: staffnum"+StaffNumber);


                    if (ErrorMess.equals("Incorrect credentials")){

                        Toast.makeText(login.this, "Incorrect Credentials,try again", Toast.LENGTH_SHORT).show();
                    }

                    if (PasswordReset.equals("0")){

                        Intent intent=new Intent(login.this,ResetPassword.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {

                        Intent intent=new Intent(login.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }






                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginProgressBar.setVisibility(View.GONE);

                if (error.toString().contains("Client")){
                    Toast.makeText(login.this, "Incorrect Credentials,try again.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "onErrorResponse: "+error);
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                




            }
        }  )
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("email", email);
                params2.put("password", word);

                return params2;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void Login(){

        email=EmailET.getText().toString();
        word=PasswordET.getText().toString();

        Log.d(TAG, "Login: "+email);
        Log.d(TAG, "Login: "+word);

        loginProgressBar.setVisibility(View.VISIBLE);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loginRequest();


            }
        },1000);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        hideShowPassword=findViewById(R.id.viewPassword);
        PasswordET=findViewById(R.id.passwordET);
        loginProgressBar=findViewById(R.id.loginProgressbar);
        registerHereBtn=findViewById(R.id.registerBtn);
        EmailET=findViewById(R.id.userNameET);

        Login=findViewById(R.id.getverificationBtn);



        ActivityResultLauncher<Intent> authActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Intent intent=new Intent(login.this,MainActivity.class);
                            startActivity(intent);

                        }
                    }
                });


        SharedPreferences UserDetails=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");


        if (!MemberId.equals("n/a")){

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                KeyguardManager km=(KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                if(km.isKeyguardSecure()){
                    Intent auhtIntent=km.createConfirmDeviceCredentialIntent("CEWA","PLEASE UNLOCK YOUR PHONE");
                    authActivityResultLauncher.launch(auhtIntent);

                }

            }

        }
        else {
            Log.d(TAG, "onCreate: no login");
        }




        PasswordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (PasswordET.getRight() - PasswordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        PasswordET.setTransformationMethod(null);
                        PasswordET.setSelection(PasswordET.length());

                        return true;
                    }
                }
                return false;
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
//                Intent intent=new Intent(login.this,MainActivity.class);
//                startActivity(intent);
            }
        });

        registerHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,Register.class);
               startActivity(intent);
            }
        });


        hideShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               PasswordET.setTransformationMethod(null);
               PasswordET.setSelection(PasswordET.length());


            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(login.this,WelcomeScreen.class);
        startActivity(intent);
        finish();
    }
}