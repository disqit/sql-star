# sqlstar

SqlStar is a containerized service that:
- Returns censored text via the ingest endpoint `/sqlstar`
- Provides internal CRUD endpoints to manage the sensitive word list

## Requirements
- Java 17+
- Maven 3.9+
- Docker Desktop (required for tests via Testcontainers; optional for running a local SQL Server instance)

## Database
The application uses Microsoft SQL Server by default (no profiles required). Connection details are provided via environment variables:

- `MSSQL_HOST` (default `localhost`)
- `MSSQL_PORT` (default `1433`)
- `MSSQL_DB` (default `sqlstar`)
- `MSSQL_USER` (default `sa`)
- `MSSQL_PASSWORD` (default `YourStrong!Passw0rd`)

Tests use Testcontainers to start a disposable SQL Server automatically. Ensure Docker Desktop (or a compatible Docker daemon) is running before executing tests.

## Quick start (MSSQL via Docker)
From repository root (where `docker-compose.yml` is located):

1) Start SQL Server (pulls the image on first run):
```
docker compose up -d
```

2) Run the API (default config points to MSSQL):
- Windows CMD:
```
cmd /c "cd sqlstar-api && .\mvnw.cmd spring-boot:run
```
- PowerShell:
```
cd sqlstar-api; ./mvnw.cmd spring-boot:run
```

3) Swagger:
- Swagger UI: http://localhost:8080/swagger-ui.html

4) Seeding
- On first run against an empty database, `DataSeeder` loads words from `src/main/resources/sql_sensitive_list.txt`.
- You can force reseeding via internal endpoint:
  ```
  POST http://localhost:8080/internal/words/seed?onlyIfEmpty=false
  ```

5) Open UI in browser: http://localhost:8080/

- Enter SQL-like text and click Submit to POST to `/sqlstar`.
- The result panel shows `status`, `receivedAt`, `amendedText`, and `replacements`.

## Endpoints

### Internal CRUD endpoints (manage sensitive words)
Base path: `/internal/words`

- GET `/internal/words` – list all words
- GET `/internal/words/{id}` – get one (Future: filter by word)
- POST `/internal/words` – create
  - Body: `{ "word": "DROP" }`
  - Returns 201, 409 on duplicate (case-insensitive)
- PUT `/internal/words/{id}` – update
  - Body: `{ "word": "EXEC" }`
  - Returns 200, 404/409 on not found/duplicate
- DELETE `/internal/words/{id}` – delete
  - Returns 204
- POST `/internal/words/seed?onlyIfEmpty=true|false` – seed from builtin list
  - Returns: `{ "inserted": N, "skipped": M }`

### Ingest/health preview (internal)
- POST `/sqlstar` (and `/sqlstar/`)
  - Request:
    ```json
    { "query": "select * from dual", "metadata": { "source": "demo" } }
    ```
  - Response (200):
    ```json
    { "status": "ok", "receivedAt": "2025-01-01T12:34:56.789Z", "queryPreview": "select * from dual", "amendedText": "****** * **** dual", "replacements": 3 }
    ```

## Build and test
From repository root:
- Run tests (uses Testcontainers MSSQL; requires Docker running):
```
mvn -f sqlstar-api/pom.xml test
```

## cURL examples

- Ingest (returns censored preview):
  ```
  curl -X POST http://localhost:8080/sqlstar -H "Content-Type: application/json" -d "{\"query\":\"select * from dual\"}"
  ```

- Internal: create a word
  ```
  curl -X POST http://localhost:8080/internal/words -H "Content-Type: application/json" -d "{\"word\":\"DROP\"}"
  ```

- Internal: list words
  ```
  curl http://localhost:8080/internal/words
  ```

- Internal: seed (force reseed)
  ```
  curl -X POST "http://localhost:8080/internal/words/seed?onlyIfEmpty=false"
  ```

## Notes
- Default SQL Server password is for local development only. Override `MSSQL_PASSWORD` as needed.
- If you already have a native SQL Server:
  - Create database `sqlstar`
  - Set the environment variables listed above to point to your instance
  - Start the app as shown in Quick start (no profile required)
