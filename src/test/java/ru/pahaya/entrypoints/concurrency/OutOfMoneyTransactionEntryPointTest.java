package ru.pahaya.entrypoints.concurrency;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.pahaya.entity.Account;
import ru.pahaya.entity.AccountVO;
import ru.pahaya.entity.Transaction;
import ru.pahaya.entrypoints.AccountEntryPoint;
import ru.pahaya.entrypoints.AccountEntryPointTest;
import ru.pahaya.entrypoints.TransactionEntryPoint;

import javax.ws.rs.BadRequestException;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is concurrent test
 * This class tests the situation, when multiple threads trying to withdraw money and money is not enough .
 */
@RunWith(ConcurrentTestRunner.class)
public class OutOfMoneyTransactionEntryPointTest {

    private final static TransactionEntryPoint TRANSACTION_ENTRY_POINT = new TransactionEntryPoint();
    private final static AccountEntryPoint ACCOUNT_ENTRY_POINT = new AccountEntryPoint();
    private final static AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
    private static final Gson gson = new Gson();
    private static final String MAIN = "MAIN";
    private final static int COUNT = 500;
    private static final String SECOND = "SECOND";
    private static final Logger logger = LogManager.getLogger(OutOfMoneyTransactionEntryPointTest.class);

    /**
     * Create 2 accounts with balance = 250
     */
    @Before
    public void setUp() {
        ACCOUNT_ENTRY_POINT.create(gson.toJson(new Account(MAIN, new BigDecimal(String.valueOf(COUNT / 2)))));
        ACCOUNT_ENTRY_POINT.create(gson.toJson(new Account(SECOND, new BigDecimal(String.valueOf(COUNT / 2)))));
    }

    /**
     * Check the results
     */
    @After
    public void check() {
        AccountVO accountMain = gson.fromJson(ACCOUNT_ENTRY_POINT.get(MAIN), AccountVO.class);
        AccountVO accountSecond = gson.fromJson(ACCOUNT_ENTRY_POINT.get(SECOND), AccountVO.class);
        Assert.assertEquals(accountMain.getMoney(), new BigDecimal("0"));
        Assert.assertEquals(accountSecond.getMoney(), new BigDecimal(COUNT));
        Assert.assertEquals(new BigDecimal(ATOMIC_INTEGER.get()), new BigDecimal(COUNT / 2));
    }

    /**
     * Withdraw money with 500 threads. Each thread should withdraw 1 point.
     * We must catch 250 exceptions - not enough money.
     */
    @Test
    @ThreadCount(COUNT)
    public void notEnoughMoneyTest() {
        try {
            TRANSACTION_ENTRY_POINT.process(gson.toJson(new Transaction(MAIN, SECOND, new BigDecimal(1))));
        } catch (BadRequestException e) {
            ATOMIC_INTEGER.incrementAndGet();
        }
    }

}