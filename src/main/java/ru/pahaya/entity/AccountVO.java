package ru.pahaya.entity;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountVO accountVO = (AccountVO) o;
        return Objects.equals(id, accountVO.id) &&
                Objects.equals(money, accountVO.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, money);
    }
}
