/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static com.jmelzer.jitty.Util.*;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 20.08.2016.
 * integration test for tournament views
 */
public class TournamentHttpTest {

    @Test
    public void testList() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/tournaments");
        assertTrue(page.asText().contains("Jitty Open 2016"));
    }
    @Test
    public void newTournament() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/tournaments");
        HtmlAnchor anchor = page.getAnchorByName("newTournament");
        assertNotNull(anchor);
        HtmlPage newUserPage = anchor.click();
        assertTrue(newUserPage.asText().contains("Turnier eingeben"));

        HtmlForm form = newUserPage.getFormByName("tournamentForm");

        HtmlInput submitInput = form.getInputByName("submit");
        newUserPage  = submitInput.click();
        //validation triggered
        assertTrue(newUserPage.asText().contains("Bitte einen Namen eingeben"));

        //fill out form
        String name = randomString(10);
        form.getInputByName("name").setValueAttribute(name);

        HtmlPage listPage = submitInput.click();
        assertTrue("Must contains " +  name, listPage.asText().contains(name));
    }
    @Test
    public void edit() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/tournaments");
        HtmlAnchor anchor = page.getAnchorByHref("#/tournaments/2");
        assertNotNull(anchor);
        HtmlPage teditPage = anchor.click();
        assertTrue(teditPage.asText().contains("Turnier eingeben"));
        assertTrue(teditPage.asText().contains("Jitty Open 2016"));

        HtmlForm form = teditPage.getFormByName("tournamentForm");
        form.getInputByName("name").setValueAttribute("HtmlUnit Open 2016");
        HtmlInput submitInput = form.getInputByName("submit");
        HtmlPage listPage = submitInput.click();
        assertTrue("Listpage = " + listPage.asText() , listPage.asText().contains("Alle Turniere"));
//        System.out.println("listPage = " + listPage.asText());
        assertTrue("Must contains HtmlUnit Open 2016" , listPage.asText().contains("HtmlUnit Open 2016"));

    }
    @Test
    public void neweditdeletetc() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/tournaments");
        HtmlAnchor anchor = page.getAnchorByHref("#/tournaments/2");
        HtmlPage teditPage = anchor.click();
        String startPage = teditPage.asXml();


        String tcName = randomString(5);
        anchor = teditPage.getAnchorByName("newTC");
        HtmlPage tcPage = anchor.click();
        assertTrue(tcPage.asText().contains("Turnierklasse eingeben"));
        assertFalse(tcPage.asText(), tcPage.asText().contains(tcName));

        HtmlForm form = tcPage.getFormByName("tournamentClassForm");

        HtmlInput submitInput = form.getInputByName("submit");
        tcPage  = submitInput.click();
        assertTrue(tcPage.asText().contains("Bitte einen Namen eingeben"));

        form.getInputByName("name").setValueAttribute(tcName);
        form.getInputByName("startTTR").setValueAttribute("10");
        form.getInputByName("endTTR").setValueAttribute("999");
        submitInput = form.getInputByName("submit");
        tcPage  = submitInput.click();
        assertTrue(tcPage.asText().contains("Turnier eingeben"));
        assertTrue(tcPage.asText().contains(tcName));


        String diff = StringUtils.difference(startPage, tcPage.asXml());
        String nr = StringUtils.substringBetween(diff, "href=\"#/tournament/tournament-classes/", "\"" );

        //Edit the same tournament class
        HtmlAnchor anchorEdit = tcPage.getAnchorByName("edit"+nr);
        tcPage = anchorEdit.click();
        assertTrue(tcPage.asText().contains("Turnierklasse bearbeiten"));
        assertTrue(tcPage.asText().contains(tcName));
        form = tcPage.getFormByName("tournamentClassForm");
//        System.out.println("tcPage.asText() = " + tcPage.asText());
        //change the name
        tcName = tcName + "E";
        form.getInputByName("name").setValueAttribute(tcName);
        submitInput = form.getInputByName("submit");
        tcPage  = submitInput.click();
        assertTrue(tcPage.getUrl().toString(), tcPage.asText().contains("Turnier eingeben"));
        assertTrue(tcPage.asText().contains(tcName));


        HtmlAnchor anchorDelete = tcPage.getAnchorByName("delete"+nr);
        tcPage = anchorDelete.click();

        assertTrue(tcPage.asText().contains("Turnier eingeben"));
        assertFalse("must be deleted", tcPage.asText().contains(tcName));
    }
}
