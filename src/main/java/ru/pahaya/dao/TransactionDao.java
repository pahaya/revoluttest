package ru.pahaya.dao;

import ru.pahaya.entity.Account;
import ru.pahaya.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * DAO for Transactions
 */
public class TransactionDao {

    private static final ConcurrentHashMap<String, Transaction> DB = new ConcurrentHashMap<>();

    /**
     * Get Transaction from DB
     * @param id id of Transaction
     * @return Transaction from DB
     */
    public Optional<Transaction> get(String id) {
        return Optional.ofNullable(DB.get(id));
    }

    /**
     * Create record in DB about transaction
     * @param fromAccount source of money
     * @param toAccount destination of money
     * @param money money
     * @return created Transaction
     */
    public Transaction create(Account fromAccount, Account toAccount, BigDecimal money) {
        String id = UUID.randomUUID().toString();
        return DB.computeIfAbsent(id, (key) -> new Transaction(id, fromAccount.getId(), toAccount.getId(), money));
    }

    /**
     * Get all client transactions
     * @param clientId client id
     * @return List of transactions
     */
    public List<Transaction> getByClientId(String clientId) {
        return DB.values().stream().filter(transaction -> transaction
                .getFromAccount().contains(clientId) && !transaction.getRefunded().get()).collect(Collectors.toList());
    }
}
