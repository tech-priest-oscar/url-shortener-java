#!/usr/bin/env bash
# Tests the SMTP credentials in .env by authenticating and sending a test
# email. Usage: ./test-email.sh [recipient] [port]
#   recipient defaults to MAIL_USERNAME, port defaults to MAIL_PORT
#   port 587 uses STARTTLS; port 465 uses implicit TLS (smtps)
set -euo pipefail

cd "$(dirname "$0")"

if [[ ! -f .env ]]; then
    echo "❌ No .env file found next to this script" >&2
    exit 1
fi

set -a
. ./.env
set +a

: "${MAIL_HOST:?MAIL_HOST is not set in .env}"
: "${MAIL_PORT:?MAIL_PORT is not set in .env}"
: "${MAIL_USERNAME:?MAIL_USERNAME is not set in .env}"
: "${MAIL_PASSWORD:?MAIL_PASSWORD is not set in .env}"

TO="${1:-$MAIL_USERNAME}"
PORT="${2:-$MAIL_PORT}"

if [[ "$PORT" == "465" ]]; then
    URL="smtps://${MAIL_HOST}:${PORT}"   # implicit TLS
    TLS_FLAG=""
else
    URL="smtp://${MAIL_HOST}:${PORT}"    # STARTTLS
    TLS_FLAG="--ssl-reqd"
fi

echo "Testing ${MAIL_USERNAME} against ${MAIL_HOST}:${PORT}..."

exit_code=0
# TLS_FLAG is deliberately unquoted: it is empty for port 465
curl -sS --connect-timeout 15 --max-time 45 \
    --url "$URL" $TLS_FLAG \
    --user "${MAIL_USERNAME}:${MAIL_PASSWORD}" \
    --mail-from "${MAIL_USERNAME}" \
    --mail-rcpt "${TO}" \
    --upload-file - <<EOF || exit_code=$?
From: ${MAIL_USERNAME}
To: ${TO}
Subject: SMTP credential test

Credentials work. Sent $(date).
EOF

case $exit_code in
    0)  echo "✅ Success — authenticated and sent a test email to ${TO}" ;;
    67) echo "❌ Login denied — wrong username or app password (curl exit 67)" >&2 ;;
    28) echo "❌ Timed out — port ${PORT} may be blocked on this network; try: $0 ${TO} 465" >&2 ;;
    35|64) echo "❌ TLS failed — check that the port matches the TLS mode (curl exit $exit_code)" >&2 ;;
    *)  echo "❌ Failed with curl exit code $exit_code" >&2 ;;
esac
exit $exit_code
