package ru.pahaya.services;

import ru.pahaya.entity.Account;
import ru.pahaya.entity.Transaction;

import java.math.BigDecimal;

public interface TransactionService {

    boolean process(Account from, Account to, BigDecimal money);

    boolean refund(String transactionId);

    Transaction get(String id);
}
