#!/bin/bash
# wait-for-it.sh

TIMEOUT=${TIMEOUT:-15}
QUIET=${QUIET:-0}

echoerr() {
  if [ "$QUIET" -ne 1 ]; then echo "$@" 1>&2; fi
}

usage() {
  exitcode="$1"
  cat << USAGE >&2
Usage:
  $cmdname host:port [-t timeout] [-- command args]
  -t TIMEOUT | --timeout=TIMEOUT  Timeout in seconds, zero for no timeout
  -- COMMAND ARGS                 Execute command with args after the host is available
USAGE
  exit "$exitcode"
}

while [ "$#" -gt 0 ]; do
  case "$1" in
    *:* )
      HOST=$(printf "%s\n" "$1"| cut -d : -f 1)
      PORT=$(printf "%s\n" "$1"| cut -d : -f 2)
      shift 1
      ;;
    -t)
      TIMEOUT="$2"
      if [[ $TIMEOUT =~ ^[0-9]+$ ]]; then
        shift 2
      else
        echoerr "Error: invalid timeout value '$2'"
        usage 1
      fi
      ;;
    --timeout=*)
      TIMEOUT="${1#*=}"
      if [[ $TIMEOUT =~ ^[0-9]+$ ]]; then
        shift 1
      else
        echoerr "Error: invalid timeout value '${1#*=}'"
        usage 1
      fi
      ;;
    --)
      shift
      CLI=("$@")
      break
      ;;
    *)
      echoerr "Unknown argument: $1"
      usage 1
      ;;
  esac
done

if [ -z "$HOST" ] || [ -z "$PORT" ]; then
  echoerr "Error: you need to provide a host and a port."
  usage 1
fi

echoerr "Waiting for $HOST:$PORT..."
start_ts=$(date +%s)
while :
do
  if (( $(date +%s) - start_ts > TIMEOUT )); then
    echoerr "Error: timeout occurred after $TIMEOUT seconds waiting for $HOST:$PORT."
    exit 1
  fi
  (echo > /dev/tcp/"$HOST"/"$PORT") >/dev/null 2>&1
  result=$?
  if [ $result -eq 0 ]; then
    echoerr "$HOST:$PORT is available."
    break
  fi
  sleep 1
done

exec "${CLI[@]}"