/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.jmelzer.jitty.SeleniumUtil.*;
import static com.jmelzer.jitty.Util.randomString;
import static junit.framework.TestCase.assertEquals;
import static org.apache.commons.lang3.RandomUtils.nextInt;

/**
 * Created by J. Melzer on 01.05.2017.
 */
@Ignore
public class AndroCupSeleniumTest {

    @Test
    public void testit() throws Exception {
        doLogin();

        long id = createTournament();
        driver().navigate().back();
        selectTournament(id);

        for (int i = 0; i < 12; i++) {
            createRandomPlayerAndAssignToClassA();
        }

        navigate("#/draw-select-class", "Auslosung: Klasse auswählen");
        waitForText(2, "Cup-Klasse");
        driver().findElement(By.linkText("Cup-Klasse")).click();

        for (int i = 1; i < 7; i++) {
//            System.out.println("########## createRound " + (i));
            createRound(i);
            enterResultsForGroup();

            driver().findElement(By.linkText("Hier klicken")).click();
//            System.out.println("########## Runde " + (i + 1));
            waitForText(2, "Runde " + (i + 1));
        }
    }

    private long createTournament() throws Exception {
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

        final String tName = "Selenium Test - " + randomString(5);
        driver().findElement(By.name("name")).sendKeys(tName);

        WebElement select = driver().findElement(By.id("tournamenttype"));
        select.click();
        WebElement in = select.findElement(By.tagName("input"));
        in.sendKeys(Keys.ARROW_DOWN);
        in.sendKeys(Keys.ARROW_DOWN);
        in.sendKeys(Keys.ENTER);


        //submit for
        driver().findElement(By.name("name")).submit();

        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Das Turnier wurde gespeichert");
            }
        });

        //Klasse bearneiten
        driver().findElement(By.linkText("Bearbeiten")).click();

        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Turnierklasse bearbeiten");
            }
        });

        select = driver().findElement(By.id("systeminput"));
        select.click();
        in = select.findElement(By.tagName("input"));
        in.sendKeys(Keys.ARROW_DOWN);
        in.sendKeys(Keys.ENTER);

        waitForText(1, "WTTV Andro Cup");

        driver().findElement(By.name("submit")).submit();

        //back to Turnier after save()
        (new WebDriverWait(driver, 2)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Turnier eingeben");
            }
        });

        //nochmal speichern
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
        String id = driver().getCurrentUrl().substring(driver().getCurrentUrl().lastIndexOf("/") + 1);
        return Long.valueOf(id);
    }

    private void selectTournament(long id) {
        waitForText(1, "Alle Turniere");
        driver.findElement(By.id("select-" + id)).click();
        waitForText(1, "Du bearbeitest das Turnier ");
    }

    private void createRandomPlayerAndAssignToClassA() throws InterruptedException {
        navigate("#/players", "Spieler anzeigen");

        navigate("#/player-add", "Spieler anlegen");
        driver().findElement(By.name("qttr")).sendKeys("" + nextInt(1600, 1799));
        driver().findElement(By.name("firstName")).sendKeys(randomString(6));
        driver().findElement(By.name("lastName")).sendKeys(randomString(6));
        driver().findElement(By.name("input_mobile_number")).sendKeys("0171-3" + nextInt(10000, 99999));
        driver().findElement(By.id("birthday")).sendKeys("01.01.1970");
        driver().findElement(By.name("gender2")).click();
        driver().findElement(By.id("input_email_address")).sendKeys(randomString(5) + "@" + randomString(6) + ".de");
        waitForText(2, "Cup-Klasse");
        Select select = new Select(driver().findElement(By.id("bootstrap-duallistbox-nonselected-list_")));
        select.selectByVisibleText("Cup-Klasse - 0 -> 3000");
        driver().findElement(By.name("firstName")).submit();

        waitForTitle(2, "Spieler anzeigen");
    }

    private void createRound(int r) throws InterruptedException {


        waitForText(2, "Auslosung der Klasse");
        waitForText(2, "Runde " + r);

        driver().findElement(By.id("automaticDraw")).click();
        Thread.sleep(100);
        driver().findElement(By.id("start")).click();

        waitForText(2, "Mögliche Begegnungen");
    }

    private void enterResultsForGroup() throws InterruptedException {
        final int GAMECOUNT = 6;
        navigate("#/tournamentdirector/overview", "Turnierleitung");
        List<WebElement> enterResultLinks = driver().findElements(By.xpath("//a[.='Ergebnis...']"));
        List<WebElement> possibleGamesLinks = driver().findElements(By.xpath("//a[.='Starten']"));
        assertEquals(GAMECOUNT, possibleGamesLinks.size());
        assertEquals(0, enterResultLinks.size());

        driver().findElement(By.id("assignFreeTables")).click();
        waitForText(1, "Ergebnis...");
        enterResultLinks = driver().findElements(By.xpath("//a[.='Ergebnis...']"));
        assertEquals(GAMECOUNT, enterResultLinks.size());

        enterResults(GAMECOUNT);

        waitForText(2, "Hier klicken");
    }


    private void enterResults(int size) throws InterruptedException {
        for (int i = 0; i < size; i++) {
            driver().findElement(By.linkText("Ergebnis...")).click();
            waitForText(2, "Spieler 1");
            driver().findElement(By.id("gameResult")).sendKeys("" + nextInt(0, 10) + " " + nextInt(0, 10) + " " + nextInt(0, 10));
            Thread.sleep(500);
            driver().findElement(By.id("btnOk")).click();
            Thread.sleep(500);
        }
    }
}
