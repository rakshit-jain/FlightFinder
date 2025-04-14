package com.flightfinder.model;

public class OneStopFlight {
    Flight firstFlight;
    Flight secondFlight;
    String intermediateAirport;
    long travelTimeBetweenFlights;
    public OneStopFlight(Flight firstFlight, Flight secondFlight, long travelTimeBetweenFlights) {
        this.firstFlight = firstFlight;
        this.secondFlight = secondFlight;
        this.intermediateAirport = firstFlight.getToAirport();
        this.travelTimeBetweenFlights = travelTimeBetweenFlights;
    }

    public long getTotalTravelTime() {
        return travelTimeBetweenFlights + firstFlight.getDuration() + secondFlight.getDuration();
    }
    public Flight getFirstFlight() {
        return firstFlight;
    }
    public Flight getSecondFlight() {
        return secondFlight;
    }
    public String getIntermediateAirport() {
        return intermediateAirport;
    }
}
