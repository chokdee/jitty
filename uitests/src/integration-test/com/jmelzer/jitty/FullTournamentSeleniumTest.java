/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty;

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
 * Created by J. Melzer on 23.12.2016.
 */
public class FullTournamentSeleniumTest {

    @Test
    public void testit() throws Exception {
        doLogin();

        long id = createTournament();
        driver().navigate().back();
        selectTournament(id);

        for (int i = 0; i < 8; i++) {
            createRandomPlayerAndAssignToClassA();
        }

        createDrawForBKlasse();
        enterResultsForGroup();
        createDrawForKO();
        enterResultsForKO();
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
//        in.sendKeys("Allg. Turnier (z.B. Gruppe / KO Runde etc.");
        in.sendKeys(Keys.ARROW_DOWN);
        in.sendKeys(Keys.ENTER);


        //submit for
        driver().findElement(By.name("name")).submit();

        (new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains("Das Turnier wurde gespeichert");
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
        waitForText(2, "A-Klasse");
        Select select = new Select(driver().findElement(By.id("bootstrap-duallistbox-nonselected-list_")));
        select.selectByVisibleText("B-Klasse - 0 -> 1800");
        driver().findElement(By.name("firstName")).submit();

        waitForTitle(2, "Spieler anzeigen");
    }

    private void createDrawForBKlasse() throws InterruptedException {
        //overview list of classes
        navigate("#/draw-select-class", "Auslosung: Klasse auswählen");
        waitForText(2, "B-Klasse");
        driver().findElement(By.linkText("B-Klasse")).click();
        waitForText(2, "Auslosung der Klasse");

        driver().findElement(By.id("btnSelectSystem")).click();


        Select select = new Select(driver().findElement(By.id("modus1")));
        select.selectByIndex(1);

        driver().findElement(By.id("automaticDraw")).click();
        Thread.sleep(100);
//        driver().findElement(By.id("saveDraw")).click();
//        Thread.sleep(100);
        driver().findElement(By.id("start")).click();

        waitForText(2, "Mögliche Begegnungen");
    }

    private void enterResultsForGroup() throws InterruptedException {
        navigate("#/tournamentdirector/overview", "Turnierleitung");
        List<WebElement> enterResultLinks = driver().findElements(By.xpath("//a[.='Ergebnis...']"));
        List<WebElement> possibleGamesLinks = driver().findElements(By.xpath("//a[.='Starten']"));
        assertEquals(4, possibleGamesLinks.size());
        assertEquals(0, enterResultLinks.size());

        driver().findElement(By.id("assignFreeTables")).click();
        waitForText(1, "Ergebnis...");
        enterResultLinks = driver().findElements(By.xpath("//a[.='Ergebnis...']"));
        assertEquals(4, enterResultLinks.size());

        enterResults(4);

        driver().findElement(By.id("assignFreeTables")).click();
        waitForText(1, "Ergebnis...");
        enterResultLinks = driver().findElements(By.xpath("//a[.='Ergebnis...']"));
        assertEquals(4, enterResultLinks.size());
        enterResults(4);

        driver().findElement(By.id("assignFreeTables")).click();
        waitForText(1, "Ergebnis...");
        enterResultLinks = driver().findElements(By.xpath("//a[.='Ergebnis...']"));
        assertEquals(4, enterResultLinks.size());
        enterResults(4);

        waitForText(2, "Hier klicken");
    }

    private void createDrawForKO() throws InterruptedException {
        navigate("#/tournamentdirector/overview", "Turnierleitung");
        driver().findElement(By.linkText("Hier klicken")).click();
        waitForText(1, "Phase");
        Select select = new Select(driver().findElement(By.id("phase")));
        select.selectByVisibleText("KO");

        driver().findElement(By.id("automaticDraw")).click();
        Thread.sleep(200);
        driver().findElement(By.id("startKO")).click();

        waitForText(1, "Mögliche Begegnungen");
    }

    private void enterResultsForKO() throws InterruptedException {
        driver().findElement(By.id("assignFreeTables")).click();
        Thread.sleep(200);
        enterResults(2);
        driver().findElement(By.id("assignFreeTables")).click();
        Thread.sleep(100);
        enterResults(1);
        navigate("#/liveview/overview", "Klasse auswählen");
        driver().findElement(By.linkText("KO Feld anzeigen")).click();
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
