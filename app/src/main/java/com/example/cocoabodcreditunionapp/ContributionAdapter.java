package com.example.cocoabodcreditunionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContributionAdapter extends RecyclerView.Adapter<ContributionAdapter.ViewHolder> {


    ArrayList<ContributionModel> ContributionModelArrayList ;


    public ContributionAdapter( ArrayList<ContributionModel> ContributionModelArraylist) {
        this.ContributionModelArrayList =ContributionModelArraylist;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.contributionitem_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ContributionModel contributionModel= ContributionModelArrayList.get(position);

        holder.Date.setText(contributionModel.getContDate());
        holder.Description.setText(contributionModel.getDescription());
        holder.TotalAmount.setText(contributionModel.getTotalAmount());
        holder.Balance.setText(contributionModel.getContBalance());





    }


    @Override
    public int getItemCount() {
        return ContributionModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Date,Description,TotalAmount,Balance;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            Date=itemView.findViewById(R.id.contributionDate);
            Description=itemView.findViewById(R.id.contributionDescription);
            TotalAmount =itemView.findViewById(R.id.contributionTotalAmount);
            Balance=itemView.findViewById(R.id.contributionBalance);

        }

    }
}
