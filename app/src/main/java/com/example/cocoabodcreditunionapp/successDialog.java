package com.example.cocoabodcreditunionapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class successDialog {

    Context context;

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.request_success_alert);


        context= activity.getApplicationContext();

        TextView dialogButton =  dialog.findViewById(R.id.DoneBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, PartialWithdrawal.class);
                activity.startActivity(intent);
            }
        });

        dialog.show();

    }




}
