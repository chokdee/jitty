package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.html.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.jmelzer.jitty.SeleniumUtil.driver;
import static com.jmelzer.jitty.SeleniumUtil.quit;
import static com.jmelzer.jitty.Util.webClient;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 14.08.2016.
 */
public class LoginHttpSeleniumTest {

    @AfterClass
    public static void after() {
        quit();
    }

    @Test
    public void homePage() throws Exception {

        driver().navigate().to("http://localhost:8080");
        Assert.assertEquals("Jitty - Startseite", driver().getTitle());

        String pageAsText = driver().getPageSource();
        assertTrue(pageAsText.contains("Bitte zunächst einloggen"));

        driver().navigate().to("http://localhost:8080/#/login");
        WebElement usernameField = driver().findElement(By.name("username"));
        usernameField.sendKeys("Cheese!");
        WebElement pwField = driver().findElement(By.name("password"));
        pwField.submit();

        assertTrue(driver().getPageSource().contains("Anmeldung ist fehlgeschlagen"));

        SeleniumUtil.doLogin();
    }


    @Test
    public void testLogout() throws Exception {

        SeleniumUtil.doLogin();

        WebElement anchor = driver().findElement(By.name("logout"));
        assertNotNull(anchor);
        anchor.click();
        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Bitte zunächst einloggen");
            }
        });

    }
}
