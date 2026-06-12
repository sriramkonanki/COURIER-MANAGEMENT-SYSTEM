#!/usr/bin/env bash
#
# Registers a Chrome node with the Selenium Grid Hub.
#
# Usage:
#   ./grid/start-node-chrome.sh [HUB_HOST] [MAX_SESSIONS]
#
# Example (node running on a different machine than the hub):
#   ./grid/start-node-chrome.sh 192.168.1.10 4
#
# Requires:
#   - selenium-server-<version>.jar in this directory
#   - Google Chrome and a matching chromedriver on PATH
#     (Selenium Manager will attempt to resolve this automatically)

set -e

HUB_HOST="${1:-localhost}"
MAX_SESSIONS="${2:-2}"

SELENIUM_JAR=$(ls selenium-server-*.jar 2>/dev/null | head -n 1)

if [ -z "$SELENIUM_JAR" ]; then
    echo "ERROR: selenium-server-<version>.jar not found in $(pwd)"
    exit 1
fi

echo "Starting Chrome node, registering with hub at http://${HUB_HOST}:4444 ..."
java -jar "$SELENIUM_JAR" node \
    --hub "http://${HUB_HOST}:4444" \
    --port 5555 \
    --max-sessions "${MAX_SESSIONS}" \
    --override-max-sessions true
