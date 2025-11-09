# sqlstar-api

Spring Boot 3 API with a single POST endpoint at `/sqlstar` (and `/sqlstar/`) handled by method `ingestQuery`.

## Requirements
- Java 17+
- Maven 3.9+ (or compatible)

## Build and Run

- Run the app:
  - From this repository root (where `sqlstar-api` folder is located):
    ```
    mvn -f sqlstar-api/pom.xml spring-boot:run
    ```
  - The server starts on http://localhost:8080

- Run tests:
  ```
  mvn -f sqlstar-api/pom.xml test
  ```

## API

- Method: POST
- Path: `/sqlstar` and `/sqlstar/`
- Content-Type: `application/json`
- Request body:
  ```json
  {
    "query": "select * from dual",
    "metadata": { "source": "demo" }
  }
  ```
  - `query` is required and must be non-blank
  - `metadata` is optional

- Response (200):
  ```json
  {
    "status": "ok",
    "receivedAt": "2025-01-01T12:34:56.789Z",
    "queryPreview": "select * from dual"
  }
  ```

- Response (400):
  - When `query` is blank or missing (Bean Validation)

## Example requests

- Windows CMD (single line):
  ```
  curl -X POST http://localhost:8080/sqlstar -H "Content-Type: application/json" -d "{\"query\":\"select 1\",\"metadata\":{\"k\":\"v\"}}"
  ```

- PowerShell:
  ```
  curl -Method POST http://localhost:8080/sqlstar -ContentType "application/json" -Body '{ "query": "select 1", "metadata": { "k": "v" } }'
  ```

- Trailing slash works too:
  ```
  curl -X POST http://localhost:8080/sqlstar/ -H "Content-Type: application/json" -d "{\"query\":\"select 2\"}"
  ```

## Project layout

- `src/main/java/com/sqlstar/SqlStarApplication.java` – Spring Boot entrypoint
- `src/main/java/com/sqlstar/api/IngestController.java` – controller with `ingestQuery`
- `src/main/java/com/sqlstar/dto/IngestRequest.java` – request DTO
- `src/main/java/com/sqlstar/dto/IngestResponse.java` – response DTO
- `src/test/java/com/sqlstar/api/IngestControllerTest.java` – MockMvc tests
