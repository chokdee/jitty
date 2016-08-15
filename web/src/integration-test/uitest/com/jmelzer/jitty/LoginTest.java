package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 14.08.2016.
 */
public class LoginTest {
    @Test
    public void homePage() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(10000);

        final HtmlPage page = webClient.getPage("http://localhost:8080");
        Assert.assertEquals("Jitty", page.getTitleText());

        String pageAsText = page.asText();
        assertTrue(pageAsText.contains("Bitte zunächst einloggen"));

        HtmlPage pageLogin = webClient.getPage("http://localhost:8080/#/login");
        HtmlForm form = pageLogin.getFormByName("loginform");

        HtmlButton button = form.getButtonByName("submit");
        HtmlTextInput textField = form.getInputByName("username");

        // Change the value of the text field
        textField.setValueAttribute("root");

        // Now submit the form by clicking the button and get back the second page.
        pageLogin = button.click();
        assertTrue(pageLogin.asText().contains("Anmeldung ist fehlgeschlagen"));

        form = pageLogin.getFormByName("loginform");

        button = form.getButtonByName("submit");
        textField = form.getInputByName("username");
        textField.setValueAttribute("user");
        HtmlPasswordInput pwField = form.getInputByName("password");
        pwField.setValueAttribute("42");

        pageLogin = button.click();
        assertTrue(pageLogin.asText().contains("Willkommen bei Jitty"));
//        System.out.println("page2.asText() = " + pageLogin.asText());
    }
}
