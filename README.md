# User Fraud Check Service (Java)

## Overview

Lightweight **user fraud detection service** implemented in **pure
Java**, exposed via a **REST API**.

------------------------------------------------------------------------

## Key Points

-   **Language:** Java (JDK)
-   **Build:** Gradle
-   **Frameworks:** None (intentional)
-   **Libraries:** Jackson (JSON parsing at REST boundary)
-   **Architecture:** Rule-based fraud detection
-   **Persistence:** None

------------------------------------------------------------------------

## Architecture

    POST /fraud-check
            ↓
    FraudCheckHandler (REST adapter)
            ↓
    FraudCheckService
            ↓
    FraudRule(s)
            ↓
    FraudResult (fraud + reasons)

-   REST layer is a thin adapter
-   Core logic is framework-agnostic
-   Rules are independent and extensible

------------------------------------------------------------------------

## REST Endpoint

**POST /fraud-check**

Evaluates a user against a set of fraud detection rules and returns an
explainable fraud result.

### Request

``` json
{
  "id": "1",
  "email": "test@mailinator.com",
  "phone": "+521234567890"
}
```

### Response

``` json
{
  "fraud": true,
  "reasons": ["Blacklisted email domain"]
}
```

### Status Codes

-   `200 OK` -- Request processed successfully
-   `400 Bad Request` -- Invalid JSON or missing required fields
-   `405 Method Not Allowed` -- Only POST is supported
-   `500 Internal Server Error` -- Unexpected server error

------------------------------------------------------------------------

## JSON Parsing

Incoming JSON requests are deserialized using **Jackson ObjectMapper**
at the REST boundary.

Reasoning: - Manual parsing proved brittle and error-prone - Jackson
ensures correct mapping regardless of formatting - Keeps the core fraud
logic unchanged and framework-agnostic

------------------------------------------------------------------------

## Error Handling

Errors are handled explicitly in the REST layer and returned as JSON
responses.

### Example

``` json
{
  "error": "Invalid JSON format"
}
```

This ensures clear feedback to API consumers while keeping domain logic
clean.

------------------------------------------------------------------------

## Run

### Build

``` bash
./gradlew build
```

### Start server

``` bash
./gradlew run
```

Server starts on:

    http://localhost:8080

------------------------------------------------------------------------

## Testing

-   Unit tests for fraud rules
-   Aggregation tests for FraudCheckService
-   No persistence or network calls in tests
 
Run tests:

``` bash
./gradlew test
```
------------------------------------------------------------------------

## Summary

This service provides an extensible fraud detection core exposed
via a minimal REST API. 
