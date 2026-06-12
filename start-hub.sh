#!/usr/bin/env bash
#
# Starts the Selenium Grid 4 Hub (the "router" process).
# All nodes register with this hub, and all RemoteWebDriver
# sessions in the framework connect to it via grid.url in
# config.properties (default: http://localhost:4444/wd/hub).
#
# Usage:
#   ./grid/start-hub.sh
#
# Requires selenium-server-<version>.jar to be present in this
# directory (download from https://www.selenium.dev/downloads/).

set -e

SELENIUM_JAR=$(ls selenium-server-*.jar 2>/dev/null | head -n 1)

if [ -z "$SELENIUM_JAR" ]; then
    echo "ERROR: selenium-server-<version>.jar not found in $(pwd)"
    echo "Download it from https://www.selenium.dev/downloads/ and place it in this directory."
    exit 1
fi

echo "Starting Selenium Grid Hub using $SELENIUM_JAR ..."
java -jar "$SELENIUM_JAR" hub \
    --port 4444 \
    --reject-unsupported-caps true
