package ru.pahaya.dao;

import ru.pahaya.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AccountDao {

    private static final ConcurrentHashMap<String, Account> DB = new ConcurrentHashMap<>();

    public Account create(String id, BigDecimal money) {
        return DB.computeIfAbsent(id, (key) -> new Account(key, money));
    }

    public Optional<Account> get(String id) {
        return Optional.ofNullable(DB.get(id));
    }

    public boolean remove(String id) {
        return DB.remove(id) != null;
    }

    public boolean replace(String id, Account account, Account add) {
        return DB.replace(id, account, add);
    }
}
