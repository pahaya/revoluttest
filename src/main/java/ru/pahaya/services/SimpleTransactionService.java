package ru.pahaya.services;

import ru.pahaya.ServiceHolder;
import ru.pahaya.dao.TransactionDao;
import ru.pahaya.entity.Account;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class SimpleTransactionService implements TransactionService {

    private static final TransactionDao TRANSACTION_DAO = new TransactionDao();
    private static final AccountService ACCOUNT_SERVICE = ServiceHolder.getAccountService();

    @Override
    public boolean process(Account from, Account to, BigDecimal money) {
        Lock first;
        Lock second;
        if (from.getId().hashCode() > to.getId().hashCode()) {
            first = from.getLock();
            second = to.getLock();
        } else {
            second = from.getLock();
            first = to.getLock();
        }
        boolean lockedFirst = false;
        boolean lockedSecond = false;
        try {
            lockedFirst = first.tryLock(100, TimeUnit.MILLISECONDS);
            lockedSecond = second.tryLock(100, TimeUnit.MILLISECONDS);
            if (lockedFirst && lockedSecond) {
                if (ACCOUNT_SERVICE.withdraw(from, money.negate())) {
                    if (ACCOUNT_SERVICE.withdraw(to, money)) {
                        TRANSACTION_DAO.create(from, to, money);
                        return true;
                    } else {
                        ACCOUNT_SERVICE.withdraw(from, money);
                    }
                }
            }
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (lockedFirst) {
                first.unlock();
            }
            if (lockedSecond) {
                second.unlock();
            }
        }
    }

    @Override
    public void refund(String transactionId) {

    }
}
