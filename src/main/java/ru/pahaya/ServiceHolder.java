package ru.pahaya;

import ru.pahaya.services.AccountService;
import ru.pahaya.services.SimpleAccountService;
import ru.pahaya.services.SimpleTransactionService;
import ru.pahaya.services.TransactionService;

public class ServiceHolder {

    private static final AccountService ACCOUNT_SERVICE = new SimpleAccountService();
    private static final TransactionService TRANSACTION_SERVICE = new SimpleTransactionService();

    public static AccountService getAccountService() {
        return ACCOUNT_SERVICE;
    }

    public static TransactionService getTransactionService() {
        return TRANSACTION_SERVICE;
    }
}
