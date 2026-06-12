#!/usr/bin/env bash
#
# Registers a Firefox node with the Selenium Grid Hub.
#
# Usage:
#   ./grid/start-node-firefox.sh [HUB_HOST] [MAX_SESSIONS]
#
# Requires:
#   - selenium-server-<version>.jar in this directory
#   - Firefox and a matching geckodriver on PATH
#     (Selenium Manager will attempt to resolve this automatically)

set -e

HUB_HOST="${1:-localhost}"
MAX_SESSIONS="${2:-2}"

SELENIUM_JAR=$(ls selenium-server-*.jar 2>/dev/null | head -n 1)

if [ -z "$SELENIUM_JAR" ]; then
    echo "ERROR: selenium-server-<version>.jar not found in $(pwd)"
    exit 1
fi

echo "Starting Firefox node, registering with hub at http://${HUB_HOST}:4444 ..."
java -jar "$SELENIUM_JAR" node \
    --hub "http://${HUB_HOST}:4444" \
    --port 5556 \
    --max-sessions "${MAX_SESSIONS}" \
    --override-max-sessions true
