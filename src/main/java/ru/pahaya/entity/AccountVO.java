package ru.pahaya.entity;

import java.math.BigDecimal;

public class AccountVO {

    private final String id;
    private final BigDecimal money;

    public AccountVO(String id, BigDecimal money) {
        this.id = id;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
    }
}
