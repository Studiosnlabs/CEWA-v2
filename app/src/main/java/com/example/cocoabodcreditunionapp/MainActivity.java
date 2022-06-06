package com.example.cocoabodcreditunionapp;

import static java.lang.Thread.sleep;

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
import android.os.CountDownTimer;
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

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener , Runnable {
    private static final String TAG = "MainActivity";
    private static final int INTENT_AUTHENTICATE = 20;
    Button showBalanceBtn;
    RelativeLayout accountDetailsRel;
    RelativeLayout LaccountDetailsRel;
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
    TextView myAccountLabel;
    TextView loanBalance;
    Button showLoanBalanceBtn;
    DecimalFormat df=new DecimalFormat("#,###.00");
    CountDownTimer expiry;
    String timerActive="";
    Context context;


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

        showLoanBalanceBtn=findViewById(R.id.LshowBalance);

        accountDetailsRel = findViewById(R.id.accountDetails);
        LaccountDetailsRel=findViewById(R.id.LaccountDetails);

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

        myAccountLabel=findViewById(R.id.accountLabel);

        userName=findViewById(R.id.profileName);
        userContribution=findViewById(R.id.userAccountBalance);
        loanBalance=findViewById(R.id.LuserAccountBalance);
        memberID=findViewById(R.id.userMemberID);

        context=getApplicationContext();

        SharedPreferences UserDetails=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");
        String ContributionBal=UserDetails.getString("contributionBal","n/a");
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");


        Double ContributionBalance=Double.valueOf(ContributionBal);
        Double LoanBalance=Double.valueOf(loanBal);
        userName.setText(UserName);
        memberID.setText(MemberId);
        userContribution.setText("GHS "+df.format(ContributionBalance));
        loanBalance.setText("GHS "+df.format(LoanBalance));
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

        ActivityResultLauncher<Intent> LauthActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            showLoanBalanceBtn.setVisibility(View.GONE);
                            LaccountDetailsRel.setVisibility(View.VISIBLE);
                        }
                    }
                });

        showBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                    if (km.isKeyguardSecure()) {
                        Intent auhtIntent = km.createConfirmDeviceCredentialIntent("SHOW BALANCE", "UNLOCK YOUR PHONE");
                        authActivityResultLauncher.launch(auhtIntent);

                    }
                }


            }
        });


        showLoanBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                    if (km.isKeyguardSecure()) {
                        Intent auhtIntent = km.createConfirmDeviceCredentialIntent("SHOW BALANCE", "UNLOCK YOUR PHONE");
                        LauthActivityResultLauncher.launch(auhtIntent);

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
                if (scrollY > 90) {

                    userDetGroup.setVisibility(View.GONE);
                    userToolbar.setVisibility(View.VISIBLE);
                    businessMan.setVisibility(View.GONE);
                    finiIcons.setVisibility(View.VISIBLE);
                    myAccountLabel.setVisibility(View.GONE);




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
                SharedPreferences settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                settings.edit().clear().commit();
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
        Log.d(TAG, "onStop: app has been moved to the back");
        expireApp();

    }

    @Override
    protected void onResume() {

        Log.d(TAG, "onResume: App has resumed");
        
        
        super.onResume();
    }


    @Override
    protected void onPause() {

//        if (!this.isFinishing()){
//            Log.d(TAG, "onPause: app has paused");
//            super.onPause();
//        } else {
//
//            Log.d(TAG, "onStop: app has stopped");
//            expireApp();
//
//        }

        super.onPause();
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