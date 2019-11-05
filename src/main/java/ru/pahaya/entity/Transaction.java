package ru.pahaya.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final String id;
    private final String fromAccount;
    private final String toAccount;
    private final BigDecimal money;

    public Transaction(String fromAccount, String toAccount, BigDecimal money) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.money = money;
        this.id = UUID.randomUUID().toString();
    }

    public Transaction(String id, String fromAccount, String toAccount, BigDecimal money) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public BigDecimal getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fromAccount, that.fromAccount) &&
                Objects.equals(toAccount, that.toAccount) &&
                Objects.equals(money, that.money);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, fromAccount, toAccount, money);
    }
}
