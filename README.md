# URL Shortener

A REST API for shortening URLs, built with Spring Boot and PostgreSQL. Create short
codes for long URLs, redirect visitors, and track each click.

## Tech Stack

- **Java 17**
- **Spring Boot 4.x** (Web, JPA, Validation)
- **PostgreSQL** — primary database
- **Flyway** — database migrations
- **Hibernate** — ORM with schema validation
- **Lombok** — boilerplate reduction (`@Data`, `@Slf4j`, …)
- **Apache Commons Validator** — IP address validation
- **SLF4J / Logback** — logging (Spring Boot default)

## Prerequisites

- Java 17+
- PostgreSQL running locally
- Maven (or use the included `./mvnw` wrapper)

## Setup

1. Create the PostgreSQL database:
   ```sql
   CREATE DATABASE url_shortener;
   ```

2. Create a `.env` file in the project root:
   ```
   DB_URL=jdbc:postgresql://localhost:5432/url_shortener
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   ```

3. Run database migrations:
   ```bash
   make migrate
   ```

4. Start the server:
   ```bash
   make start-server
   ```

The server runs on `http://localhost:8080`.

## Available Commands

| Command | Description |
|---------|-------------|
| `make migrate` | Run pending Flyway migrations |
| `make start-server` | Build and start the application |
| `make check-style` | Run Checkstyle linting |

## API Reference

### Create a short URL

```
POST /api/urls/create
Content-Type: application/json

{ "url": "https://example.com/some/long/link" }
```

The `url` field is validated: it is required, must be at least 4 characters, and must
start with `http://` or `https://`. Returns `200 OK` with the created record, or
`400 Bad Request` with per-field error details if validation fails.

### List / search URLs

```
GET /api/urls/?search=git&page=0&size=10&sort=createdAt,desc
```

| Param | Default | Description |
|-------|---------|-------------|
| `search` | — | Case-insensitive match against the original URL **or** short code |
| `page` | `0` | Zero-based page number |
| `size` | `10` | Items per page |
| `sort` | `createdAt,desc` | `field,direction` |

Returns `200 OK` with a paginated list of URLs.

### Delete a URL

```
DELETE /api/urls/{id}/
```

Returns `204 No Content` on success, or `404 Not Found` if the id does not exist.

### Redirect (visit a short link)

```
GET /short/{shortCode}
```

Responds with `302 Found` and a `Location` header pointing to the original URL. Each
visit is tracked: `click_count` is incremented, `last_clicked_at` is updated, and a
`click_log` row is recorded with the visitor's IP address and user-agent. Returns
`404 Not Found` if the short code is unknown.

### List click logs

```
GET /api/click-logs/?search={urlId}&page=0&size=10
```

`search` accepts a URL id (UUID) to filter logs for a single URL. Returns `200 OK`
with a paginated list of click logs (each including the original URL).

### Error format

Validation and not-found errors return a consistent JSON shape:

```json
{
  "timestamp": "2026-06-07T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/urls/create",
  "errors": { "url": "URL must start with http:// or https://" }
}
```

## Database Schema

### `url`
| Column | Type | Description |
|--------|------|-------------|
| `id` | UUID | Primary key |
| `original_url` | TEXT | The full original URL |
| `short_code` | VARCHAR(10) | Unique short identifier |
| `click_count` | INT | Number of times the link was visited |
| `last_clicked_at` | TIMESTAMP | Last visit timestamp |
| `created_at` | TIMESTAMP | Record creation time |
| `updated_at` | TIMESTAMP | Last update time |

### `click_log`
| Column | Type | Description |
|--------|------|-------------|
| `id` | UUID | Primary key |
| `url_id` | UUID | Foreign key to `url` |
| `ip_address` | TEXT | Visitor IP address |
| `user_agent` | TEXT | Visitor browser/device info |
| `created_at` | TIMESTAMP | Click timestamp |
| `updated_at` | TIMESTAMP | Last update time |

## Project Structure

```
Url-Shortener/
├── src/
│   ├── main/
│   │   ├── java/techpriest/Url_Shortener/
│   │   │   ├── controllers/        # REST + redirect endpoints
│   │   │   ├── dto/                # request/response shapes
│   │   │   ├── exceptions/         # NotFoundException + GlobalExceptionHandler
│   │   │   ├── models/             # JPA entities (Base, Url, ClickLog)
│   │   │   ├── repositories/       # Spring Data JPA repositories
│   │   │   ├── services/           # business logic
│   │   │   ├── util/               # ShortCodeGenerator
│   │   │   └── UrlShortenerApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V1__create_url_table.sql
│   │           └── V2__create_click_log_table.sql
│   └── test/
├── .env                  # local environment variables (not committed)
├── .gitignore
├── Makefile
└── pom.xml
```

## Adding Migrations

Create a new file in `src/main/resources/db/migration/` following the naming pattern:

```
V{number}__{description}.sql
```

Then run `make migrate` before restarting the server.
