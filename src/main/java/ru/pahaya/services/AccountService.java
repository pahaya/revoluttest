package ru.pahaya.services;

import ru.pahaya.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {

    Account create(String id, BigDecimal money);

    Account create(BigDecimal money);

    Optional<Account> get(String id);

   boolean delete(String id);

   boolean withdraw(Account account, BigDecimal money);
}
