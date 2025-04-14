package com.flightfinder.service;

import com.flightfinder.client.LlamaApiClient;
import com.flightfinder.model.FlightDetails;
import com.flightfinder.processor.LlamaQueryProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LLamaServiceTest {

    private LLamaService llamaService;

    @Mock
    private LlamaApiClient llamaApiClient;

    @Mock
    private FlightService flightService;

    @Mock
    private LlamaQueryProcessor llamaQueryProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        llamaService = new LLamaService(llamaApiClient, flightService, llamaQueryProcessor);
    }

    @Test
    void testFindTop5FastestFlights_WithFlights() throws IOException, InterruptedException {
        // Mock input and dependencies
        String userQuery = "Find flights from BOM to MAA";
        FlightDetails flightDetails = new FlightDetails("BOM", "MAA");
        List<Map<String, Map<String, Integer>>> mockFlights = Arrays.asList(
                Map.of("Flight1", Map.of("Duration", 120)),
                Map.of("Flight2", Map.of("Duration", 150))
        );

        when(llamaQueryProcessor.getFlightDetailsFromQuery(userQuery, llamaApiClient)).thenReturn(flightDetails);
        when(flightService.findFastestFlights("BOM", "MAA")).thenReturn(mockFlights);

        // Call the method
        String result = llamaService.findTop5FastestFlights(userQuery);

        // Verify
        assertNotNull(result);
        assertTrue(result.contains("Flight1"));
        assertTrue(result.contains("Flight2"));
        verify(llamaQueryProcessor).getFlightDetailsFromQuery(userQuery, llamaApiClient);
        verify(flightService).findFastestFlights("BOM", "MAA");
    }

    @Test
    void testFindTop5FastestFlights_NoFlights() throws IOException, InterruptedException {
        // Mock input and dependencies
        String userQuery = "Find flights from BOM to MAA";
        FlightDetails flightDetails = new FlightDetails("BOM", "MAA");

        when(llamaQueryProcessor.getFlightDetailsFromQuery(userQuery, llamaApiClient)).thenReturn(flightDetails);
        when(flightService.findFastestFlights("BOM", "MAA")).thenReturn(Collections.emptyList());

        // Call the method
        String result = llamaService.findTop5FastestFlights(userQuery);

        // Verify
        assertEquals("No flights found", result);
        verify(llamaQueryProcessor).getFlightDetailsFromQuery(userQuery, llamaApiClient);
        verify(flightService).findFastestFlights("BOM", "MAA");
    }

    @Test
    void testFindTop5FastestFlights_Exception() throws IOException, InterruptedException {
        // Mock input and dependencies
        String userQuery = "Find flights from BOM to MAA";

        when(llamaQueryProcessor.getFlightDetailsFromQuery(userQuery, llamaApiClient))
                .thenThrow(new IOException("API error"));

        // Call the method and verify exception
        assertThrows(IOException.class, () -> llamaService.findTop5FastestFlights(userQuery));
        verify(llamaQueryProcessor).getFlightDetailsFromQuery(userQuery, llamaApiClient);
        verifyNoInteractions(flightService);
    }
}