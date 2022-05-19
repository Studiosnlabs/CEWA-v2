package com.example.cocoabodcreditunionapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "MainActivity";
    private static final int INTENT_AUTHENTICATE = 20;
    Button showBalanceBtn;
    RelativeLayout accountDetailsRel;
    CardView PartialWithdrawalCard;
    CardView LoanApplicationCard;
    CardView GeneralStatementCard;
    CardView LoanStatementCard;
    NestedScrollView mainScrollView;
    RelativeLayout userToolbar;
    RelativeLayout userDetGroup;
    Toolbar userToolbarEx;
    ImageView optionsButton;
    ImageView businessMan;
    ImageView finiIcons;
    TextView userContribution;
    TextView userName;
    TextView memberID;

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showBalanceBtn = findViewById(R.id.showBalance);

        accountDetailsRel = findViewById(R.id.accountDetails);

        PartialWithdrawalCard = findViewById(R.id.partialWithdrawalCard);

        LoanApplicationCard = findViewById(R.id.loanCard);

        GeneralStatementCard = findViewById(R.id.generalStatementCard);

        LoanStatementCard = findViewById(R.id.loanStatementCard);

        mainScrollView = findViewById(R.id.mainScrollView);

        userToolbar = findViewById(R.id.userToolbar);

        userDetGroup = findViewById(R.id.userDetGroup);

        userToolbarEx = findViewById(R.id.profileToolbar);
        optionsButton = findViewById(R.id.optionsMenu);

        businessMan=findViewById(R.id.businessMan);
        finiIcons=findViewById(R.id.Finiicons);

        userName=findViewById(R.id.profileName);
        userContribution=findViewById(R.id.userAccountBalance);
        memberID=findViewById(R.id.userMemberID);

        SharedPreferences UserDetails=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");
        String ContibutionBal=UserDetails.getString("contributionBal","n/a");

        userName.setText(UserName);
        memberID.setText(MemberId);
        userContribution.setText("GHS "+ContibutionBal);
        Log.d(TAG, "onCreate: "+memberID);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.main_menu);
                popup.show();
            }
        });


        ActivityResultLauncher<Intent> authActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            showBalanceBtn.setVisibility(View.GONE);
                            accountDetailsRel.setVisibility(View.VISIBLE);
                        }
                    }
                });


        showBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                    if (km.isKeyguardSecure()) {
                        Intent auhtIntent = km.createConfirmDeviceCredentialIntent("SHOW BALANCE", "ENTER YOUR PHONE PASSWORD");
                        authActivityResultLauncher.launch(auhtIntent);

                    }
                }


            }
        });


        PartialWithdrawalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PartialWithdrawal.class);
                startActivity(intent);
            }
        });

        LoanApplicationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, LoanApplication.class);
                startActivity(intent);

            }
        });

        GeneralStatementCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GeneralStatements.class);
                startActivity(intent);
            }
        });

        LoanStatementCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, LoanStatements.class);
                startActivity(intent);
            }
        });


        mainScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG, "onScrollChange:y value is : " + scrollY);
                if (scrollY > 95) {

                    userDetGroup.setVisibility(View.GONE);
                    userToolbar.setVisibility(View.VISIBLE);
                    businessMan.setVisibility(View.GONE);
                    finiIcons.setVisibility(View.VISIBLE);




                } else {
                    userDetGroup.setVisibility(View.VISIBLE);
                    userToolbar.setVisibility(View.GONE);
                    businessMan.setVisibility(View.VISIBLE);
                    finiIcons.setVisibility(View.INVISIBLE);

                }


            }
        });

//        userToolbarEx.setNavigationIcon(R.drawable.ic_baseline_more_vert_24);
//        userToolbarEx.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_AUTHENTICATE) {
            if (requestCode == RESULT_OK) {

                accountDetailsRel.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
       // Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                return true;
            case R.id.nav_help:
                // do your code
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onBackPressed() {

       moveTaskToBack(true);
    }
}