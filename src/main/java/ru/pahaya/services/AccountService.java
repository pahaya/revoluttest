package ru.pahaya.services;

import ru.pahaya.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    /**
     * Create account with id and initial money
     * @param id of the account
     * @param money initial money
     * @return created account
     */
    Account create(String id, BigDecimal money);

    /**
     * Create account with initial money
     * @param money initial money
     * @return created account
     */
    Account create(BigDecimal money);

    /**
     * Get account from the service
     * @param id of the account
     * @return account from the storage
     */
    Optional<Account> get(String id);

    /**
     * Removes account from the system
     * @param id of the account
     * @return result of operation
     */
   boolean delete(String id);

    /**
     * Subtracts money from an account
     * @param account account for subtraction
     * @param money money
     * @return result of operation
     */
   boolean withdraw(Account account, BigDecimal money);
}
