package ru.pahaya.services;

import ru.pahaya.entity.Account;
import ru.pahaya.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    boolean process(Account from, Account to, BigDecimal money);

    boolean refund(String transactionId);

    Transaction get(String id);

    List<Transaction> getByClientId(String clientId);
}
