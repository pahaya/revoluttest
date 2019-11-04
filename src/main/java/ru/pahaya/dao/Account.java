package ru.pahaya.dao;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    private final String id;
    private final BigDecimal money;

    public Account(String id, BigDecimal money) {
        this.id = id;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
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
