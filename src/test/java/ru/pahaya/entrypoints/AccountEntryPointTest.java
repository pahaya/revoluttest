package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ru.pahaya.Application;
import ru.pahaya.entity.AccountVO;
import ru.pahaya.entity.Result;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/***
 * This test starts http server, creates several accounts, and removes them.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountEntryPointTest extends EntryPointTest {

    private static final Logger logger = LogManager.getLogger(AccountEntryPointTest.class);
    private static final Gson gson = new Gson();
    private final static String account = "{\"id\":\"2\",\"money\":1000.35}";
    private final static String account2 = "{\"money\":1000.35}";
    private final static String account3 = "{\"id\":\"3\",\"money\":1000.35}";
    private static final String HTTP_LOCALHOST_8080_ACCOUNT = "http://localhost:8080/account/";

    @Before
    public void startApp() {
        Application.startServer();
    }

    @After
    public void stopApp() {
        Application.stop();
    }


    @Test
    public void createAccount() {
        createAccount(account);
    }

    @Test
    public void createAccount2() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_ACCOUNT))
                .POST(HttpRequest.BodyPublishers.ofString(account2))
                .build();

        HttpResponse<String> response = getStringHttpResponse(client, request);
        AccountVO accountFromResponse = gson.fromJson(response.body(), AccountVO.class);
        request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_ACCOUNT + accountFromResponse.getId()))
                .GET()
                .build();
        response = getStringHttpResponse(client, request);
        AccountVO accountFromResponse2 = gson.fromJson(response.body(), AccountVO.class);

        Assert.assertEquals(accountFromResponse, accountFromResponse2);

        request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_ACCOUNT))
                .method("DELETE", HttpRequest.BodyPublishers.ofString(gson.toJson(accountFromResponse, AccountVO.class)))
                .build();

        response = getStringHttpResponse(client, request);
        Result result = gson.fromJson(response.body(), Result.class);
        Assert.assertEquals(result.isResult(), true);
    }

    @Test
    public void getAccount() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/2"))
                .GET()
                .build();
        HttpResponse<String> response = getStringHttpResponse(client, request);
        Assert.assertEquals(response.body(), account);
    }

    @Test
    public void createAccountSeveralTimes() {
        Assert.assertEquals(createAccount3().statusCode(), 200);
        Assert.assertEquals(createAccount3().statusCode(), 400);
    }

    private HttpResponse<String> createAccount3() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_ACCOUNT))
                .POST(HttpRequest.BodyPublishers.ofString(account3))
                .build();

        return getStringHttpResponse(client, request);
    }

}