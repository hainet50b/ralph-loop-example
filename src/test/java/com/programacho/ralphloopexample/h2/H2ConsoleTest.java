package com.programacho.ralphloopexample.h2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.h2console.autoconfigure.H2ConsoleProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class H2ConsoleTest {

    @LocalServerPort
    private int port;

    @Autowired
    private H2ConsoleProperties h2ConsoleProperties;

    @Test
    void h2ConsoleIsEnabled() {
        assertThat(h2ConsoleProperties.isEnabled()).isTrue();
    }

    @Test
    void h2ConsoleIsAccessible() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/h2-console"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isIn(200, 302);
    }
}
