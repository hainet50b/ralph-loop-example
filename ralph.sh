#!/usr/bin/env bash
set -euo pipefail

MAX_ITERATIONS=${1:-10}
PROMPT_FILE="prompt.md"
SLEEP_SECONDS=2

echo "=== Ralph Loop ==="
echo "Max iterations: $MAX_ITERATIONS"
echo ""

for ((i = 1; i <= MAX_ITERATIONS; i++)); do
  echo "--- Iteration $i / $MAX_ITERATIONS ---"

  result=$(claude --dangerously-skip-permissions -p "$(cat "$PROMPT_FILE")" 2>&1) || true
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
