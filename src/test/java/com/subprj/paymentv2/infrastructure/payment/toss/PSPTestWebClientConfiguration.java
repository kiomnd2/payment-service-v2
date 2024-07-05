package com.subprj.paymentv2.infrastructure.payment.toss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Base64;

@TestConfiguration
public class PSPTestWebClientConfiguration {
    @Value("${psp.toss.url}")
    private String baseURL;

    @Value("${psp.toss.secretKey}")
    private String secretKey;

    public WebClient createTestTossWebClient(Pair<String, String> ... customerHeaderKey) {
        String encodedKey = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        return WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic "+ encodedKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeaders(httpHeaders -> {
                    for (Pair<String, String> headerKey : customerHeaderKey) {
                        httpHeaders.add(headerKey.getFirst(), headerKey.getSecond());
                    }
                })
                .clientConnector(reactorClientHttpConnector())
                .build();

    }

    private ClientHttpConnector reactorClientHttpConnector() {
        return new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.builder("test-toss-payment")
                .build()));
    }
}
