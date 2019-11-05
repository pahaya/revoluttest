package ru.pahaya.services;

import ru.pahaya.entity.Account;

import java.math.BigDecimal;

public interface TransactionService {

    boolean process(Account from, Account to, BigDecimal money);

    void refund(String transactionId);

}
