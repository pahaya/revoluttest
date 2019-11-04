package ru.pahaya.services;

import ru.pahaya.dao.Account;
import ru.pahaya.dao.AccountDao;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SimpleAccountService implements AccountService {

    private static final AccountDao ACCOUNT_DAO = new AccountDao();

    @Override
    public Account create(String id, BigDecimal money) {
        return ACCOUNT_DAO.create(id, money);
    }

    @Override
    public Account create(BigDecimal money) {
        UUID id = UUID.randomUUID();
        return ACCOUNT_DAO.create(id.toString(), money);
    }

    @Override
    public Optional<Account> get(String id) {
        return ACCOUNT_DAO.get(id);
    }


}