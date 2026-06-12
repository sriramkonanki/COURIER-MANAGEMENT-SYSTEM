package com.framework.tests;

import com.framework.utils.ConfigReader;
import com.framework.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

/**
 * Base class for all test classes.
 *
 * Each test method runs on its own thread when TestNG is configured for
 * parallel execution (see testng.xml, parallel="methods"/"tests").
 * The "browser" parameter comes from the <test> tag in testng.xml, which
 * lets different parallel test groups target different browsers on the
 * Selenium Grid simultaneously.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(String browser) {
        DriverFactory.initDriver(browser);
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Hook point: capture screenshot, attach logs, etc.
            System.err.println("Test failed: " + result.getMethod().getMethodName()
                    + " on thread " + Thread.currentThread().getId());
        }
        DriverFactory.quitDriver();
    }
}
