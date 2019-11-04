package ru.pahaya.entrypoints;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ru.pahaya.Application;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountEntryPointTest {

    private static final Logger logger = LogManager.getLogger(AccountEntryPointTest.class);
    private final static String account = "{\"id\":\"2\",\"money\":1000.35}";

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

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            logger.error("Problems with socket", e);
        } catch (InterruptedException e) {
            logger.error("Problems with thread", e);
        }
        Assert.assertEquals(response.body(), account);
    }

    @Test
    public void getAccount() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/2"))
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            logger.error("Problems with socket", e);
        } catch (InterruptedException e) {
            logger.error("Problems with thread", e);
        }
        Assert.assertEquals(response.body(), account);
    }


}