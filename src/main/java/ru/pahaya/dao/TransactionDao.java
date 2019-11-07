package ru.pahaya.dao;

import ru.pahaya.entity.Account;
import ru.pahaya.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TransactionDao {

    private static final ConcurrentHashMap<String, Transaction> DB = new ConcurrentHashMap<>();

    public Optional<Transaction> get(String id) {
        return Optional.ofNullable(DB.get(id));
    }

    public boolean remove(Transaction transaction) {
        return DB.remove(transaction.getId(), transaction);
    }

    public Transaction create(Account fromAccount, Account toAccount, BigDecimal money) {
        String id = UUID.randomUUID().toString();
        return DB.computeIfAbsent(id, (key) -> new Transaction(id, fromAccount.getId(), toAccount.getId(), money));
    }

    public List<Transaction> getByClientId(String clientId) {
        return DB.values().stream().filter(transaction -> transaction
                .getFromAccount().contains(clientId)).collect(Collectors.toList());
    }
}
