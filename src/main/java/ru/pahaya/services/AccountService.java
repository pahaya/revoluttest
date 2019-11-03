package ru.pahaya.services;

import ru.pahaya.dao.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account create(String id, BigDecimal money);

    Account create(BigDecimal money);
}
