package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * A second example test class. Running this alongside
 * GoogleSearchTest under different <test> blocks in testng.xml
 * (each pinned to a different browser) demonstrates the framework
 * distributing parallel sessions across multiple nodes/browsers
 * registered with the Selenium Grid hub.
 */
public class NavigationTest extends BaseTest {

    @Test(description = "Browser window can be resized and reports correct dimensions")
    public void windowCanBeResized() {
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1024, 768));
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();

        Assert.assertEquals(size.getWidth(), 1024, "Window width should match requested size");
        Assert.assertEquals(size.getHeight(), 768, "Window height should match requested size");
    }

    @Test(description = "Current URL matches the configured base URL")
    public void currentUrlMatchesBaseUrl() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("google.com"),
                "Driver should have navigated to the configured base URL");
    }
}
