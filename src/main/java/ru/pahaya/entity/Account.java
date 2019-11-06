package ru.pahaya.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final String id;
    private final BigDecimal money;
    private transient final Lock lock =  new ReentrantLock();

    public Account(String id, BigDecimal money) {
        this.id = id;
        this.money = money;
    }

    public Lock getLock() {
        return lock;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Account add(BigDecimal someMoney) {
        return new Account(id, money.add(someMoney));
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
