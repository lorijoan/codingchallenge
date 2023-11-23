package com.tfl.codingchallenge.controller;

import com.tfl.codingchallenge.model.Road;
import com.tfl.codingchallenge.service.RoadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RoadController {

    private final RoadService roadService;

    public RoadController(RoadService roadService) {
        this.roadService = roadService;
    }

    @GetMapping("/road/{id}")
    public String getRoadById(@PathVariable String id) {
        try {
            Road road = roadService.findRoadById(id);
            return String.format("response: The status of the %s is %s and %s.", road.getDisplayName(), road.getRoadStatus(), road.getRoadStatusDescription()).toString();
        } catch(Exception exception) {
            return String.format("response: %s is not a valid road.", id).toString();
        }
    }
}