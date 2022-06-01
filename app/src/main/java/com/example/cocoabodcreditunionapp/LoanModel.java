package com.example.cocoabodcreditunionapp;



public class LoanModel {

    String date;
    String loanAmount;
    String debt;
    String installment;
    String balance;

    public LoanModel(String date, String loanAmount, String debt, String installment, String balance) {
        this.date = date;
        this.loanAmount = loanAmount;
        this.debt = debt;
        this.installment = installment;
        this.balance = balance;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }





}
