package org.example.fraud.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.fraud.model.FraudResult;
import org.example.fraud.model.User;
import org.example.fraud.service.FraudCheckService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

//class that exposes the POST method
public class FraudCheckHandler implements HttpHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final FraudCheckService fraudCheckService;

    public FraudCheckHandler(FraudCheckService fraudCheckService) {
        this.fraudCheckService = fraudCheckService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Only POST is supported
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            // Read request body
            String requestBody = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            if (requestBody.isBlank()) {
                sendError(exchange, 400, "Request body is empty");
                return;
            }

            // Deserialize JSON into domain object
            User user = objectMapper.readValue(requestBody, User.class);

            // Basic validation
            if (user.getEmail() == null || user.getPhone() == null) {
                sendError(exchange, 400, "Missing required fields");
                return;
            }

            // Execute fraud check
            FraudResult result = fraudCheckService.check(user);

            // Serialize response
            String responseJson = objectMapper.writeValueAsString(result);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
            exchange.getResponseBody().write(responseJson.getBytes());

        } catch (JsonProcessingException e) {
            // Invalid JSON payload
            sendError(exchange, 400, "Invalid JSON format");

        } catch (Exception e) {
            // Unexpected error
            sendError(exchange, 500, "Internal server error");

        } finally {
            exchange.close();
        }
    }

    private void sendError(HttpExchange exchange, int status, String message)
            throws IOException {

        String json = String.format("{\"error\":\"%s\"}", message);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
    }
}