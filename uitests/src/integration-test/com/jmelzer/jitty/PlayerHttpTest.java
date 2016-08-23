package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static com.jmelzer.jitty.Util.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 20.08.2016.
 * integration test for tournament views
 */
public class PlayerHttpTest {

    @Test
    public void testList() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/players");
        assertTrue(page.asText().contains("Xin"));
        assertTrue(page.asText().contains("Alle Spieler"));
    }
    @Test
    public void newPlayer() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/players");
        String startPage = page.asXml();

        HtmlAnchor anchor = page.getAnchorByName("create");
        assertNotNull(anchor);
        HtmlPage editPage = anchor.click();
        assertTrue(editPage.asText().contains("Spieler eingeben"));

        HtmlForm form = editPage.getFormByName("playerForm");

        HtmlInput submitInput = form.getInputByName("submit");
        editPage  = submitInput.click();
        //validation triggered
        assertTrue(editPage.asText().contains("Bitte einen Vornamen eingeben"));

        form = editPage.getFormByName("playerForm");
        submitInput = form.getInputByName("submit");

        //fill out form
        String firstName = randomString(10);
        form.getInputByName("firstName").setValueAttribute(firstName);
        form.getInputByName("lastName").setValueAttribute(randomString(10));
        form.getInputByName("qttr").setValueAttribute("1500");
        ((HtmlInput)editPage.getElementById("birthday")).setValueAttribute("01.01.1990");
        editPage.getElementById("gender").click();
//        HtmlSelect select = (HtmlSelect) editPage.getElementById("associnput");
//        HtmlOption option = select.getOptionByValue("Badischer TTV");
//        select.setSelectedAttribute(option, true);
        //todo how to set value for ui-select?

        HtmlPage listPage = submitInput.click();
//        System.out.println("listPage = " + listPage.asText());
        assertTrue(listPage.asText().contains("Alle Spieler"));
        assertTrue("Must contains " +  firstName, listPage.asText().contains(firstName));

        //edit the new create player again
        String diff = StringUtils.difference(startPage, listPage.asXml());
        String nr = StringUtils.substringBetween(diff, "href=\"#/players/", "\"" );

        HtmlAnchor anchorEdit = listPage.getAnchorByName("edit"+nr);
        editPage = anchorEdit.click();
        assertTrue(editPage.asText().contains("C-Klasse"));

    }

}
