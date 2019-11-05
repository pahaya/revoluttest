package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ru.pahaya.Application;
import ru.pahaya.entity.Account;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountEntryPointTest {

    private static final Logger logger = LogManager.getLogger(AccountEntryPointTest.class);
    private static final Gson gson = new Gson();
    private final static String account = "{\"id\":\"2\",\"money\":1000.35}";
    private final static String account2 = "{\"money\":1000.35}";

    public class Response {
        private boolean result;

        public boolean isResult() {
            return result;
        }
    }

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
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/"))
                .POST(HttpRequest.BodyPublishers.ofString(account))
                .build();

        HttpResponse<String> response = getStringHttpResponse(client, request);
        Assert.assertEquals(response.body(), account);
    }

    @Test
    public void createAccount2() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/"))
                .POST(HttpRequest.BodyPublishers.ofString(account2))
                .build();

        HttpResponse<String> response = getStringHttpResponse(client, request);
        Account accountFromResponse = gson.fromJson(response.body(), Account.class);
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/" + accountFromResponse.getId()))
                .GET()
                .build();
        response = getStringHttpResponse(client, request);
        Account accountFromResponse2 = gson.fromJson(response.body(), Account.class);

        Assert.assertEquals(accountFromResponse, accountFromResponse2);

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/"))
                .method("DELETE", HttpRequest.BodyPublishers.ofString(gson.toJson(accountFromResponse, Account.class)))
                .build();

        response = getStringHttpResponse(client, request);
        Response result = gson.fromJson(response.body(), Response.class);
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

    private HttpResponse<String> getStringHttpResponse(HttpClient client, HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            logger.error("Problems with socket", e);
        } catch (InterruptedException e) {
            logger.error("Problems with thread", e);
        }
        return response;
    }
}