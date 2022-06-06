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
import android.webkit.MimeTypeMap;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FullGeneralStatement extends AppCompatActivity implements Runnable {

    TextView StaffName, MemberID,Division,StaffId,Contact;
    TextView OpeningBalanceTV;
    ArrayList<String> Dates = new ArrayList<>();
    ArrayList<String> Description = new ArrayList<>();
    ArrayList<String> TotalAmount = new ArrayList<>();
    ArrayList<String> Balances = new ArrayList<>();
    ArrayList<ContributionModel> arrayList;
    RecyclerView recyclerView;
    ContributionModel contributionModel;
    ContributionAdapter contributionAdapter;
    ScrollView genContScroll;
    TextView loadingTxt;
    String pBearerToken;
    String FromDate;
    String ToDate;
    Double OpeningBalance;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    RelativeLayout payslipCons;
    TextView genMonthYearTitle;
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



    private static final String TAG = "FullGeneralStatement";
    public static final String ANDROID_CHANNEL_ID = "com.example.cocobodcreditunionapp";


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

    public String convertDate(String Tdate){

        String pattern = "dd MMMM yyyy";

        try {
            Date hiredDate = new SimpleDateFormat("yyyy-MM-dd").parse(Tdate);
            SimpleDateFormat humanReadableDate = new SimpleDateFormat(pattern);
            String date = humanReadableDate.format(Tdate);
            Log.d(TAG, "convertDate: "+date);
            System.out.println(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return Tdate;
        }

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
            Uri fileUri = FileProvider.getUriForFile(FullGeneralStatement.this, FullGeneralStatement.this.getApplicationContext().getPackageName() + ".provider", createPdf());
            Intent openFile = new Intent(Intent.ACTION_VIEW, fileUri);
            openFile.setDataAndType(fileUri, type);
            openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(openFile);
            } catch (Exception e) {
                Toast.makeText(FullGeneralStatement.this, "No application found to open pdf", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getDivision(JSONObject response){

        try {
            JSONObject division=response.getJSONObject("user");
            JSONObject divisionObj=division.getJSONObject("division");
            String divisionName=divisionObj.optString("name");
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



                Dates.add(date.optString("trans_date"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getDescription(JSONObject response){
        try {
            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject description = transactionArray.getJSONObject(i);



                Description.add(description.optString("trans_type"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    public void getTotalAmount(JSONObject response){
        try {
            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject totalAmount = transactionArray.getJSONObject(i);



                TotalAmount.add(totalAmount.optString("amount"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getBalance(JSONObject object,JSONObject response){

        try {
            OpeningBalance=response.optDouble("opening_balance");

            JSONArray transactionArray = response.getJSONArray("transactions");
            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject Amount = transactionArray.getJSONObject(i);

                if (i==0){
                    String AmountValue= Amount.getString("amount");
                    // Double OpeningBalValue=Double.valueOf(OpeningBalance);
                    Double BalanceValue=Double.valueOf(AmountValue)+OpeningBalance;
                    String Balance=BalanceValue.toString();



                    Balances.add(Balance);

                }else{

                    String AmountValue= Amount.getString("amount");
                     Double OpeningBalValue=Double.valueOf(Balances.get(i-1));
                    Log.d(TAG, "getBalance: "+OpeningBalValue);
                    Double BalanceValue=Double.valueOf(AmountValue)+OpeningBalValue;
                    String Balance=BalanceValue.toString();



                    Balances.add(Balance);

                }



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "getBalance: "+Balances);
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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_general_statement);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        gestureDetector = new GestureDetector(this, new GestureListener());

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



        StaffName=findViewById(R.id.genStaffName);
        MemberID=findViewById(R.id.genMemberId);
        Division=findViewById(R.id.genDivision);
        Contact=findViewById(R.id.genContact);
        genContScroll=findViewById(R.id.genscrollView2);
        loadingTxt=findViewById(R.id.genloadingtxt);
        StaffId=findViewById(R.id.genStaffId);
        recyclerView=findViewById(R.id.genrecycler_view);
        payslipCons = findViewById(R.id.genpayslipRel);
        genMonthYearTitle=findViewById(R.id.genMonthYearTitle);
        downloadBtn=findViewById(R.id.genFab);
        OpeningBalanceTV=findViewById(R.id.openingBalance);

        SharedPreferences UserDetails=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String UserName=UserDetails.getString("userName","n/a");
        String MemberId=UserDetails.getString("memberID","n/a");
        String ContributionBal=UserDetails.getString("contributionBal","n/a");
        String loanBal = UserDetails.getString("current_loan_balance", "n/a");
        String email=UserDetails.getString("email", "n/a");
        String staffId=UserDetails.getString("staff_number", "n/a");
        pBearerToken=UserDetails.getString("token","n/a");

        //Division.setText();
        StaffName.setText(UserName);
        MemberID.setText(MemberId);
        Contact.setText(email);
        StaffId.setText(staffId);

        FromDate=getIntent().getStringExtra("from");
        ToDate=getIntent().getStringExtra("to");


      String fromDate= getIntent().getStringExtra("monthFrom") + " "+ getIntent().getStringExtra("yearFrom");
      String toDate=getIntent().getStringExtra("monthTo")+ " "+ getIntent().getStringExtra("yearTo");
      genMonthYearTitle.setText("Member Statement from "+ fromDate+" to " +toDate);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.genStatement,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject message = object.optJSONObject("message");

                            assert message != null;
                            getDate(message);
                            getDescription(message);
                            getTotalAmount(message);
                            getBalance(object,message);
                            getDivision(message);



                            arrayList = new ArrayList<>();

                            int arraysize = Balances.size();

                            OpeningBalanceTV.setText(OpeningBalance.toString());

                            for (int position = 0; position < arraysize; position++) {
                                DecimalFormat df = new DecimalFormat("#,###.00");


                                String Balance = Balances.get(position);
                                Double balanceAmtD = Double.parseDouble(Balance);
                                String balanceAmountF = df.format(balanceAmtD);
                                Log.d(TAG, "onResponse: " + balanceAmountF);


                                String totalAmount = TotalAmount.get(position);
                                Double totalAmtD = Double.parseDouble(totalAmount);
                                String totalAmountF = df.format(totalAmtD);

                                String transType = Description.get(position);

                                String date = Dates.get(position);


                                //String hour=hours.get(position);
                                //String balance=balances.get(position);
                                //String rate=rates.get(position);

                                ContributionModel bill = new ContributionModel(date, transType, totalAmountF, balanceAmountF);
                                arrayList.add(bill);
                                setRecyclerView();


                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                              Log.d(TAG, "EmpPay: "+response);
                        genContScroll.setVisibility(View.VISIBLE);
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
                params3.put("statement_type", "GENERAL");


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


        //download pdf
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("size", " " + payslipCons.getWidth() + "  " + payslipCons.getWidth());
                bitmap = loadBitmapFromView(payslipCons, payslipCons.getWidth(), payslipCons.getHeight());
                createPdf();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                File file = new File(FromDate + ToDate + "Payslip.pdf");
                if (checkPermission()) {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    String type = mime.getMimeTypeFromExtension(ext);
                    Uri fileUri = FileProvider.getUriForFile(FullGeneralStatement.this, FullGeneralStatement.this.getApplicationContext().getPackageName() + ".provider", createPdf());
                    Intent openFile = new Intent(Intent.ACTION_VIEW, fileUri);
                    openFile.setDataAndType(fileUri, type);
                    openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PendingIntent p = PendingIntent.getActivity(FullGeneralStatement.this, 0, openFile, 0);


                    createNotificationChannel();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                            .setContentTitle("CEWA Member Statement Downloaded")
                            .setContentText("Your " + fromDate+" to "+ toDate + " member statement has been downloaded")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Your " + fromDate+" to "+ toDate + " member statement has been downloaded to files"))
                            .setSmallIcon(R.drawable.cewalogo)
                            .setContentIntent(p)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    Log.d(TAG, "onNotify: Permission is already granted");
                    int NotifId = 5;


// notificationId is a unique int for each notification that you must define


                    notificationManager.notify(NotifId, builder.build());

                    startActivity(openFile);


                } else {
                    requestPermission();
                    Log.d(TAG, "onClickNotify:Could not open file");
                    Toast.makeText(FullGeneralStatement.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
                }






            }
        });







    }

    private void setRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contributionAdapter = new ContributionAdapter(arrayList);
        recyclerView.setAdapter(contributionAdapter);

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