package org.example.model;

public class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void debit(double amount) {
        this.balance -= amount;
    }

    public void credit(double amount) {
        this.balance += amount;
    }
}
