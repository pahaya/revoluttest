package ru.pahaya.services;

import ru.pahaya.entity.Account;
import ru.pahaya.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    /**
     * Transfer money from one account ot another
     * @param from account from
     * @param to account to
     * @param money amount of money
     * @return result of operation
     */
    boolean process(Account from, Account to, BigDecimal money);

    /**
     * Invalidates the transaction
     * @param transactionId id of transaction
     * @return
     */
    boolean refund(String transactionId);

    /**
     * Get transaction from the system
     * @param id of the transaction
     * @return Transaction from the system
     */
    Transaction get(String id);

    /**
     * Get all client transactions
     * @param clientId client id
     * @return List of transactions
     */
    List<Transaction> getByClientId(String clientId);
}
