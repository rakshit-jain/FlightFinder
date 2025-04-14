package com.flightfinder.utils;

import com.flightfinder.model.Flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private FileUtils() {
        // Private constructor to prevent instantiation
    }

    public static List<Flight> readFlightsFromCSV(String CSV_FILE_PATH) throws IOException {
        List<Flight> flights = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                FileUtils.class.getClassLoader().getResourceAsStream(CSV_FILE_PATH)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    try {
                        String flightNo = parts[0].trim();
                        String fromAirport = parts[1].trim();
                        String toAirport = parts[2].trim();
                        LocalTime startTime = TimeUtils.parseTimeInHHmmFormat(parts[3].trim());
                        LocalTime endTime = TimeUtils.parseTimeInHHmmFormat(parts[4].trim());
                        flights.add(new Flight(flightNo, fromAirport, toAirport, startTime, endTime));
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                        // Handle the error appropriately: log, throw exception, or continue.
                    }
                } else {
                    System.err.println("Skipping invalid line: " + line); // Handle invalid lines
                }
            }
        }
        return flights;
    }
}
