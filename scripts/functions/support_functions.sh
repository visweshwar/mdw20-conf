set -e # Exit on any error

run() {
  set -xe # Echo command
  "$@"
  { set +x; } 2>/dev/null  # Turn off echo without writing to console
}

exec 3>&1; # Create FD 3 for console output
log_stdin() {
  echo "++STDIN:" >&3
  tee /dev/fd/3
}

promptForVariable() {
  local _VARIABLE_NAME="$1"; shift
  local _SAVE_TO_FILE="$1"; shift
  local _PROMPT="$1"; shift

  if [[ ! -f "$_SAVE_TO_FILE" ]]; then
    read -p "$_PROMPT: "
    mkdir -p $(dirname "$_SAVE_TO_FILE")
    echo -n "$REPLY" > "$_SAVE_TO_FILE"
  fi
  eval "$_VARIABLE_NAME=\"$(cat "$_SAVE_TO_FILE" | sed 's/"/\\"/g')\""
}


