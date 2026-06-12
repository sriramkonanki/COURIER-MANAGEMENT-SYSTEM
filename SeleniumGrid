# Distributed Selenium Grid Test Framework

A Java + TestNG + Maven automation framework that executes test suites
**in parallel across multiple browsers and Selenium Grid 4 nodes**.

## Features

- **Selenium Grid 4** hub/node architecture (CLI-based, no Docker required)
- **Parallel execution** via TestNG (`parallel="tests"`, configurable thread count)
- **Cross-browser support**: Chrome, Firefox, Edge — each test block runs against
  a different browser, with sessions routed by the hub to whichever node
  supports that capability
- **Thread-safe driver management** using `ThreadLocal<WebDriver>` so parallel
  threads never share a driver instance
- **Centralized configuration** via `config.properties`, overridable from the
  command line (`-Dbrowser=...`, `-DgridUrl=...`)
- Clean separation of framework utilities (`com.framework.utils`) and test
  classes (`com.framework.tests`)

## Project Structure

```
selenium-grid-framework/
├── pom.xml
├── testng.xml                     # Parallel suite definition
├── grid/
│   ├── start-hub.sh                # Starts the Grid hub
│   ├── start-node-chrome.sh        # Registers a Chrome node
│   ├── start-node-firefox.sh       # Registers a Firefox node
│   └── start-node-edge.sh          # Registers an Edge node
└── src/
    ├── main/
    │   ├── java/com/framework/utils/
    │   │   ├── ConfigReader.java   # Loads & resolves config values
    │   │   └── DriverFactory.java  # Builds RemoteWebDriver sessions
    │   └── resources/
    │       └── config.properties
    └── test/java/com/framework/tests/
        ├── BaseTest.java           # @BeforeMethod/@AfterMethod lifecycle
        ├── GoogleSearchTest.java
        └── NavigationTest.java
```

## Prerequisites

- Java 11+
- Maven 3.6+
- [Selenium Server standalone jar](https://www.selenium.dev/downloads/) (Grid 4)
- Chrome / Firefox / Edge installed on whichever machines will run nodes
  (Selenium Manager resolves matching drivers automatically)

## Running the Grid

1. Download `selenium-server-<version>.jar` into the `grid/` directory.
2. Start the hub:
   ```bash
   ./grid/start-hub.sh
   ```
3. On the same machine or other machines on the network, register nodes:
   ```bash
   ./grid/start-node-chrome.sh   <hub-ip> 4
   ./grid/start-node-firefox.sh  <hub-ip> 4
   ./grid/start-node-edge.sh     <hub-ip> 4
   ```
4. Verify the grid console at `http://<hub-ip>:4444`.

## Running the Tests

By default, tests connect to `http://localhost:4444/wd/hub` as configured in
`src/main/resources/config.properties`.

```bash
mvn clean test
```

To point at a remote hub:

```bash
mvn clean test -DgridUrl=http://192.168.1.10:4444/wd/hub
```

The included `testng.xml` runs three `<test>` blocks (Chrome, Firefox, Edge)
concurrently (`parallel="tests"`, `thread-count="3"`), so all three browser
sessions are dispatched to the grid and executed at the same time, distributed
across whichever nodes have capacity.

## Configuration

All runtime settings live in `src/main/resources/config.properties`:

| Key                  | Description                                  |
|----------------------|-----------------------------------------------|
| `grid.url`           | Selenium Grid hub endpoint                    |
| `default.browser`    | Browser used if none is specified             |
| `headless`           | Run browsers headless (`true`/`false`)        |
| `implicit.wait`      | Implicit wait in seconds                      |
| `page.load.timeout`  | Page load timeout in seconds                  |
| `base.url`           | Application under test base URL               |

Any key can be overridden at runtime with a matching `-D` system property,
e.g. `-Dheadless=false`.

## Extending the Framework

- Add new test classes under `src/test/java/com/framework/tests/`, extending
  `BaseTest` to inherit driver setup/teardown.
- Add new `<test>` blocks to `testng.xml` with a `browser` parameter to add
  more parallel cross-browser lanes.
- `DriverFactory.buildCapabilities()` is the single place to add new browser
  capabilities or grid-specific options (e.g. tags, timeouts).

## Possible Extensions

- Page Object Model layer for application-specific pages
- Allure/ExtentReports integration for richer reporting
- Dockerized hub/node setup (`docker-compose`) for CI environments
- CI pipeline (GitHub Actions) to spin up the grid and run `mvn test`
