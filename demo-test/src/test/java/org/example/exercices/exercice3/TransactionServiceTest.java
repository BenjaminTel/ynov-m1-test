package org.example.exercices.exercice3;

import org.example.exception.NotEnoughMoneyException;
import org.example.model.BankAccount;
import org.example.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private BankAccount debitAccount;
    private BankAccount creditAccount;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService();
        debitAccount = new BankAccount("12345", 500.0);
        creditAccount = new BankAccount("67890", 200.0);
    }

    @Test
    public void testTransactionSuccess() {
        boolean result = transactionService.executeTransaction(debitAccount, creditAccount, 100.0);
        assertTrue(result);
        assertEquals(400.0, debitAccount.getBalance());
        assertEquals(300.0, creditAccount.getBalance());
    }

    @Test
    public void testNullDebitAccount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.executeTransaction(null, creditAccount, 100.0);
        });
        assertEquals("Invalid transaction parameters.", exception.getMessage());
    }

    @Test
    public void testNullCreditAccount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.executeTransaction(debitAccount, null, 100.0);
        });
        assertEquals("Invalid transaction parameters.", exception.getMessage());
    }

    @Test
    public void testNegativeAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.executeTransaction(debitAccount, creditAccount, -100.0);
        });
        assertEquals("Invalid transaction parameters.", exception.getMessage());
    }

    @Test
    public void testNullAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.executeTransaction(debitAccount, creditAccount, null);
        });
        assertEquals("Invalid transaction parameters.", exception.getMessage());
    }

    @Test
    public void testNotEnoughMoney() {
        NotEnoughMoneyException exception = assertThrows(NotEnoughMoneyException.class, () -> {
            transactionService.executeTransaction(debitAccount, creditAccount, 600.0);
        });
        assertEquals("Not enough money in the debit account.", exception.getMessage());
    }

    @Test
    public void testTransactionTimeLimit() {
        assertTimeout(Duration.ofSeconds(1), () -> {
            transactionService.executeTransaction(debitAccount, creditAccount, 100.0);
        });
    }

    @ParameterizedTest
    @ValueSource(doubles = {100.0, 200.0, 50.0, -10.0})
    public void testParameterizedTransactionAmount(double amount) {
        if (amount <= 0) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                transactionService.executeTransaction(debitAccount, creditAccount, amount);
            });
            assertEquals("Invalid transaction parameters.", exception.getMessage());
        } else {
            boolean result = transactionService.executeTransaction(debitAccount, creditAccount, amount);
            assertTrue(result);
        }
    }

    @Test
    public void testTransactionWithMultipleAssertions() {
        assertAll("Transaction test",
                () -> assertTrue(transactionService.executeTransaction(debitAccount, creditAccount, 50.0)),
                () -> assertEquals(450.0, debitAccount.getBalance()),
                () -> assertEquals(250.0, creditAccount.getBalance())
        );
    }
}
