package ru.pahaya.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.pahaya.ServiceHolder;
import ru.pahaya.dao.TransactionDao;
import ru.pahaya.entity.Account;
import ru.pahaya.entity.Transaction;

import javax.ws.rs.BadRequestException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class SimpleTransactionService implements TransactionService {

    private static final TransactionDao TRANSACTION_DAO = new TransactionDao();
    private static final AccountService ACCOUNT_SERVICE = ServiceHolder.getAccountService();
    private static final Logger logger = LogManager.getLogger(SimpleTransactionService.class);

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
            logger.error("Exception during lock wait!", e);
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
    public boolean refund(String transactionId) {
        Optional<Transaction> tr = TRANSACTION_DAO.get(transactionId);
        if (tr.isEmpty()) {
            throw new BadRequestException("Wrong transaction id !");
        }
        Optional<Account> from = ACCOUNT_SERVICE.get(tr.get().getFromAccount());
        Optional<Account> to = ACCOUNT_SERVICE.get(tr.get().getToAccount());
        if (to.isEmpty() || from.isEmpty()) {
            throw new BadRequestException("Wrong transaction id !");
        }
        return process(to.get(), from.get(), tr.get().getMoney());
    }

    @Override
    public Transaction get(String id) {
        Optional<Transaction> transaction = TRANSACTION_DAO.get(id);
        if (transaction.isEmpty()) {
            throw new BadRequestException("Wrong transaction id !");
        }
        return transaction.get();
    }
}
