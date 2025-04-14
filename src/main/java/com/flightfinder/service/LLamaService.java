package com.flightfinder.service;

import com.flightfinder.client.LlamaApiClient;
import com.flightfinder.model.FlightDetails;
import com.flightfinder.processor.LlamaQueryProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class LLamaService implements NlpService{
    private final LlamaApiClient llamaApiClient;
    private final FlightService flightService;
    private final LlamaQueryProcessor llamaQueryProcessor;
    private static final String NO_FLIGHTS_FOUND = "No flights found";

    public LLamaService(@Autowired LlamaApiClient llamaApiClient,
                        @Autowired FlightService flightService,
                        @Autowired LlamaQueryProcessor llamaQueryProcessor) {
        this.flightService = flightService;
        this.llamaApiClient = llamaApiClient;
        this.llamaQueryProcessor = llamaQueryProcessor;
    }

    public String findTop5FastestFlights(String userQuery) throws IOException, InterruptedException {
        FlightDetails flightDetails = llamaQueryProcessor.getFlightDetailsFromQuery(userQuery, llamaApiClient);

        List<Map<String, Map<String, Integer>>> fastestFlights =
                flightService.findFastestFlights(flightDetails.getFrom(), flightDetails.getTo());
        fastestFlights = fastestFlights.size() > 5 ? fastestFlights.subList(0, 5) : fastestFlights;
        return fastestFlights.isEmpty() ? NO_FLIGHTS_FOUND : fastestFlights.toString();
    }
}