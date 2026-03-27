Read the PRD at prd.md and progress.txt, then follow these instructions:

1. Find the first unchecked task (- [ ]) in the PRD. Process Tasks first, then Post Tasks.
2. Implement ONLY that one task. If the task requires dependencies that are not yet in `pom.xml`, add them.
3. If the task is from Tasks: also write corresponding unit tests using JUnit 5. Mock dependencies where appropriate.
4. Run `./mvnw test` to verify all existing tests still pass.
5. If tests pass, mark the task as checked (- [x]) in the PRD.
6. Append a progress entry to `progress.txt` using the following format:

```
## Task: <task name>

**Timestamp:** <current UTC time in ISO 8601 format>

**What was done:**
<summary of implementation — use multiple lines if needed>

**What was changed:**
<list of files added or modified — one per line>

**Remarks:**
<any issues encountered, workarounds applied, or lessons learned — write as much as needed>
```

7. Stage all changes and create a git commit with a descriptive message.
8. If ALL tasks in both Tasks and Post Tasks are now checked, include the exact text `<promise>COMPLETE</promise>` in your response.

IMPORTANT:
- Work on only ONE task, then stop.
- Do not proceed to the next task.
- Do not skip failing tests. Fix the code until tests pass before marking complete.
