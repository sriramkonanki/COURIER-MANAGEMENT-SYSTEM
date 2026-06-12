package com.framework.utils;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Creates and manages RemoteWebDriver instances that point at a
 * Selenium Grid 4 hub. A ThreadLocal is used so that each TestNG
 * thread (i.e. each parallel test) gets its own isolated driver,
 * which is what allows the framework to safely run suites in
 * parallel across multiple nodes/browsers registered with the hub.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverFactory() {
    }

    /**
     * Initializes a RemoteWebDriver session on the Grid hub for the
     * given browser and stores it in the current thread's ThreadLocal.
     *
     * @param browser "chrome", "firefox" or "edge"
     */
    public static void initDriver(String browser) {
        String gridUrl = ConfigReader.getGridUrl();
        MutableCapabilities options = buildCapabilities(browser);

        try {
            WebDriver driver = new RemoteWebDriver(new URL(gridUrl), options);
            driver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()))
                    .pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
            driver.manage().window().maximize();

            DRIVER_THREAD_LOCAL.set(driver);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Selenium Grid hub URL: " + gridUrl, e);
        }
    }

    /**
     * @return the WebDriver instance bound to the current thread.
     */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized for this thread. Call initDriver() first.");
        }
        return driver;
    }

    /**
     * Quits the driver for the current thread and clears the ThreadLocal
     * so resources aren't leaked between tests.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }

    private static MutableCapabilities buildCapabilities(String browser) {
        boolean headless = ConfigReader.isHeadless();

        switch (browser.toLowerCase()) {
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                return options;
            }
            case "edge": {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                return options;
            }
            case "chrome":
            default: {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
                return options;
            }
        }
    }
}
