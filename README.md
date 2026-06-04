# URL Shortener

A REST API for shortening URLs, built with Spring Boot and PostgreSQL.

## Tech Stack

- **Java 17**
- **Spring Boot 4.x** (Web, JPA)
- **PostgreSQL** — primary database
- **Flyway** — database migrations
- **Hibernate** — ORM with schema validation

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
│   │   ├── java/com/techpriest/Url_Shortener/
│   │   │   ├── controllers/
│   │   │   ├── models/
│   │   │   │   ├── Base.java
│   │   │   │   ├── ClickLog.java
│   │   │   │   └── Url.java
│   │   │   ├── repositories/
│   │   │   ├── services/
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
