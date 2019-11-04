package ru.pahaya.services;

import ru.pahaya.dao.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    Account create(String id, BigDecimal money);

    Account create(BigDecimal money);

    Optional<Account> get(String id);
}
