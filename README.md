# FlightFinder
Find Fastest Flights from the given set

## System Requirements
- Java 17 or higher
- Gradle 7.0 or higher
- IntelliJ IDEA or any other Java IDE
- Ollama(To run llama Model)

## How to run
1. Clone the repository
2. Navigate to the project directory
3. Run the following command to build the project:
   ```bash
   ./gradlew build
   ```
4. Run the following command to run the project:
   ```bash
     ./gradlew run
    ```
## How to test API
1. Hit the endpoint after starting the project 
 ```http://localhost:9090/flights/fastest?from=IXC&to=COK```

## Callouts
1. I have observed the time format in ivtest-sched.csv is not given as per HHMM format. As per the example given in problem statement the corrupted data is also considered in result hence data in CSV file is cleaned to match HHMM format

