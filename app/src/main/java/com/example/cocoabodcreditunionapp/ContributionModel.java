package com.example.cocoabodcreditunionapp;

public class ContributionModel {

    String ContDate;
    String Description;
    String TotalAmount;
    String ContBalance;

    public ContributionModel(String contDate, String description, String totalAmount, String contBalance) {
        ContDate = contDate;
        Description = description;
        TotalAmount = totalAmount;
        ContBalance = contBalance;
    }

    public String getContDate() {
        return ContDate;
    }

    public void setContDate(String contDate) {
        ContDate = contDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getContBalance() {
        return ContBalance;
    }

    public void setContBalance(String contBalance) {
        ContBalance = contBalance;
    }
}
