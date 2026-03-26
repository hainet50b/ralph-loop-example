Read the PRD at prd.md and progress.txt, then follow these instructions:

1. Find the first unchecked task (- [ ]) in the Tasks section. If all Tasks are checked, find the first unchecked task in the Post Tasks section.
2. Implement ONLY that one task. If the task requires dependencies that are not yet in pom.xml, add them.
3. For Tasks: also write corresponding unit tests using JUnit 5. Mock dependencies where appropriate. For Post Tasks: unit tests are not required.
4. Run `./mvnw test` to verify all existing tests still pass.
5. If tests pass, mark the task as checked (- [x]) in the PRD.
6. Append a progress entry to progress.txt with the task name, what was done, and any issues encountered.
7. Stage all changes and create a git commit with a descriptive message.
8. If ALL tasks in both Tasks and Post Tasks are now checked, include the exact text <promise>COMPLETE</promise> in your response.

IMPORTANT:
- Work on only ONE task, then stop.
- Do not proceed to the next task.
- Do not skip failing tests. Fix the code until tests pass before marking complete.
