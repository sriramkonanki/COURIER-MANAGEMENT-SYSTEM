#!/usr/bin/env bash
#
# Registers an Edge node with the Selenium Grid Hub.
#
# Usage:
#   ./grid/start-node-edge.sh [HUB_HOST] [MAX_SESSIONS]
#
# Requires:
#   - selenium-server-<version>.jar in this directory
#   - Microsoft Edge and a matching msedgedriver on PATH
#     (Selenium Manager will attempt to resolve this automatically)

set -e

HUB_HOST="${1:-localhost}"
MAX_SESSIONS="${2:-2}"

SELENIUM_JAR=$(ls selenium-server-*.jar 2>/dev/null | head -n 1)

if [ -z "$SELENIUM_JAR" ]; then
    echo "ERROR: selenium-server-<version>.jar not found in $(pwd)"
    exit 1
fi

echo "Starting Edge node, registering with hub at http://${HUB_HOST}:4444 ..."
java -jar "$SELENIUM_JAR" node \
    --hub "http://${HUB_HOST}:4444" \
    --port 5557 \
    --max-sessions "${MAX_SESSIONS}" \
    --override-max-sessions true
