package com.example.cocoabodcreditunionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ResetPassword extends AppCompatActivity {

    EditText newPassword;
    EditText confirmPassword;
    Button SetPassword;
    String NewPassword;
    String ConfirmPassword;
    String pBearerToken;


    private static final String TAG = "ResetPassCewa";
    public  void  setNewPassword(){

        Log.d(TAG, "setNewPassword: "+pBearerToken);

        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.passReset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                   JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    Log.d(TAG, "onResponse: "+success);

                    if(success.equals("Password Changed Successfully") && ConfirmPassword.length()>=6){
                        Intent intent=new Intent(ResetPassword.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Log.d(TAG, "onResponse: ");
                        Toast.makeText(ResetPassword.this, "Password Reset Failed,Please try again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);

                Toast.makeText(ResetPassword.this, "Password reset failed , check internet connection and try again", Toast.LENGTH_SHORT).show();

            }
        }  )
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("password", ConfirmPassword);


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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPassword=findViewById(R.id.NewPassWordET);

        confirmPassword=findViewById(R.id.ConfirmPasswordET);

        SetPassword=findViewById(R.id.setPasswordBtn);

        SharedPreferences UserToken=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        pBearerToken=UserToken.getString("token","n/a");






        SetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewPassword=newPassword.getText().toString();
                ConfirmPassword=confirmPassword.getText().toString();

                Log.d(TAG, "onClick: "+NewPassword);
                Log.d(TAG, "onClick: "+ConfirmPassword);

                if(newPassword.getText().toString().equals(confirmPassword.getText().toString())){



                    setNewPassword();


                } else{
                    Toast.makeText(ResetPassword.this, "The passwords do not match please check and try again", Toast.LENGTH_SHORT).show();
                }


            }
        });



        confirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        confirmPassword.setTransformationMethod(null);
                        confirmPassword.setSelection(confirmPassword.length());

                        return true;
                    }
                }
                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ResetPassword.this,login.class);
        startActivity(intent);
        finish();
    }

}