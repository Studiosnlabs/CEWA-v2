package com.example.cocoabodcreditunionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {


    ArrayList<LoanModel> LoanModelArrayList ;


    public LoanAdapter( ArrayList<LoanModel> LoanModelArraylist) {
        this.LoanModelArrayList =LoanModelArraylist;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.loanitem_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LoanModel billingModel= LoanModelArrayList.get(position);

        holder.Date.setText(billingModel.getDate());
        holder.LoanAmount.setText(billingModel.getLoanAmount());
        holder.Debt.setText(billingModel.getDebt());
        holder.Installment.setText(billingModel.getInstallment());
        holder.Balance.setText(billingModel.getBalance());





    }


    @Override
    public int getItemCount() {
        return LoanModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Date,LoanAmount,Debt,Installment,Balance;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            Date=itemView.findViewById(R.id.date);
            LoanAmount=itemView.findViewById(R.id.loanAmount);
            Debt =itemView.findViewById(R.id.Debt);
            Installment=itemView.findViewById(R.id.Installment);
            Balance=itemView.findViewById(R.id.Balance);

        }

    }
}