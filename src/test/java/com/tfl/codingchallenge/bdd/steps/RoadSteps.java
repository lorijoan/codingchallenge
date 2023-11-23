package com.tfl.codingchallenge.bdd.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

import com.tfl.codingchallenge.model.Road;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.List;


import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoadSteps {
    private Road roadToTest;

    private String result;

    private static WireMockServer wireMockServer = new
            WireMockServer();

    @Given("a road with the expected data")
    public void a_road_with_the_expected_data(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists(String.class);
        roadToTest = new Road(data.get(0).get(1), data.get(1).get(1), data.get(2).get(1), data.get(3).get(1));
    }

    @Given("an invalid road with the id {string}")
    public void a_road_with_the_id(String id) {
        roadToTest = null;
    }

    @When("I call the api with it")
    public void i_call_the_api_with_it() throws IOException, ParseException {
        wireMockServer.start();
        mockCallToTfl(roadToTest);
        String uriPath = String.format("/road/%s", roadToTest.getId());
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("http://localhost:8080" + uriPath);
        request.addHeader("accept", "application/json");
        CloseableHttpResponse httpResponse = httpClient.execute(request);
        HttpEntity responseEntity = httpResponse.getEntity();
        result = EntityUtils.toString(responseEntity).toString();
        wireMockServer.stop();
    }

    @Then("the call is successful with result {string}")
    public void the_call_is_successful_with_result(String string) {
        assertEquals(string, result);
    }

    @Then("the call is not successful with result {string}")
    public void the_call_is_not_successful_with_result(String string) {
        assertEquals(string, result);
    }

    public void mockCallToTfl(Road road) {
        wireMockServer.stubFor(get(urlEqualTo(String.format("/road/%s", road.getId()).toString()))
                .willReturn(aResponse()
                        .withBody(String.format("response: The status of the %s is %s and %s.", road.getDisplayName(), road.getRoadStatus(), road.getRoadStatusDescription()))));
    }
}