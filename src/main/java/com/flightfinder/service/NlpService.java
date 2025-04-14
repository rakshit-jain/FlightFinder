package com.flightfinder.service;

import java.io.IOException;

public interface NlpService {
    String findTop5FastestFlights(String userQuery) throws IOException, InterruptedException;
}