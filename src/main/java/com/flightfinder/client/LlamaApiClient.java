package com.flightfinder.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LlamaApiClient {

    private final HttpClient httpClient;
    private final String apiUrl;

    public LlamaApiClient(@Value("${ollama.api.url:http://localhost:11434/api/chat}") String apiUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.apiUrl = apiUrl;
    }

    public String getLlamaResponse(String userQuery) throws IOException, InterruptedException {
        String requestBody = createRequestBody(userQuery);
        HttpRequest request = buildHttpRequest(requestBody);
        HttpResponse<String> response = sendRequest(request);
        return parseResponse(response);
    }

    private String createRequestBody(String userQuery) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "llama3.2");
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", userQuery);
        requestBody.put("messages", new org.json.JSONArray().put(message));
        requestBody.put("stream", false);
        return requestBody.toString();
    }

    private HttpRequest buildHttpRequest(String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Error calling Llama API: " + response.body());
        }
        return response;
    }

    private String parseResponse(HttpResponse<String> response) {
        JSONObject responseBody = new JSONObject(response.body());
        JSONObject llamaResponse = responseBody.getJSONObject("message");
        return llamaResponse.getString("content");
    }
}