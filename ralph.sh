#!/usr/bin/env bash
set -euo pipefail

MAX_ITERATIONS=${1:-10}
PROMPT_FILE="prompt.md"
SLEEP_SECONDS=2

echo "=== Ralph Loop ==="
echo "Max iterations: $MAX_ITERATIONS"
echo ""

total_start=$SECONDS

for ((i = 1; i <= MAX_ITERATIONS; i++)); do
  echo "--- Iteration $i / $MAX_ITERATIONS ---"

  iter_start=$SECONDS
  result=$(claude --dangerously-skip-permissions -p "$(cat "$PROMPT_FILE")" 2>&1) || true
  iter_elapsed=$((SECONDS - iter_start))

  echo "$result"
  echo ""
  echo "--- Iteration $i completed in ${iter_elapsed}s ---"
  echo ""

  if [[ "$result" == *"<promise>COMPLETE</promise>"* ]]; then
    total_elapsed=$((SECONDS - total_start))
    echo "=== All tasks complete! (total: ${total_elapsed}s) ==="
    exit 0
  fi

  sleep "$SLEEP_SECONDS"
done

total_elapsed=$((SECONDS - total_start))
echo "=== Reached max iterations ($MAX_ITERATIONS) without completing all tasks (total: ${total_elapsed}s) ==="
exit 1
