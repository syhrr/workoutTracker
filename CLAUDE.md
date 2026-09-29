# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Project overview

A RESTful backend API for a workout tracker. Users sign up, log in, build workout plans from a seeded library of exercises, schedule workouts, log progress, and generate reports on past workouts.

This is a portfolio/CV project based on the roadmap.sh "Workout Tracker" brief. Code quality, tests, and documentation matter as much as features: optimise for code a reviewer can read quickly and trust.

## Tech stack

- **Language:** Java 21
- **Framework:** Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security, Bean Validation)
- **Database:** PostgreSQL (relational database is a hard requirement)
- **Migrations:** Flyway
- **Auth:** JWT (stateless), passwords hashed with BCrypt
- **Docs:** OpenAPI 3 via springdoc-openapi (Swagger UI at `/swagger-ui.html`)
- **Testing:** JUnit 5, Mockito, Spring Boot Test, Testcontainers (real Postgres for integration tests)
- **Build:** Maven
- **Runtime:** Docker + docker-compose (app + Postgres)
- **CI:** GitHub Actions (build + test on every push and PR)

Do not introduce new frameworks or libraries without asking first.

## Commands

```bash
./mvnw spring-boot:run            # run the app locally (needs Postgres running)
docker compose up -d db           # start only Postgres
docker compose up --build         # run app + Postgres
./mvnw test                       # unit + integration tests
./mvnw verify                     # full build, tests, and checks
```

## Architecture

Layered, package-by-feature:

```
src/main/java/com/<name>/workouttracker/
  auth/        # sign-up, login, logout, JWT filter and service
  user/
  exercise/    # entity, repository, read-only endpoints, seeder
  workout/     # plans, workout exercises, scheduling, comments
  report/      # progress and history reports
  common/      # error handling, shared config, base classes
```

Within each feature: `Controller -> Service -> Repository`.

- Controllers handle HTTP only: validation, mapping DTOs, status codes. No business logic.
- Services hold business logic and enforce ownership rules.
- Never expose JPA entities in API responses. Use request/response DTOs (Java records).
- A single `@RestControllerAdvice` returns consistent error bodies (RFC 7807 `ProblemDetail`).

## Domain model

- **User**: id, email (unique), password hash, created_at
- **Exercise**: id, name (unique), description, category (`CARDIO`, `STRENGTH`, `FLEXIBILITY`), muscle group (`CHEST`, `BACK`, `LEGS`, `SHOULDERS`, `ARMS`, `CORE`, `FULL_BODY`)
- **Workout**: id, user_id, name, status (`PENDING`, `ACTIVE`, `COMPLETED`), scheduled_at, completed_at, comments, created_at, updated_at
- **WorkoutExercise**: id, workout_id, exercise_id, sets, reps, weight_kg, order_index

Relationships: a User has many Workouts; a Workout has many WorkoutExercises; each WorkoutExercise references one Exercise.

All schema changes go through Flyway migrations in `src/main/resources/db/migration`. Never rely on Hibernate auto-DDL (`ddl-auto` must be `validate`).

## Exercise seeder

Seed the exercise library on startup (or via a Flyway migration), covering every category and muscle group. The seeder must be idempotent: running it twice must not create duplicates.

## API endpoints

All endpoints are under `/api/v1`. Everything except sign-up and login requires a valid JWT.

| Method | Path | Purpose |
|---|---|---|
| POST | `/auth/signup` | Create an account |
| POST | `/auth/login` | Return a JWT |
| POST | `/auth/logout` | Log out (client discards token; optional server-side denylist) |
| GET | `/exercises` | List exercises, filterable by category and muscle group |
| GET | `/exercises/{id}` | Get one exercise |
| POST | `/workouts` | Create a workout with its exercises |
| GET | `/workouts` | List the user's active or pending workouts, sorted by `scheduled_at` ascending; filter by status |
| GET | `/workouts/{id}` | Get one workout |
| PUT | `/workouts/{id}` | Update a workout, including exercises and comments |
| PATCH | `/workouts/{id}/schedule` | Set or change the scheduled date and time |
| PATCH | `/workouts/{id}/complete` | Mark a workout completed |
| DELETE | `/workouts/{id}` | Delete a workout |
| GET | `/reports/progress` | Report on past workouts: counts, volume over time, per-exercise progress, within a date range |

List endpoints must support pagination (`page`, `size`).

## Security rules

- Users can only read, update, or delete **their own** workouts. Enforce this in the service layer, and return `404` (not `403`) for another user's workout so resource existence isn't leaked.
- Never log passwords, tokens, or full request bodies from auth endpoints.
- JWT secret and database credentials come from environment variables, never committed. Provide `.env.example`.
- Validate every request body (`@Valid`) with sensible bounds (e.g. sets and reps > 0, weight >= 0).

## Testing

- Every service method gets unit tests (Mockito for dependencies).
- Every endpoint gets at least one integration test using Testcontainers Postgres, covering the happy path, validation errors, unauthenticated access (`401`), and access to another user's data (`404`).
- Report calculations need tests with known fixtures and expected numbers.
- Run `./mvnw test` after changes and make sure everything passes before calling a task done.

## Documentation

- Annotate controllers so the generated OpenAPI spec includes descriptions, request/response examples, and error responses.
- Keep `README.md` up to date with: project summary, tech stack, how to run locally with Docker, how to authenticate in Swagger UI, example `curl` requests, and a short "design decisions" section.

## Conventions

- Java records for DTOs; constructor injection only (no field `@Autowired`).
- Use `Instant`/`OffsetDateTime` for timestamps and store them in UTC.
- Small, focused commits with clear messages (e.g. `feat(workout): add scheduling endpoint`).
- Prefer clarity over cleverness. Add comments only where the "why" isn't obvious.

## Definition of done

A feature is done when: the code compiles, all tests pass, new behaviour is covered by tests, the OpenAPI docs describe it, and the README reflects any new setup or usage.

## Stretch goals (only when asked)

- Import workout history from a Hevy CSV export (parse, validate, deduplicate)
- GitHub-style training heatmap endpoint (workouts per day over the past year)
- Estimated 1RM tracking and personal records per exercise
- Simple React + TypeScript frontend