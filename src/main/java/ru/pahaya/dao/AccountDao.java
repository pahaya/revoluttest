package ru.pahaya.dao;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class AccountDao {
    private static final ConcurrentHashMap<String, Account> DB = new ConcurrentHashMap<>();
    public Account create(String id, BigDecimal money) {
        return DB.computeIfAbsent(id, (key) -> new Account(key, money));
    }
}
