package com.flightfinder.service;

import com.flightfinder.model.Flight;
import com.flightfinder.model.OneStopFlight;
import com.flightfinder.utils.FileUtils;
import com.flightfinder.utils.TimeUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private static final String CSV_FILE_PATH = "ivtest-sched.csv";
    private static final int LAYOVER_MINUTES = 120;
    private final List<Flight> allFlights;

    public FlightService() throws IOException {
        this.allFlights = FileUtils.readFlightsFromCSV(CSV_FILE_PATH);
    }


    public List<Map<String, Map<String, Integer>>> findFastestFlights(String from, String to) {

        List<Map<String, Map<String, Integer>>> result = findDirectFlights(from, to);
        List<OneStopFlight> oneStopFlights = findOneStopFlights(from, to).stream()
                .sorted(Comparator.comparingLong(OneStopFlight::getTotalTravelTime))
                .collect(Collectors.toList());

        for (OneStopFlight oneStopFlight : oneStopFlights) {
            Map<String, Integer> flightDetails = new HashMap<>();
            flightDetails.put(oneStopFlight.getFirstFlight().getFlightNo() + "_"
                    + oneStopFlight.getSecondFlight().getFlightNo(), (int) oneStopFlight.getTotalTravelTime());
            result.add(Map.of(from + "_" + oneStopFlight.getIntermediateAirport() + "_" + to, flightDetails));
        }

        List<Map<String, Map<String, Integer>>> deduplicatedResult = result.stream()
                .distinct() // Remove duplicates
                .sorted((a, b) -> {
                    int durationA = getFlightDuration(a);
                    int durationB = getFlightDuration(b);
                    return Integer.compare(durationA, durationB);
                })
                .collect(Collectors.toList());

        return deduplicatedResult;
    }


    List<Map<String, Map<String, Integer>>> findDirectFlights(String from, String to) {
        List<Map<String, Map<String, Integer>>> result = new ArrayList<>();
        List<Flight> directFlights = allFlights.stream()
                .filter(flight -> flight.getFromAirport().equals(from) && flight.getToAirport().equals(to))
                .sorted(Comparator.comparingLong(Flight::getDuration)) // Sort by duration
                .collect(Collectors.toList());

        for (Flight flight : directFlights) {
            Map<String, Integer> flightDetails = new HashMap<>();
            flightDetails.put(flight.getFlightNo(), (int) flight.getDuration());
            result.add(Map.of(from + "_" + to, flightDetails));
        }
        return result;
    }

    List<OneStopFlight> findOneStopFlights(String from, String to) {
        // Track all valid flights for each intermediate airport
        Map<String, List<OneStopFlight>> flightsByIntermediate = new HashMap<>();

        for (Flight firstFlight : allFlights) {
            if (firstFlight.getFromAirport().equals(from)) {
                List<Flight> connectingFlights = allFlights.stream()
                        .filter(flight -> flight.getFromAirport().equals(firstFlight.getToAirport())
                                && flight.getToAirport().equals(to)).collect(Collectors.toList());

                for (Flight secondFlight : connectingFlights) {
                    // Check for minimum layover time
                    long travelTimeBetweenFlightsInMinutes
                            = TimeUtils.getTravelTimeBetweenFlightsInMinutes(firstFlight, secondFlight);
                    if (travelTimeBetweenFlightsInMinutes >= LAYOVER_MINUTES) {
                        OneStopFlight oneStopFlight
                                = new OneStopFlight(firstFlight, secondFlight, travelTimeBetweenFlightsInMinutes);
                        String intermediateAirport = firstFlight.getToAirport();

                        // Add this flight to the list for the intermediate airport
                        flightsByIntermediate
                                .computeIfAbsent(intermediateAirport, k -> new ArrayList<>())
                                .add(oneStopFlight);
                    }
                }
            }
        }

        // Select the shortest flight for each intermediate airport
        return flightsByIntermediate.values().stream()
                .map(flights -> flights.stream()
                        .min(Comparator.comparingLong(OneStopFlight::getTotalTravelTime))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private static int getFlightDuration(Map<String, Map<String, Integer>> flightMap) {
        // Safely get the duration from the nested map.
        if (flightMap != null && !flightMap.isEmpty()) {
            for (Map.Entry<String, Map<String, Integer>> entry : flightMap.entrySet()) {
                Map<String, Integer> innerMap = entry.getValue(); // Get the inner map
                if (innerMap != null && !innerMap.isEmpty()) {
                    return innerMap.values().iterator().next();
                }
            }
        }
        return Integer.MAX_VALUE; // Return a large value in case of error to avoid incorrect sorting.
    }

}

