package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.pahaya.Application;
import ru.pahaya.entity.Account;
import ru.pahaya.entity.AccountVO;
import ru.pahaya.entity.Result;
import ru.pahaya.entity.Transaction;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TransactionEntryPointTest extends EntryPointTest {

    private static final Logger logger = LogManager.getLogger(TransactionEntryPointTest.class);
    private final static String MAIN = "MAIN";
    private final static String SECOND = "SECOND";
    private final static String account = "{\"id\":\"" + MAIN + "\",\"money\":1000}";
    private final static String account2 = "{\"id\":\"" + SECOND + "\",\"money\":0}";
    private static final Gson gson = new Gson();
    private static final int COUNT_OF_MONEY = 100;

    @Before
    public void startApp() {
        Application.startServer();
    }

    @After
    public void stopApp() {
        Application.stop();
    }

    @Test
    public void get() {
    }

    @Test
    public void process() {
    }

    @Test
    public void refund() {
    }

    @Test
    public void getTransactions() {
        createAccount(account);
        createAccount(account2);
        createTransactions();
        checkAccount(MAIN, 0);
        checkAccount(SECOND, 1000);
    }

    private void checkAccount(String account, int money) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/" + account))
                .GET()
                .build();
        HttpResponse<String> response = getStringHttpResponse(client, request);
        AccountVO accountFromResponse = gson.fromJson(response.body(), AccountVO.class);
        Assert.assertEquals(accountFromResponse.getMoney(), new BigDecimal(money));
    }

    private void createTransactions() {
        for (int i = 0; i < 100; i++) {
            String transaction = gson.toJson(new Transaction(MAIN, SECOND, new BigDecimal("10")));
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/transaction/"))
                    .POST(HttpRequest.BodyPublishers.ofString(transaction))
                    .build();

            HttpResponse<String> response = getStringHttpResponse(client, request);
            Result result = gson.fromJson(response.body(), Result.class);
            Assert.assertTrue(result.isResult());
        }

    }
}