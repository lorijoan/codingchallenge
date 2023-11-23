package com.tfl.codingchallenge.controller;

import com.tfl.codingchallenge.model.Road;
import com.tfl.codingchallenge.service.RoadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class RoadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoadService roadService;

    @Autowired
    private RoadController roadController;

    @Test
    public void shouldReturnRoadStatusDetailsAndRoadStatusSeverityIfRoadExists() throws Exception {
        Road exampleRoad = new Road("a23", "A23", "good", "whatever");
        when(roadService.findRoadById("a23")).thenReturn(exampleRoad);

        MvcResult result = mockMvc.perform(get("/api/v1/road/a23")).andReturn();

        String expectedContent = "response: The status of the A23 is good and whatever.";
        String actualContent = result.getResponse().getContentAsString();
        assertEquals(expectedContent, actualContent); // TODO check this comment
    }

    @Test
    public void shouldReturn404AndUsefulMessageIfRoadDoesNotExist() throws Exception {
        Road exampleRoad = null;
        when(roadService.findRoadById("a2")).thenReturn(exampleRoad);
        MvcResult result = mockMvc.perform(get("/api/v1/road/a2")).andReturn();

        String expectedContent = "response: a2 is not a valid road.";
        String actualContent = result.getResponse().getContentAsString();
        assertEquals(expectedContent, actualContent); // TODO check this comment
    }
}