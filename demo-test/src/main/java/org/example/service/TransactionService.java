package org.example.service;

import org.example.exception.NotEnoughMoneyException;
import org.example.model.BankAccount;

public class TransactionService {

    public boolean executeTransaction(BankAccount debitAccount, BankAccount creditAccount, Double amount) {
        if (debitAccount == null || creditAccount == null || amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid transaction parameters.");
        }

        if (debitAccount.getBalance() < amount) {
            throw new NotEnoughMoneyException("Not enough money in the debit account.");
        }

        debitAccount.debit(amount);
        creditAccount.credit(amount);
        return true;
    }
}
