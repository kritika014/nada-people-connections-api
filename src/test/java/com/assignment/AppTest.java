package com.assignment;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    private static Javalin app;
    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    public static void setup() {
        app = App.startServer(8080);
    }

    @AfterAll
    public static void teardown() {
        app.stop();
    }

    private void createPerson(String name) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/people"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"" + name + "\"}"))
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    private void createConnection(int a, int b) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/knows"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"a\": " + a + ", \"b\": " + b + "}"))
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    @Test
    public void testThreeHopLimit() throws Exception {
        createPerson("A"); // ID: 1
        createPerson("B"); // ID: 2
        createPerson("C"); // ID: 3
        createPerson("D"); // ID: 4
        createPerson("E"); // ID: 5

        createConnection(1, 2);
        createConnection(2, 3);
        createConnection(3, 4);
        createConnection(4, 5);

        // A to D (3 hops) -> connected
        HttpRequest reqAD = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/path?from=1&to=4"))
                .GET()
                .build();
        HttpResponse<String> resAD = client.send(reqAD, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resAD.statusCode());
        assertTrue(resAD.body().contains("\"connected\":true"));
        assertTrue(resAD.body().contains("\"hops\":3"));

        // A to E (4 hops limit exceeded) -> not connected
        HttpRequest reqAE = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/path?from=1&to=5"))
                .GET()
                .build();
        HttpResponse<String> resAE = client.send(reqAE, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resAE.statusCode());
        assertTrue(resAE.body().contains("\"connected\":false"));
        assertFalse(resAE.body().contains("\"hops\""));
    }
}
