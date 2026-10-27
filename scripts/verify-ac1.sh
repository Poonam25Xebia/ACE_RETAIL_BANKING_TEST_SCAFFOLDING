#!/usr/bin/env bash
# AC-1 (spec_functional) scripted check: start the packaged service using only environment
# variables and assert that the health-check endpoint returns HTTP 200 with status UP.
# Requires Docker (for PostgreSQL), Java 21 and Maven.
set -euo pipefail

cd "$(dirname "$0")/.."

export POSTGRES_HOST="${POSTGRES_HOST:-localhost}"
export POSTGRES_PORT="${POSTGRES_PORT:-55432}"
export POSTGRES_DB="${POSTGRES_DB:-account_management}"
export POSTGRES_USER="${POSTGRES_USER:-ac1_verify}"
export POSTGRES_PASSWORD="${POSTGRES_PASSWORD:-$(openssl rand -hex 16)}"
export SERVER_PORT="${SERVER_PORT:-18080}"
export GRPC_SERVER_PORT="${GRPC_SERVER_PORT:-19090}"

APP_PID=""
cleanup() {
  [[ -n "$APP_PID" ]] && kill "$APP_PID" 2>/dev/null || true
  docker compose down -v >/dev/null 2>&1 || true
}
trap cleanup EXIT

docker compose up -d --wait postgres
mvn -B -q -DskipTests package
java -jar target/account-management-service-*.jar > target/verify-ac1.log 2>&1 &
APP_PID=$!

url="http://localhost:${SERVER_PORT}/actuator/health"
for _ in $(seq 1 60); do
  if body="$(curl -fsS "$url" 2>/dev/null)"; then
    echo "GET $url -> 200 $body"
    [[ "$body" == *'"status":"UP"'* ]] && { echo "AC-1: PASS"; exit 0; }
  fi
  if ! kill -0 "$APP_PID" 2>/dev/null; then
    echo "AC-1: FAIL (service exited; see target/verify-ac1.log)"; tail -n 40 target/verify-ac1.log; exit 1
  fi
  sleep 1
done
echo "AC-1: FAIL (health endpoint did not respond)"; tail -n 40 target/verify-ac1.log; exit 1
