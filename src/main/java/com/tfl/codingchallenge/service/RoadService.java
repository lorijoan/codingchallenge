package com.tfl.codingchallenge.service;

import com.tfl.codingchallenge.RoadNotFoundException;
import com.tfl.codingchallenge.model.Road;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
public class RoadService {

    private String apiKey;

    private final WebClient webClient;

    public RoadService(WebClient.Builder webClientBuilder, @Value("${tfl.api.key}") String apiKey, @Value("${tfl.api.baseUrl}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }
    public Road findRoadById(String id) {

        String uriPath = String.format("/road/%s", id);

        Mono<Road[]> result = webClient.get().uri(uriBuilder ->
                uriBuilder.path(uriPath)
                  .queryParam("app_key", apiKey)
                  .build()).retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new RoadNotFoundException("Road not found"))).bodyToMono(Road[].class);

        Road[] roads = result.block();

        return Arrays.stream(roads).findFirst().get();
    }
}