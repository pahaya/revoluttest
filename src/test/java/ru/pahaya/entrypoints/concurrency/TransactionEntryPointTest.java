package ru.pahaya.entrypoints.concurrency;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.pahaya.entity.Account;
import ru.pahaya.entity.AccountVO;
import ru.pahaya.entity.Transaction;
import ru.pahaya.entrypoints.AccountEntryPoint;
import ru.pahaya.entrypoints.TransactionEntryPoint;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(ConcurrentTestRunner.class)
public class TransactionEntryPointTest {

    private final static TransactionEntryPoint TRANSACTION_ENTRY_POINT = new TransactionEntryPoint();
    private final static AccountEntryPoint ACCOUNT_ENTRY_POINT = new AccountEntryPoint();
    private final static AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
    private static final Gson gson = new Gson();
    private static final String MAIN = "MAIN2";
    private final static int COUNT = 500;

    @Before
    public void setUp() {
        ACCOUNT_ENTRY_POINT.create(gson.toJson(new Account(MAIN, new BigDecimal(String.valueOf(COUNT)))));
        for (int i = 0; i < COUNT; i++) {
            ACCOUNT_ENTRY_POINT.create(gson.toJson(new Account(String.valueOf(i), new BigDecimal("0"))));
        }
    }

    @After
    public void tearDown() {
        AccountVO accountVO = gson.fromJson(ACCOUNT_ENTRY_POINT.get(MAIN), AccountVO.class);
        Assert.assertEquals(accountVO.getMoney(), new BigDecimal("0"));
    }

    @Test
    public void get() {

    }

    @Test
    @ThreadCount(COUNT)
    public void process() {
        TRANSACTION_ENTRY_POINT.process(gson.toJson(new Transaction(MAIN, String.valueOf(ATOMIC_INTEGER.getAndIncrement()),
                new BigDecimal("1"))));
    }

    @Test
    public void delete() {
    }
}