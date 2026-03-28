# Ralph Loop Example

A hands-on example of the [Ralph Loop](https://ghuntley.com/ralph/) (also known as the "Ralph Wiggum Loop"), an autonomous AI coding workflow originally introduced by Geoffrey Huntley.

This repository demonstrates the Ralph Loop by building a simple User CRUD API with Spring Boot 4 from a minimal PRD (Product Requirements Document).

## How It Works

The Ralph Loop is a pattern where an AI agent reads a PRD, implements one task at a time, verifies it with tests, and repeats until all tasks are complete. When you run `./ralph.sh`, the following happens:

1. `ralph.sh` invokes `claude -p` with the contents of `prompt.md`
2. Claude reads `prd.md` and `progress.txt`, then selects the next task to work on
3. Claude implements the task and writes unit tests
4. Claude runs `./mvnw test` — if tests fail, it fixes the code before proceeding
5. Claude marks the task as checked in `prd.md` and logs what it did in `progress.txt`
6. Claude stages all changes and creates a git commit
7. If unchecked tasks remain, `ralph.sh` starts the next iteration from step 1 with a fresh context window
8. When all tasks are checked, Claude outputs `<promise>COMPLETE</promise>` and the loop exits

## Prerequisites

- Java 25+
- [Claude Code](https://claude.ai/download) CLI installed and authenticated
- `jq` and `bc` (used by `ralph.sh` for token usage tracking)

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/hainet50b/ralph-loop-example.git
cd ralph-loop-example
```

2. Create a branch for the Ralph Loop run (do **not** run on `main`):

```bash
git switch -c example/ralph-loop-result
```

3. (IMPORTANT) Review `prompt.md` and `prd.md` before running. The loop uses `--dangerously-skip-permissions`, which allows the AI agent to execute arbitrary commands without confirmation. The exact behavior cannot be guaranteed.

4. Run the loop:

```bash
./ralph.sh
```

By default, the loop runs up to 10 iterations. To override:

```bash
./ralph.sh 20
```

5. Once complete, start the application and try the API:

```bash
./mvnw spring-boot:run
curl -s localhost:8080/actuator/health
curl -s -X POST localhost:8080/users -H 'Content-Type: application/json' -d '{"name":"Alice","email":"alice@example.com"}'
curl -s localhost:8080/users
```

6. To run again, archive the current result branch and start fresh from step 2:

```bash
git branch -m "$(git branch --show-current)-$(git log -1 --format=%h)"
git switch main
```

## Example Ralph Loop Result

See the [`example/ralph-loop-result`](https://github.com/hainet50b/ralph-loop-example/tree/example/ralph-loop-result) branch for the result of a completed Ralph Loop run, including all generated source code, tests, and progress log.

The following run was based on [`example/ralph-loop-result-a9e88cc`](https://github.com/hainet50b/ralph-loop-example/tree/example/ralph-loop-result-a9e88cc) and completed using Claude Opus 4.6 in 9 iterations (871s total):

| # | Task                                       | Time   |
|---|--------------------------------------------|--------|
| 1 | Create User entity                         | 79s    |
| 2 | Create UserRepository                      | 77s    |
| 3 | Create UserService with CRUD methods       | 60s    |
| 4 | Create UserController with CRUD endpoints  | 116s   |
| 5 | Add exception handling for 404             | 105s   |
| 6 | Add exception handling for 409             | 117s   |
| 7 | Add Spring Actuator (health endpoint only) | 196s * |
| 8 | Configure H2 Console                       | 52s    |
| 9 | Create users.http (Post Task)              | 53s    |

\* Iteration 7 took significantly longer than others. From `progress.txt`:

> In Spring Boot 4, TestRestTemplate moved from spring-boot-test to the spring-boot-resttestclient module (package org.springframework.boot.resttestclient), and it requires spring-boot-restclient as a transitive dependency for RestTemplateBuilder. Both were added as test-scoped dependencies.

The AI agent spent extra time discovering and resolving Spring Boot 4 dependency changes through trial and error. Tracking time per iteration helps identify where the agent struggles.

## Key Files

This example uses the following files to drive the Ralph Loop:

```
.
├── prd.md          # What to build
├── prompt.md       # How each iteration should behave
├── ralph.sh        # Loop controller
├── progress.txt    # Work trail
└── metrics.csv     # Token usage and cost per iteration
```

### `prd.md`

The Product Requirements Document. Defines what to build — API specification, data model, and tasks as checkboxes.

> **Note:** In this example, specifications are written inline. You could also use dedicated specification formats (e.g., OpenAPI) as separate files and reference them from the PRD.

The PRD consists of the following sections:

- **Goal** — What to build, in one sentence.
- **Tech Stack** — Frameworks and libraries. Constrains the AI agent to technologies the developer can review and maintain.
- **API Endpoints** — The API contract the AI agent implements.
- **Data Model** — The data structure the AI agent implements.
- **Tasks** — Implementation work verified by unit tests. Each checkbox corresponds to one iteration of the loop.
- **Post Tasks** — Extra artifacts (e.g., `.http` files) that do not require test verification. Also one checkbox per iteration.
- **Completion Criteria** — A machine-verifiable definition of "done" that the AI agent checks after each task.

### `prompt.md`

Defines how each iteration should behave — task selection, testing requirements, commit workflow, and completion signal. Passed to Claude on every iteration. Separated from `ralph.sh` to keep loop control and iteration behavior independent.

### `ralph.sh`

The loop controller. A bash script that:

- Runs up to N iterations
- Calls `claude -p` with the contents of `prompt.md` on each iteration
- Tracks duration, token usage, and cost per iteration, and records them to `metrics.csv`
- Exits when:
  - Claude outputs `<promise>COMPLETE</promise>` (all tasks done)
  - The max iteration count is reached (exits with failure)

### `progress.txt`

An initially empty file where the AI agent logs what it did after each iteration — what was done, what files were changed, and any remarks (issues, workarounds, lessons learned). Serves as both a work trail and inter-iteration memory (since each `claude -p` call starts with a fresh context window).

### `metrics.csv`

A CSV file where `ralph.sh` records duration, token usage, and cost for each iteration. Tracking time per iteration helps identify tasks that are difficult or require attention, and can inform how to adjust task granularity in the PRD. Tracking tokens and cost helps verify that changes to `prd.md` or `prompt.md` do not cause unexpected spending.

> **Note:** `metrics.csv` captures how the code was built, not what was built. In traditional development, this kind of process data would not belong in a Git repository. However, in AI-driven development, recording the cost and behavior of the generation process is gaining importance. Until the ecosystem for tracking AI development processes matures, committing this file to Git is a temporary workaround. Whether to do so depends on your organization and project needs.

