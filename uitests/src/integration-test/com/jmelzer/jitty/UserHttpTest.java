/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Ignore;
import org.junit.Test;

import static com.jmelzer.jitty.Util.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 20.08.2016.
 * integration test for user views
 */
@Ignore
public class UserHttpTest {

    @Test
    public void testList() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/users");
        assertTrue(page.asText().contains("Rossi"));
    }
    @Test
    public void newUser() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/users");
        HtmlAnchor anchor = page.getAnchorByName("newUser");
        assertNotNull(anchor);
        HtmlPage newUserPage = anchor.click();
        assertTrue(newUserPage.asText().contains("Benutzerinformationen eingeben"));

        HtmlForm form = newUserPage.getFormByName("userForm");

        HtmlInput submitInput = form.getInputByName("submit");
        newUserPage  = submitInput.click();
        //validation triggered
        assertTrue(newUserPage.asText().contains("Bitte einen Login-Namen eingeben"));

        //fill out form
        String name = randomString(10);
        form.getInputByName("loginName").setValueAttribute(randomString(10));
        form.getInputByName("name").setValueAttribute(name);
        form.getInputByName("input_email_address").setValueAttribute(randomString(10)+ "@email.com");
        form.getInputByName("input_password").setValueAttribute("123456");
        form.getInputByName("input_password_confirm").setValueAttribute("123456");

        HtmlPage listPage = submitInput.click();
        assertTrue("Must contains " +  name, listPage.asText().contains(name));
    }

    @Test
    public void editUser() throws Exception {
        doLogin();

        HtmlPage page = webClient().getPage("http://localhost:8080/#/users");
        HtmlAnchor anchor = page.getAnchorByHref("#/users/7");
        assertNotNull(anchor);
        HtmlPage newUserPage = anchor.click();
        assertTrue(newUserPage.asText().contains("Benutzerinformationen eingeben"));
        assertTrue(newUserPage.asText().contains("Valentino Rossi"));

        HtmlForm form = newUserPage.getFormByName("userForm");
        form.getInputByName("input_email_address").setValueAttribute("changed@email.com");
        HtmlInput submitInput = form.getInputByName("submit");
        HtmlPage listPage = submitInput.click();

        assertTrue("Must contains changed@email.com" , listPage.asText().contains("changed@email.com"));

    }

    @Test
    public void changePassword() {
        //todo , but how?
    }
}
