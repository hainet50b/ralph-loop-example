# PRD: User CRUD API

## Goal

Build a RESTful User CRUD API.

## Tech Stack

- Spring Boot 4
- Spring Web
- Spring Data JPA
- H2 (in-memory)

## API Endpoints

| Method | Path          | Description    | Success | Error    |
|--------|---------------|----------------|---------|----------|
| POST   | /users        | Create user    | 201     | 409      |
| GET    | /users        | List all users | 200     |          |
| GET    | /users/{id}   | Get user       | 200     | 404      |
| PUT    | /users/{id}   | Update user    | 200     | 404, 409 |
| DELETE | /users/{id}   | Delete user    | 204     | 404      |

## Data Model

### User

| Field | Type   | Constraints        |
|-------|--------|--------------------|
| id    | Long   | Auto-generated, PK |
| name  | String | Required, Unique   |
| email | String | Required, Unique   |

## Tasks

- [x] Create User entity
- [x] Create UserRepository
- [x] Create UserService with CRUD methods
- [ ] Create UserController with CRUD endpoints
- [ ] Add exception handling for 404 when the user is not found
- [ ] Add exception handling for 409 when the name or email is already taken
- [ ] Add Spring Actuator and expose only the health endpoint
- [ ] Configure H2 Console in application.yaml

## Post Tasks
- [ ] Create users.http with HTTP requests for all API endpoints, including normal and error cases

## Completion Criteria

All tasks and post tasks are checked off and `./mvnw test` passes with no failures.

