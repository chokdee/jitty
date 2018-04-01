/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.jmelzer.jitty.SeleniumUtil.*;
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
        waitForText(2, "Benutzername");
        Assert.assertEquals("Jitty - Login", driver().getTitle());

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
        waitForText(2, "Benutzername");
        Assert.assertEquals("Jitty - Login", driver().getTitle());

    }
}
