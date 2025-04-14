package com.flightfinder.utils;

import com.flightfinder.model.Flight;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");


    private TimeUtils() {
        // Private constructor to prevent instantiation
    }

    public static LocalTime parseTimeInHHmmFormat(String timeString) {
        if(StringUtils.hasLength(timeString))
        {
            int length = timeString.length();
            switch (length) {
                case 4 -> timeString = timeString;
                case 3 -> timeString = "0" + timeString;
                case 2 -> timeString = "00" + timeString;
                case 1 -> timeString = "000" + timeString;
                default -> throw new IllegalArgumentException("Invalid time format: " + timeString);
            }
            return LocalTime.parse(timeString, TIME_FORMATTER);
        }
        else
        {
            throw new IllegalArgumentException("Invalid time format: " + timeString);
        }
    }

    public static long getTravelTimeBetweenFlightsInMinutes(Flight firstFlight, Flight secondFlight) {
        if(secondFlight.getStartTime().isAfter(firstFlight.getEndTime()))
        {
            return ChronoUnit.MINUTES.between(firstFlight.getEndTime(), secondFlight.getStartTime());
        }
        else
        {
            return getMinutesBetweenMidnight(firstFlight.getEndTime(), secondFlight.getStartTime());
        }
    }

    public static long getMinutesBetweenMidnight(LocalTime startTime, LocalTime endTime) {
        LocalTime midnight = LocalTime.MIDNIGHT;
        long minutesToMidnight = ChronoUnit.MINUTES.between(startTime, midnight);
        long minutesFromMidnight = ChronoUnit.MINUTES.between(midnight, endTime);
        return minutesToMidnight + minutesFromMidnight + 24 * 60;
    }
}