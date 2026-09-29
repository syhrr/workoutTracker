# Rhys' Workout Tracker

A RESTful backend API for a workout tracker. Users sign up, log in, build workout plans from a seeded library of exercises, schedule workouts, log progress, and generate reports on past workouts.

Based on the [roadmap.sh Workout Tracker](https://roadmap.sh/projects/fitness-workout-tracker) project brief.

## Tech stack

- Java 21, Spring Boot 3 (Web, Data JPA, Security, Validation)
- PostgreSQL, with schema migrations managed by Flyway
- JWT authentication (stateless), passwords hashed with BCrypt
- OpenAPI 3 docs via springdoc-openapi (Swagger UI at `/swagger-ui.html`)
- JUnit 5, Mockito, Spring Boot Test, Testcontainers
- Maven, Docker Compose, GitHub Actions

## Data model

A user has many workouts; a workout has many workout exercises; each workout exercise references one exercise from the seeded library.

```mermaid
erDiagram
    USERS ||--o{ WORKOUT : owns
    WORKOUT ||--o{ WORKOUT_EXERCISE : contains
    EXERCISE ||--o{ WORKOUT_EXERCISE : "used in"

    USERS {
        bigint id PK
        string email UK
        string password_hash
        timestamptz created_at
    }

    EXERCISE {
        bigint id PK
        string name UK
        string description
        string category "CARDIO | STRENGTH | FLEXIBILITY"
        string muscle_group "CHEST | BACK | LEGS | SHOULDERS | ARMS | CORE | FULL_BODY"
    }

    WORKOUT {
        bigint id PK
        bigint user_id FK
        string name
        string status "PENDING | ACTIVE | COMPLETED"
        timestamptz scheduled_at
        timestamptz completed_at
        string comments
        timestamptz created_at
        timestamptz updated_at
    }

    WORKOUT_EXERCISE {
        bigint id PK
        bigint workout_id FK
        bigint exercise_id FK
        int sets
        int reps
        decimal weight_kg
        int order_index
    }
```

## Running locally

Requires Java 21 and Docker.

```bash
cp .env.example .env              # database credentials
docker compose up -d db           # start Postgres
./mvnw spring-boot:run            # run the app on http://localhost:8080
```

## Running tests

Integration tests start a real Postgres with Testcontainers, so Docker must be running.

```bash
./mvnw test                       # unit + integration tests
./mvnw verify                     # full build, tests, and checks
```

## Authenticating in Swagger UI

TODO

## Example requests

TODO

## Design decisions

TODO
