package com.tfl.codingchallenge.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.tfl.codingchallenge.model.Road;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest
public class RoadServiceITest {

    static WireMockServer wireMockServer = new WireMockServer();

    @BeforeAll
    public static void beforeAll() {
        wireMockServer.start();
    }

    @AfterAll
    public static void afterAll() {

        wireMockServer.stop();
    }

    @AfterEach
    public void afterEach() {

        wireMockServer.resetAll();
    }

    public RoadService roadService = new RoadService(WebClient.builder(), "30021019a5c0499ebdc03f707c720801", wireMockServer.baseUrl());


    @ParameterizedTest
    @MethodSource("variousRoadsToTest")
    public void shouldFindRoadByNameAndReturn200CodeIfRoadExists(Road expectedRoad) {
        mockValidCallToTfl(expectedRoad);
        Road actualRoad = roadService.findRoadById(expectedRoad.getId());
        assertTrue(expectedRoad.equals(actualRoad));
    }


    @ParameterizedTest
    @ValueSource(strings = {"a101", "z23", ""})
    public void shouldThrowExceptionIfRoadDoesNotExist(String id) {
        try {
            mockInvalidCallToTfl(id);
            Road road = roadService.findRoadById(id);
        } catch(Exception exception) {
            assertTrue(exception.getMessage().equals("com.tfl.codingchallenge.RoadNotFoundException: Road not found"));
        }
    }

    private static Stream<Arguments> variousRoadsToTest() {
        return Stream.of(
                Arguments.of(new Road("a12", "A12", "Good", "No Exceptional Delays")),
                Arguments.of(new Road("a13", "A13", "Good", "No Exceptional Delays")),
                Arguments.of(new Road("a23", "A23", "Good", "No Exceptional Delays")),
                Arguments.of(new Road("a2", "A2", "Good", "No Exceptional Delays")),
                Arguments.of(new Road("a1", "A1", "Good", "No Exceptional Delays"))
                );
    }

    public void mockValidCallToTfl(Road road) {
        wireMockServer.stubFor(get(urlEqualTo(String.format("/road/%s?app_key=%s",road.getId(),"30021019a5c0499ebdc03f707c720801").toString()))
                .withQueryParam("app_key", equalTo("30021019a5c0499ebdc03f707c720801"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(getValidResponseBody(road))));
    }

    public void mockInvalidCallToTfl(String id) {
        wireMockServer.stubFor(get(urlEqualTo(String.format("/road/%s?app_key=%s",id,"30021019a5c0499ebdc03f707c720801").toString()))
                .withQueryParam("app_key", equalTo("30021019a5c0499ebdc03f707c720801"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(getInvalidResponseBody(id))));
    }

    public String getValidResponseBody(Road road) {
        return String.format("[{\"$type\":\"Tfl.Api.Presentation.Entities.RoadCorridor, Tfl.Api.Presentation.Entities\"," +
                "\"id\":\"%s\"," +
                "\"displayName\":\"%s\"," +
                "\"statusSeverity\":\"%s\"," +
                "\"statusSeverityDescription\":\"%s\"" +
                ",\"bounds\":\"[[-0.07183,51.51187],[0.28532,51.60844]]\",\"envelope\":\"[[-0.07183,51.51187],[-0.07183,51.60844],[0.28532,51.60844],[0.28532,51.51187],[-0.07183,51.51187]]\"," +
                "\"url\":\"/Road/%s\"}]", road.getId(), road.getDisplayName(), road.getRoadStatus(), road.getRoadStatusDescription(), road.getId());
    }

    public String getInvalidResponseBody(String id) {
        return String.format("[{\"$type\":\"Tfl.Api.Presentation.Entities.ApiError, Tfl.Api.Presentation.Entities\"," +
                "\"timestampUtc\":\"2023-11-21T10:50:36.3173891Z\"," +
                "\"exceptionType\":\"EntityNotFoundException\"," +
                "\"httpStatusCode\":404," +
                "\"httpStatus\":\"NotFound\"," +
                "\"relativeUri\":\"/Road/%s\"," +
                "\"message\":\"The following road id is not recognised: %s\"}]", id, id);
    }

}
