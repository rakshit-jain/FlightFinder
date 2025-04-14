package com.flightfinder;

import com.flightfinder.service.LLamaService;
import com.flightfinder.service.NlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class FlightFinderApplication {

    private final NlpService nlpService;

    public FlightFinderApplication(@Autowired NlpService nlpService) {
        this.nlpService = nlpService;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FlightFinderApplication.class, args);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        FlightFinderApplication app = context.getBean(FlightFinderApplication.class);

        System.out.println("Welcome to Flight Finder!");
        while (true) {
            try {
                System.out.print("Enter your query (or type 'exit' to quit): ");
                String userInput = reader.readLine();

                if ("exit".equalsIgnoreCase(userInput)) {
                    System.out.println("Goodbye!");
                    break;
                }
                String response = app.nlpService.findTop5FastestFlights(userInput);
                System.out.println("Response: " + response);
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }
}