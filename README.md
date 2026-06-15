# URL Shortener

A REST API for shortening URLs, built with Spring Boot and PostgreSQL. Create short
codes for long URLs, redirect visitors, track each click, and protect management
endpoints with JWT authentication.

## Tech Stack

- **Java 17**
- **Spring Boot 4.x** (Web, JPA, Validation)
- **PostgreSQL** — primary database
- **Flyway** — database migrations
- **Hibernate** — ORM with schema validation
- **Spring Security Crypto** — BCrypt password hashing
- **JJWT (io.jsonwebtoken)** — JWT access & refresh tokens
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
   JWT_SECRET=a-long-random-secret-at-least-32-bytes-long
   ```
   > `JWT_SECRET` is required and **must be at least 32 bytes** (it signs the JWTs).

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

## Authentication

The API uses **JWT** with two tokens:

- **Access token** — short-lived (24h), sent on every protected request.
- **Refresh token** — long-lived (7 days), used only to obtain a new token pair.

Both tokens are issued as a pair (sharing a `jti`) and carry the user's id. To call a
**protected** endpoint, send the access token in the `Authorization` header:

```
Authorization: Bearer <accessToken>
```

A missing/invalid/expired token (or using a refresh token where an access token is
required) returns `401 Unauthorized`. When the access token expires, call
`/api/auth/refresh` with the refresh token to get a new pair.

### Public vs. protected endpoints

| Endpoint | Auth required? |
|----------|----------------|
| `POST /api/onboarding/register` | Public |
| `POST /api/auth/login` | Public |
| `POST /api/auth/refresh` | Public |
| `GET /short/{shortCode}` | Public |
| `POST /api/urls/create` | **Protected** |
| `GET /api/urls/` | **Protected** |
| `DELETE /api/urls/{id}/` | **Protected** |
| `GET /api/click-logs/` | **Protected** |

## API Reference

### Register

```
POST /api/onboarding/register
Content-Type: application/json

{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "email": "ada@example.com",
  "password": "supersecret"
}
```

Validations: names letters-only, valid email, password ≥ 8 chars. Returns `201 Created`
with the user and a token pair. New users default to role `USER`, status `ACTIVE`.

### Login

```
POST /api/auth/login
Content-Type: application/json

{ "email": "ada@example.com", "password": "supersecret" }
```

Returns `200 OK` with the same shape as register. Invalid credentials return
`401 Unauthorized` (the same message whether the email or password is wrong).

**Auth response shape** (register & login):
```json
{
  "user": {
    "id": "…", "firstName": "Ada", "lastName": "Lovelace",
    "email": "ada@example.com", "role": "USER",
    "status": "ACTIVE", "emailVerified": false
  },
  "tokens": { "accessToken": "eyJ…", "refreshToken": "eyJ…" }
}
```

### Refresh tokens

```
POST /api/auth/refresh
Content-Type: application/json

{ "refreshToken": "eyJ…" }
```

Returns `200 OK` with a new `{ "accessToken": "…", "refreshToken": "…" }` pair.

### Create a short URL  _(protected)_

```
POST /api/urls/create
Content-Type: application/json
Authorization: Bearer <accessToken>

{ "url": "https://example.com/some/long/link" }
```

The `url` field is required, must be at least 4 characters, and must start with
`http://` or `https://`. The created URL is associated with the authenticated user.
Returns `200 OK` with the created record, or `400 Bad Request` with field errors.

### List / search URLs  _(protected)_

```
GET /api/urls/?search=git&page=0&size=10&sort=createdAt,desc
Authorization: Bearer <accessToken>
```

| Param | Default | Description |
|-------|---------|-------------|
| `search` | — | Case-insensitive match against the original URL **or** short code |
| `page` | `0` | Zero-based page number |
| `size` | `10` | Items per page |
| `sort` | `createdAt,desc` | `field,direction` |

Returns `200 OK` with a paginated list of URLs.

### Delete a URL  _(protected)_

```
DELETE /api/urls/{id}/
Authorization: Bearer <accessToken>
```

Returns `204 No Content` on success, or `404 Not Found` if the id does not exist.

### Redirect (visit a short link)  _(public)_

```
GET /short/{shortCode}
```

Responds with `302 Found` and a `Location` header pointing to the original URL. Each
visit is tracked: `click_count` is incremented, `last_clicked_at` is updated, and a
`click_log` row is recorded with the visitor's IP address and user-agent. Returns
`404 Not Found` if the short code is unknown.

### List click logs  _(protected)_

```
GET /api/click-logs/?search={urlId}&page=0&size=10
Authorization: Bearer <accessToken>
```

`search` accepts a URL id (UUID) to filter logs for a single URL. Returns `200 OK`
with a paginated list of click logs (each including the original URL).

### Error format

Validation, auth, and not-found errors return a consistent JSON shape:

```json
{
  "timestamp": "2026-06-15T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/urls/create",
  "errors": { "url": "URL must start with http:// or https://" }
}
```

## Database Schema

### `users`
| Column | Type | Description |
|--------|------|-------------|
| `id` | UUID | Primary key |
| `first_name` | TEXT | User's first name |
| `last_name` | TEXT | User's last name |
| `email` | TEXT | Unique login identifier |
| `password` | TEXT | BCrypt password hash |
| `email_verified` | BOOLEAN | Whether the email is verified |
| `status` | TEXT | `ACTIVE` / `INACTIVE` / `SUSPENDED` |
| `role` | TEXT | `USER` / `ADMIN` |
| `created_at` / `updated_at` | TIMESTAMP | Audit timestamps |

### `url`
| Column | Type | Description |
|--------|------|-------------|
| `id` | UUID | Primary key |
| `original_url` | TEXT | The full original URL |
| `short_code` | VARCHAR(10) | Unique short identifier |
| `click_count` | INT | Number of times the link was visited |
| `last_clicked_at` | TIMESTAMP | Last visit timestamp |
| `user_id` | UUID | Foreign key to `users` (creator; nullable) |
| `created_at` / `updated_at` | TIMESTAMP | Audit timestamps |

### `click_log`
| Column | Type | Description |
|--------|------|-------------|
| `id` | UUID | Primary key |
| `url_id` | UUID | Foreign key to `url` |
| `ip_address` | TEXT | Visitor IP address |
| `user_agent` | TEXT | Visitor browser/device info |
| `created_at` / `updated_at` | TIMESTAMP | Audit timestamps |

## Project Structure

```
Url-Shortener/
├── src/
│   ├── main/
│   │   ├── java/techpriest/Url_Shortener/
│   │   │   ├── config/             # SecurityConfig, WebConfig, JwtAuthInterceptor
│   │   │   ├── controllers/        # REST + redirect endpoints
│   │   │   ├── dto/                # request/response shapes
│   │   │   ├── exceptions/         # custom exceptions + GlobalExceptionHandler
│   │   │   ├── models/             # JPA entities (Base, Url, ClickLog, User) + enums
│   │   │   ├── repositories/       # Spring Data JPA repositories
│   │   │   ├── services/           # business logic (Url, ClickLog, User, Onboarding, Auth, Jwt)
│   │   │   ├── util/               # ShortCodeGenerator
│   │   │   └── UrlShortenerApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V1__create_url_table.sql
│   │           ├── V2__create_click_log_table.sql
│   │           ├── V3__create_user_table.sql
│   │           ├── V4__add_user_status.sql
│   │           └── V5__add_user_to_url.sql
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
