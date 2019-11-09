package ru.pahaya.entrypoints;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import ru.pahaya.Application;
import ru.pahaya.entity.AccountVO;
import ru.pahaya.entity.Result;
import ru.pahaya.entity.Transaction;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is concurrent test
 * This test starts Jetty, and use http communication.
 * It moves money into SECOND account, gets all transactions and do refund for all transactions
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(ConcurrentTestRunner.class)
public class TransactionEntryPointTest extends EntryPointTest {

    private static final Logger logger = LogManager.getLogger(TransactionEntryPointTest.class);
    private final static String MAIN = "MAIN";
    private final static String SECOND = "SECOND";
    private final static String account = "{\"id\":\"" + MAIN + "\",\"money\":1000}";
    private final static String account2 = "{\"id\":\"" + SECOND + "\",\"money\":0}";
    private static final Gson gson = new Gson();
    private static final int COUNT_THREADS = 100;
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(COUNT_THREADS);
    private static final CountDownLatch COUNT_DOWN_LATCH_GET_TRANSACTIONS = new CountDownLatch(1);
    private static final String HTTP_LOCALHOST_8080_TRANSACTION = "http://localhost:8080/transaction/";
    private static final String HTTP_LOCALHOST_8080_ACCOUNT = "http://localhost:8080/account/";
    private static volatile List transactions;
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Before
    public void startApp() {
        Application.startServer();
        createAccount(account);
        createAccount(account2);
    }

    @After
    public void stopApp() {
        checkAccount(MAIN, 1000);
        checkAccount(SECOND, 0);
        Application.stop();
    }

    @Test
    @ThreadCount(COUNT_THREADS)
    public void aMoveMoneyToAnotherAccount() {
        createTransactions(MAIN, SECOND);
        COUNT_DOWN_LATCH.countDown();
    }

    @Test
    @ThreadCount(1)
    public void bGetAllTransactions() {
        waitForMoneyTransfer();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_TRANSACTION + "/client/" + MAIN))
                .GET()
                .build();
        HttpResponse<String> response = getStringHttpResponse(client, request);
        Type listType = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        transactions = Collections.unmodifiableList(gson.fromJson(response.body(), listType));
        COUNT_DOWN_LATCH_GET_TRANSACTIONS.countDown();
    }

    @Test
    @ThreadCount(COUNT_THREADS)
    public void cRefund() {
        try {
            COUNT_DOWN_LATCH_GET_TRANSACTIONS.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int id = COUNTER.getAndIncrement();
        Transaction transaction = (Transaction) transactions.get(id);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_TRANSACTION))
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(transaction)))
                .build();
        HttpResponse<String> response = getStringHttpResponse(client, request);
        Result result = gson.fromJson(response.body(), Result.class);
        Assert.assertTrue(result.isResult());
    }

    private void waitForMoneyTransfer() {
        try {
            COUNT_DOWN_LATCH.await();
        } catch (InterruptedException e) {
            logger.error("Count Down doesn't work!", e);
        }
    }

    private void checkAccount(String account, int money) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_ACCOUNT + account))
                .GET()
                .build();
        HttpResponse<String> response = getStringHttpResponse(client, request);
        AccountVO accountFromResponse = gson.fromJson(response.body(), AccountVO.class);
        Assert.assertEquals(accountFromResponse.getMoney(), new BigDecimal(money));
    }

    private void createTransactions(String from, String to) {
        String transaction = gson.toJson(new Transaction(from, to, new BigDecimal("10")));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_TRANSACTION))
                .POST(HttpRequest.BodyPublishers.ofString(transaction))
                .build();

        HttpResponse<String> response = getStringHttpResponse(client, request);
        Result result = gson.fromJson(response.body(), Result.class);
        Assert.assertTrue(result.isResult());
    }
}