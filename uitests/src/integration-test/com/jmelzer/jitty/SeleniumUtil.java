/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.jmelzer.jitty.SeleniumUtil.DriverType.FF;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by J. Melzer on 23.12.2016.
 */
public class SeleniumUtil {
    static WebDriver driver;

    static DriverType driverType = FF;

    public static void quit() {
        driver.quit();
        driver = null;
    }

    public static String doLogin() throws java.io.IOException {
        navigate("#/login", "Login");
        String pageLogin = driver().getPageSource();

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.clear();
        usernameField.sendKeys("user");
        WebElement pwField = driver.findElement(By.name("password"));
        pwField.sendKeys("42");
        pwField.submit();

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().contains("startseite");
            }
        });

        assertThat(driver().getPageSource(), containsString("Willkommen bei Jitty"));
        return driver().getPageSource();
    }

    public static String navigate(String urlpart, String waitForTitle) {
        System.out.println("calling " + "http://localhost:8080/" + urlpart);
        driver().navigate().to("http://localhost:8080/" + urlpart);
        waitForTitle(10, waitForTitle);
        return driver().getPageSource();
    }

    public static WebDriver driver() {
        if (driver == null) {

            switch (driverType) {
                case FF:
                    System.setProperty("webdriver.gecko.driver", "c:\\batch\\geckodriver.exe");
                    ProfilesIni profile = new ProfilesIni();
                    FirefoxOptions options = new FirefoxOptions();
                    options.setBinary("C:\\tools\\FirefoxQ\\firefox.exe");
                    FirefoxProfile myprofile = profile.getProfile("selenium");
                    myprofile.setPreference("browser.startup.homepage", "about:blank");
                    myprofile.setPreference("xpinstall.signatures.required", false);
                    options.setProfile(myprofile);
                    driver = new FirefoxDriver(options);
                    break;

                case CHROME:

                    String pathToChromeDriver = "c:\\batch\\chromedriver.exe";
                    System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

                    driver = new ChromeDriver();
                    break;
                case PHANTOM:
                    DesiredCapabilities capabilities = new DesiredCapabilities(BrowserType.PHANTOMJS, "", Platform.ANY);
                    String pathToPhantomDriver = "C:\\tools\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
//                    System.setProperty("phantomjs.binary.path", pathToPhantomDriver);
                    capabilities.setCapability("phantomjs.binary.path", pathToPhantomDriver);
                    driver = new PhantomJSDriver(capabilities);
                    break;
                case HTMLUNIT:
                    driver = new HtmlUnitDriver();
                    break;
            }
        }
        return driver;
    }

    public static void waitForTitle(int timeout, final String text) {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
//                System.out.println(d.getTitle());
//                System.out.println(d.getPageSource());
                return d.getTitle().contains(text);
            }
        });
    }

    public static void waitForText(int timeout, final String text) {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                System.out.println("check " + text);
                return d.getPageSource().contains(text);
            }
        });
    }
    enum DriverType {
        CHROME, FF, IE, HTMLUNIT, PHANTOM
    }
}
