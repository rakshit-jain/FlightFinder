package com.flightfinder.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightfinder.client.LlamaApiClient;
import com.flightfinder.model.FlightDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LlamaQueryProcessor {
    private static final String RESPONSE_FORMAT = "{\"From\":\"<>\",\"To\":\"<>\"}";
    private static final String QUERY_TEMPLATE = "Extract airport code and provide response in JSON format after replacing extracted Airport code at placeholder <> \n"
            + RESPONSE_FORMAT + "\n Ensure response is given in provided JSON format only without any additional text:\n";

    public FlightDetails getFlightDetailsFromQuery(String userQuery, LlamaApiClient llamaApiClient) throws IOException, InterruptedException {
        String fullQuery = QUERY_TEMPLATE + userQuery;
        String llamaResponse = llamaApiClient.getLlamaResponse(fullQuery);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(llamaResponse, FlightDetails.class);
    }
}
