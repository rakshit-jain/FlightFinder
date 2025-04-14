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
            @RequestParam(required = false, defaultValue = "5") int size){
        //Assuming authentication and authorization are handled by Spring Security
        if(validateAirportCode(from) && validateAirportCode(to)){
            List<Map<String, Map<String, Integer>>> fastestFlights = flightService.findFastestFlights(from, to);
            return ResponseEntity.ok(fastestFlights.size() > size ?fastestFlights.subList(0, size) : fastestFlights);
        }
        return ResponseEntity.badRequest().body(null);
    }

    public boolean validateAirportCode(String airportCode) {
        // Implement your validation logic here
        // For example, check if the airport code is in a predefined list of valid codes
        return true; // Placeholder for actual validation logic
    }
}
