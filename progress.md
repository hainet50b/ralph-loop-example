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
