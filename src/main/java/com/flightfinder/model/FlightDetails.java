package com.flightfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightDetails {
    @JsonProperty("From")
    private String From;

    @JsonProperty("To")
    private String To;

    public FlightDetails() {
        // Default constructor
    }
    public FlightDetails(String From, String To) {
        this.From = From;
        this.To = To;
    }
    // Getters and setters
    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }
}
