package ru.pahaya.entrypoints;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EntryPointTest {

    private static final Logger logger = LogManager.getLogger(EntryPointTest.class);
    private static final String HTTP_LOCALHOST_8080_ACCOUNT = "http://localhost:8080/account/";

    protected void createAccount(String account) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_LOCALHOST_8080_ACCOUNT))
                .POST(HttpRequest.BodyPublishers.ofString(account))
                .build();
        HttpResponse<String> response = getStringHttpResponse(client, request);
        Assert.assertEquals(response.body(), account);
    }

    protected HttpResponse<String> getStringHttpResponse(HttpClient client, HttpRequest request) {
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
