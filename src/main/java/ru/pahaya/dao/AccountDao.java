package ru.pahaya.dao;

import ru.pahaya.entity.Account;

import javax.ws.rs.BadRequestException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Account DAO uses concurrent hash map.
 */
public class AccountDao {

    private static final ConcurrentHashMap<String, Account> DB = new ConcurrentHashMap<>();

    /**
     * Create new account
     *
     * @param id    account if
     * @param money initial money
     * @return account
     */

    public Account create(String id, BigDecimal money) {
        return DB.compute(id, (key, account) -> {
                    if (account != null) {
                        throw new BadRequestException("Account already exist.");
                    } else {
                        return new Account(key, money);
                    }
                }
        );
    }

    /**
     * Get Account from DB
     * @param id id of Account
     * @return Account from DB
     */
    public Optional<Account> get(String id) {
        return Optional.ofNullable(DB.get(id));
    }

    /**
     * Remove account from the DB
     * @param id id of account
     * @return result of operation
     */
    public boolean remove(String id) {
        return DB.remove(id) != null;
    }

}
