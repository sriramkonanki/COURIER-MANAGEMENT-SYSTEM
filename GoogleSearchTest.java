package com.framework.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Example test verifying basic search functionality.
 * Demonstrates a real interaction running against a remote node
 * provisioned by the Selenium Grid hub.
 */
public class GoogleSearchTest extends BaseTest {

    @Test(description = "Search box accepts input and returns results")
    public void searchReturnsResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOf(driver.findElement(By.name("q"))));

        searchBox.sendKeys("Selenium Grid distributed testing");
        searchBox.submit();

        wait.until(ExpectedConditions.titleContains("Selenium Grid"));

        Assert.assertTrue(driver.getTitle().toLowerCase().contains("selenium grid"),
                "Page title should reflect the search query");
    }

    @Test(description = "Home page loads with expected title")
    public void homePageTitleIsCorrect() {
        Assert.assertTrue(driver.getTitle().toLowerCase().contains("google"),
                "Expected the base URL to load the Google home page");
    }
}
