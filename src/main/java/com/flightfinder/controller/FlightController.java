package com.flightfinder.controller;

import com.flightfinder.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights")
class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/fastest")
    public ResponseEntity<List<Map<String, Map<String, Integer>>>> findFastestFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false, defaultValue = "5") String size){
        List<Map<String, Map<String, Integer>>> fastestFlights = flightService.findFastestFlights(from, to);
        int sizeInt = Integer.parseInt(size);
        return ResponseEntity.ok(fastestFlights.size() > sizeInt ?fastestFlights.subList(0, sizeInt) : fastestFlights);
    }
}
