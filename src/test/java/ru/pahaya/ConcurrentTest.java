package ru.pahaya;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.pahaya.entity.Account;
import ru.pahaya.entity.Transaction;
import ru.pahaya.entrypoints.AccountEntryPoint;
import ru.pahaya.entrypoints.AccountEntryPointTest;
import ru.pahaya.entrypoints.TransactionEntryPoint;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentTest {

    private final static ExecutorService executor = Executors.newFixedThreadPool(100);
    private static final Logger logger = LogManager.getLogger(AccountEntryPointTest.class);
    private final static long COUNT_OF_MONEY = 1000;
    private final static TransactionEntryPoint TRANSACTION_ENTRY_POINT = new TransactionEntryPoint();
    private final static AccountEntryPoint ACCOUNT_ENTRY_POINT = new AccountEntryPoint();
    private static final Gson gson = new Gson();
    private static final String MAIN = "MAIN";

    public static void main(String[] args) throws InterruptedException {

        ACCOUNT_ENTRY_POINT.create(gson.toJson(new Account(MAIN, new BigDecimal(String.valueOf(COUNT_OF_MONEY)))));

        for (int i = 0; i < COUNT_OF_MONEY; i++) {
            ACCOUNT_ENTRY_POINT.create(gson.toJson(new Account(String.valueOf(i), new BigDecimal("0"))));
        }

        for (int i = 0; i < COUNT_OF_MONEY; i++) {
            int ii = i;
            executor.submit(() -> logger.info(TRANSACTION_ENTRY_POINT.process(gson.toJson(new Transaction(MAIN, String.valueOf(ii), new BigDecimal("1"))))));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        logger.info("Count of money in main account is {}", ACCOUNT_ENTRY_POINT.get(MAIN));
    }
}
