package ru.pahaya.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Account {

    private final String id;
    private volatile BigDecimal money;
    private transient final ReadWriteLock lock = new ReentrantReadWriteLock();

    public Account(String id, BigDecimal money) {
        this.id = id;
        this.money = money;
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getMoney() {
        //Locked mutation of the money in case somebody will use it without Transaction Service.
        lock.readLock().lock();
        try {
            return money;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void add(BigDecimal someMoney) {
        //Locked mutation of the money in case somebody will use it without Transaction Service.
        lock.writeLock().lock();
        try {
            money = money.add(someMoney);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(money, account.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, money);
    }
}
