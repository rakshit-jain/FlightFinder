package com.flightfinder.model;

import com.flightfinder.utils.TimeUtils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Flight {
    String flightNo;
    String fromAirport;
    String toAirport;
    LocalTime startTime;
    LocalTime endTime;

    public Flight(String flightNo, String fromAirport, String toAirport, LocalTime startTime, LocalTime endTime) {
        this.flightNo = flightNo;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getDuration() {
        if (endTime.isAfter(startTime)) {
            return ChronoUnit.MINUTES.between(startTime, endTime);
        } else {
            // Handle flights that cross midnight
            return TimeUtils.getMinutesBetweenMidnight(startTime, endTime);
        }
    }
    public String getFlightNo() {
        return flightNo;
    }
    public String getFromAirport() {
        return fromAirport;
    }
    public String getToAirport() {
        return toAirport;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
}
