package com.jmelzer.jitty;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.jmelzer.jitty.SeleniumUtil.doLogin;
import static com.jmelzer.jitty.SeleniumUtil.driver;
import static com.jmelzer.jitty.SeleniumUtil.navigate;
import static com.jmelzer.jitty.Util.randomString;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by J. Melzer on 23.12.2016.
 */
public class FullTournamentSeleniumTest {

    @Test
    public void testit() throws Exception{
        doLogin();

        long id = createTournament();
        driver().navigate().back();
        selectTournament(id);

        for (int i = 0; i < 24; i++) {
            createRandomPlayerAndAssignToClassA();
        }
    }

    private void createRandomPlayerAndAssignToClassA() {
        navigate("#/players", "Spieler anzeigen");

        navigate("#/player-add", "Spieler anlegen");
        driver().findElement(By.name("qttr")).sendKeys("" + nextInt(1600, 1799));
        driver().findElement(By.name("firstName")).sendKeys(randomString(6));
        driver().findElement(By.name("lastName")).sendKeys(randomString(6));
        driver().findElement(By.name("input_mobile_number")).sendKeys("0171-3" + nextInt(10000, 99999));
        driver().findElement(By.id("birthday")).sendKeys("01.01.1970");
        driver().findElement(By.name("gender2")).click();
        driver().findElement(By.id("input_email_address")).sendKeys(randomString(5) + "@" + randomString(6) + ".de");
        Select select = new Select(driver().findElement(By.id("bootstrap-duallistbox-nonselected-list_")));
        select.selectByVisibleText("B-Klasse - 0 -> 1800");
        driver().findElement(By.name("firstName")).submit();

        (new WebDriverWait(driver, 2)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().contains("Spieler anzeigen");
            }
        });
    }

    private void selectTournament(long id) {
        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Alle Turniere");
            }
        });
        driver.findElement(By.id("select-"+id)).click();
        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Du bearbeitest das Turnier ");
            }
        });
    }

    private long createTournament() throws Exception{
        driver().navigate().to("http://localhost:8080/#/tournaments");
        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Alle Turniere");
            }
        });
        driver().findElement(By.name("newTournament")).click();

        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Turnier eingeben");
            }
        });

        String tName = "Selenium Test - " + randomString(5);
        driver().findElement(By.name("name")).sendKeys(tName);
        driver().findElement(By.name("name")).submit();

        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Alle Turniere") && d.getPageSource().contains(tName);
            }
        });
        driver().findElement(By.linkText(tName)).click();

        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Turnier eingeben");
            }
        });
        String id = driver().getCurrentUrl().substring(driver().getCurrentUrl().lastIndexOf("/")+1);
        return Long.valueOf(id);
    }
}
