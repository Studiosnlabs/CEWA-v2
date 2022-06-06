package com.example.cocoabodcreditunionapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.webkit.JsPromptResult;
import android.webkit.MimeTypeMap;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullLoanStatement extends AppCompatActivity implements Runnable{

    TextView StaffName,MemberID,Division,StaffId,Contact;
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
    String fromDate;
    String toDate;
    FloatingActionButton downloadBtn;

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


    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    RelativeLayout payslipCons;
    TextView loanMonthYearTitle;
    private static final String TAG = "FullLoanStatement";
    public static final String ANDROID_CHANNEL_ID = "com.example.CocobodCreditUnionAppC2";


    private static final int PERMISSION_REQUEST_CODE = 200;

    private boolean checkPermission() {

        // checking of permissions.

        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
        }


    }


    private Bitmap bitmap;

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }


    private File createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float pageWidth = displaymetrics.heightPixels;
        float pageHeight = displaymetrics.widthPixels;

        int convertHighet = (int) pageHeight, convertWidth = (int) pageWidth;

        convertHighet = convertHighet + convertWidth;


        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Rect src = new Rect(0, 0, payslipCons.getWidth(), payslipCons.getHeight());

        Canvas canvas = page.getCanvas();
//
//        float scale = Math.min(pageWidth/src.width(), pageHeight/src.height());
//        float left = pageWidth / 2 - src.width() * scale / 2;
//        float top = pageHeight / 2 - src.height() * scale / 2;
//        float right = pageWidth / 2 + src.width() * scale / 2;
//        float bottom = pageHeight / 2 + src.height() * scale / 2;
//        RectF dst = new RectF(left, top, right, bottom);

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        File file = new File(Environment.getExternalStorageDirectory(), FromDate + ToDate + "Payslip.pdf");
//        String targetPdf = "/Internal storage/newpayslip.pdf";
//        File filePath;
//        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF Downloaded", Toast.LENGTH_SHORT).show();

        //openGeneratedPDF();

        return file;

    }


    private void openGeneratedPDF() {
        File file = new File(FromDate + ToDate + "Payslip.pdf");
        if (checkPermission()) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            Uri fileUri = FileProvider.getUriForFile(FullLoanStatement.this, FullLoanStatement.this.getApplicationContext().getPackageName() + ".provider", createPdf());
            Intent openFile = new Intent(Intent.ACTION_VIEW, fileUri);
            openFile.setDataAndType(fileUri, type);
            openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(openFile);
            } catch (Exception e) {
                Toast.makeText(FullLoanStatement.this, "No application found to open pdf", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {

                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);

            } catch (Exception e) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);

            }

        }
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ANDROID_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }




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


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        gestureDetector = new GestureDetector(this, new FullLoanStatement.GestureListener());

        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scale = 1 - detector.getScaleFactor();

                float prevScale = mScale;
                mScale += scale;

                if (mScale < 0.1f) // Minimum scale condition:
                    mScale = 0.1f;

                if (mScale > 10f) // Maximum scale condition:
                    mScale = 10f;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);
                ScrollView layout = (ScrollView) findViewById(R.id.scrollView2);
                layout.startAnimation(scaleAnimation);
                return true;
            }
        });

        if (checkPermission()) {
            Log.d(TAG, "onCreate: Permission is already granted");
        } else {
            requestPermission();
        }



        StaffName=findViewById(R.id.loanStaffName);
        MemberID=findViewById(R.id.loanMemberId);
        Division=findViewById(R.id.loanDivision);
        StaffId=findViewById(R.id.loanstaffId);
        Contact=findViewById(R.id.loanContact);
        loanMonthYearTitle=findViewById(R.id.MonthYearTitle);
        loanContScroll=findViewById(R.id.scrollView2);
        loadingTxt=findViewById(R.id.loadingtxt);
        recyclerView=findViewById(R.id.recycler_view);
        downloadBtn=findViewById(R.id.fab);
        payslipCons = findViewById(R.id.payslipRel);

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

         fromDate= getIntent().getStringExtra("monthFrom") + " "+ getIntent().getStringExtra("yearFrom");
         toDate=getIntent().getStringExtra("monthTo")+ " "+ getIntent().getStringExtra("yearTo");
        loanMonthYearTitle.setText("Loan Statement from "+ fromDate+" to " +toDate);
        Log.d(TAG, "onCreate: dates"+fromDate + toDate);




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


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("size", " " + payslipCons.getWidth() + "  " + payslipCons.getWidth());
                bitmap = loadBitmapFromView(payslipCons, payslipCons.getWidth(), payslipCons.getHeight());
                createPdf();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                File file = new File(fromDate + toDate + "loanStatement.pdf");
                if (checkPermission()) {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    String type = mime.getMimeTypeFromExtension(ext);
                    Uri fileUri = FileProvider.getUriForFile(FullLoanStatement.this, FullLoanStatement.this.getApplicationContext().getPackageName() + ".provider", createPdf());
                    Intent openFile = new Intent(Intent.ACTION_VIEW, fileUri);
                    openFile.setDataAndType(fileUri, type);
                    openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PendingIntent p = PendingIntent.getActivity(FullLoanStatement.this, 0, openFile, 0);


                    createNotificationChannel();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                            .setContentTitle("CEWA Loan Statement Downloaded")
                            .setContentText("Your " + fromDate+" to "+ toDate + " loan statement has been downloaded")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Your " + fromDate+" to "+ toDate + " loan statement has been downloaded to files"))
                            .setSmallIcon(R.drawable.cewalogo)
                            .setContentIntent(p)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    Log.d(TAG, "onNotify: Permission is already granted");
                    int NotifId = 6;


// notificationId is a unique int for each notification that you must define


                    notificationManager.notify(NotifId, builder.build());

                    startActivity(openFile);


                } else {
                    requestPermission();
                    Log.d(TAG, "onClickNotify:Could not open file");
                    Toast.makeText(FullLoanStatement.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
                }






            }
        });










    }



    private void setRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loanAdapter = new LoanAdapter(arrayList);
        recyclerView.setAdapter(loanAdapter);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    private static class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // double tap fired.
            return true;
        }

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