## Task: Create User entity

**Timestamp:**

2026-03-28T13:27:00Z

**Why this task:**

First task in dependency order — the User entity is a prerequisite for UserRepository, UserService, and UserController.

**What was done:**

Created the JPA User entity with id (auto-generated), name (required, unique), and email (required, unique) fields. Added spring-boot-starter-data-jpa and H2 runtime dependencies to pom.xml. Wrote unit tests for the entity constructor and setters.

**What was changed:**

- pom.xml (added spring-boot-starter-data-jpa and h2 dependencies)
- src/main/java/com/programacho/ralphloopexample/user/User.java (new)
- src/test/java/com/programacho/ralphloopexample/user/UserTest.java (new)

**Remarks:**

Used `@Table(name = "users")` to avoid conflict with the reserved SQL keyword `USER`. Used `GenerationType.IDENTITY` for H2 compatibility. Protected no-arg constructor for JPA, public constructor for application use.

## Task: Create UserRepository

**Timestamp:**

2026-03-28T13:29:00Z

**Why this task:**

Dependency order — UserRepository is a prerequisite for UserService and UserController.

**What was done:**

Created the UserRepository interface extending JpaRepository<User, Long>. Wrote integration tests using @DataJpaTest covering save, findById, findAll, and deleteById operations. Added spring-boot-starter-data-jpa-test dependency for the @DataJpaTest annotation.

**What was changed:**

- pom.xml (added spring-boot-starter-data-jpa-test dependency)
- src/main/java/com/programacho/ralphloopexample/user/UserRepository.java (new)
- src/test/java/com/programacho/ralphloopexample/user/UserRepositoryTest.java (new)

**Remarks:**

In Spring Boot 4, `@DataJpaTest` moved from `org.springframework.boot.test.autoconfigure.orm.jpa` to `org.springframework.boot.data.jpa.test.autoconfigure`. The separate `spring-boot-starter-data-jpa-test` dependency is now required for this annotation.

## Task: Create UserService with CRUD methods

**Timestamp:**

2026-03-28T13:30:00Z

**Why this task:**

Dependency order — UserService depends on UserRepository (done) and is a prerequisite for UserController.

**What was done:**

Created UserService with create, findAll, findById, update, and delete methods. The update and delete methods throw NoSuchElementException when the user is not found (via Optional.orElseThrow()). Wrote 8 unit tests using Mockito to mock UserRepository, covering both happy paths and not-found cases.

**What was changed:**

- src/main/java/com/programacho/ralphloopexample/user/UserService.java (new)
- src/test/java/com/programacho/ralphloopexample/user/UserServiceTest.java (new)
- prd.md (marked task as done)

**Remarks:**

Used NoSuchElementException from Optional.orElseThrow() for now. The dedicated exception handling tasks later will replace this with proper HTTP error responses (404/409).

## Task: Create UserController with CRUD endpoints

**Timestamp:**

2026-03-28T13:33:00Z

**Why this task:**

Dependency order — UserController depends on UserService (done) and is the next task in the PRD sequence.

**What was done:**

Created UserController as a @RestController with all five CRUD endpoints: POST /users (201), GET /users (200), GET /users/{id} (200/404), PUT /users/{id} (200), DELETE /users/{id} (204). Added spring-boot-starter-webmvc and spring-boot-starter-webmvc-test dependencies to pom.xml. Wrote 6 unit tests using @WebMvcTest and MockMvc covering all endpoints including the 404 case for findById.

**What was changed:**

- pom.xml (added spring-boot-starter-webmvc and spring-boot-starter-webmvc-test dependencies)
- src/main/java/com/programacho/ralphloopexample/user/UserController.java (new)
- src/test/java/com/programacho/ralphloopexample/user/UserControllerTest.java (new)
- prd.md (marked task as done)

**Remarks:**

In Spring Boot 4, the web starter is named `spring-boot-starter-webmvc` (not `spring-boot-starter-web`). The `@WebMvcTest` annotation moved from `org.springframework.boot.test.autoconfigure.web.servlet` to `org.springframework.boot.webmvc.test.autoconfigure`. Jackson 3.x changed its package from `com.fasterxml.jackson` to `tools.jackson`. A separate `spring-boot-starter-webmvc-test` dependency is required for the @WebMvcTest annotation.
