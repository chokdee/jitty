package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;

import static com.jmelzer.jitty.Util.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 20.08.2016.
 * integration test for user views
 */
public class TournamentTest {

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
        HtmlPage newUserPage = anchor.click();
        assertTrue(newUserPage.asText().contains("Turnier eingeben"));
        assertTrue(newUserPage.asText().contains("Jitty Open 2016"));

        HtmlForm form = newUserPage.getFormByName("tournamentForm");
        form.getInputByName("name").setValueAttribute("HtmlUnit Open 2016");
        HtmlInput submitInput = form.getInputByName("submit");
        HtmlPage listPage = submitInput.click();
        assertTrue("Listpage = " + listPage.asText() , listPage.asText().contains("Alle Turniere"));
        System.out.println("listPage = " + listPage.asText());
        assertTrue("Must contains HtmlUnit Open 2016" , listPage.asText().contains("HtmlUnit Open 2016"));

    }

}
