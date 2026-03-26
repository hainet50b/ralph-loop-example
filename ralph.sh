#!/usr/bin/env bash
set -euo pipefail

MAX_ITERATIONS=${1:-10}
PRD_FILE="prd.md"
SLEEP_SECONDS=2

PROMPT=$(cat <<'EOF'
Read the PRD at prd.md and progress.txt, then follow these instructions:

1. Find the first unchecked task (- [ ]) in the Tasks section.
2. Implement ONLY that one task.
3. For every implementation task, also write corresponding unit tests using JUnit 5. Mock dependencies where appropriate.
4. Run `./mvnw test` to verify all tests pass.
5. If tests pass, mark the task as checked (- [x]) in the PRD.
6. Append a progress entry to progress.txt with the task name, what was done, and any issues encountered.
7. Stage all changes and create a git commit with a descriptive message.
8. If ALL tasks are now checked, include the exact text <promise>COMPLETE</promise> in your response.

IMPORTANT:
- Work on only ONE task, then stop.
- Do not proceed to the next task.
- Do not skip failing tests. Fix the code until tests pass before marking complete.
EOF
)

echo "=== Ralph Loop ==="
echo "Max iterations: $MAX_ITERATIONS"
echo "PRD: $PRD_FILE"
echo ""

for ((i = 1; i <= MAX_ITERATIONS; i++)); do
  echo "--- Iteration $i / $MAX_ITERATIONS ---"

  result=$(claude --dangerously-skip-permissions -p "$PROMPT" 2>&1) || true
  echo "$result"

  if [[ "$result" == *"<promise>COMPLETE</promise>"* ]]; then
    echo ""
    echo "=== All tasks complete! ==="
    exit 0
  fi

  sleep "$SLEEP_SECONDS"
done

echo ""
echo "=== Reached max iterations ($MAX_ITERATIONS) without completing all tasks ==="
exit 1
