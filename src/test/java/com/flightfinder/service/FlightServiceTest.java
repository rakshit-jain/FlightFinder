package com.flightfinder.service;

import com.flightfinder.model.Flight;
import com.flightfinder.model.OneStopFlight;
import com.flightfinder.utils.FileUtils;
import com.flightfinder.utils.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    private FlightService flightService;

    @BeforeEach
    void setUp() throws IOException {
        try (MockedStatic<FileUtils> fileUtilsMock = mockStatic(FileUtils.class)) {
            // Mock the FileUtils to return a predefined list of flights
            fileUtilsMock.when(() -> FileUtils.readFlightsFromCSV("ivtest-sched.csv"))
                    .thenReturn(getMockFlights());
            flightService = new FlightService();
        }
    }
    @Test
    void testFindFastestFlights() {
        List<Map<String, Map<String, Integer>>> result = flightService.findFastestFlights("BOM", "MAA");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void testFindDirectFlights() {
        List<Map<String, Map<String, Integer>>> result = flightService.findDirectFlights("BOM", "MAA");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size()); // Assuming 1 direct flight in the mock data
    }

    @Test
    void testFindOneStopFlights() {
        List<OneStopFlight> result = flightService.findOneStopFlights("BOM", "MAA");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size()); // Assuming 1 one-stop flight in the mock data
    }

    private List<Flight> getMockFlights() {
        Flight flight1 = new Flight("F1", "BOM", "MAA", LocalTime.NOON, LocalTime.of(14, 0));
        Flight flight2 = new Flight("F2", "BOM", "DEL", LocalTime.MIDNIGHT, LocalTime.of(2, 0));
        Flight flight3 = new Flight("F3", "DEL", "MAA", LocalTime.NOON, LocalTime.of(16, 0));

        return Arrays.asList(flight1, flight2, flight3);
    }
}