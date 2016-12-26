package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Assert;
import org.junit.Test;

import static com.jmelzer.jitty.Util.webClient;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 14.08.2016.
 */
public class LoginHttpTest {

    @Test
    public void homePage() throws Exception {

        HtmlPage page = webClient().getPage("http://localhost:8080");
        Assert.assertEquals("Jitty - Login", page.getTitleText());

        HtmlPage pageLogin = webClient().getPage("http://localhost:8080/#/login");
        HtmlForm form = pageLogin.getFormByName("loginform");

        HtmlButton button = form.getButtonByName("submit");
        HtmlTextInput textField = form.getInputByName("username");

        // Change the value of the text field
        textField.setValueAttribute("root");

        // Now submit the form by clicking the button and get back the second page.
        pageLogin = button.click();
        assertTrue(pageLogin.asText().contains("Anmeldung ist fehlgeschlagen"));

        Util.doLogin();
    }


    @Test
    public void testLogout() throws Exception {
        HtmlPage page = Util.doLogin();

        HtmlAnchor anchor = page.getAnchorByName("logout");
        assertNotNull(anchor);
        HtmlPage pageLogout = anchor.click();
        assertTrue(pageLogout.asText().contains("Jitty - Login"));
    }
}
