package com.jmelzer.jitty;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 20.08.2016.
 * some nice methods
 */
public class Util {
    private static WebClient webClient;

    public static WebClient webClient() {
        if (webClient == null) {
            webClient = new WebClient();
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.waitForBackgroundJavaScript(10000);
        }
        return webClient;
    }

    public static HtmlPage doLogin() throws java.io.IOException {
        HtmlPage pageLogin = webClient().getPage("http://localhost:8080/#/login");
        HtmlForm form;
        HtmlButton button;
        HtmlTextInput textField;
        form = pageLogin.getFormByName("loginform");

        button = form.getButtonByName("submit");
        textField = form.getInputByName("username");
        textField.setValueAttribute("user");
        HtmlPasswordInput pwField = form.getInputByName("password");
        pwField.setValueAttribute("42");

        pageLogin = button.click();
        assertTrue(pageLogin.asText().contains("Willkommen bei Jitty"));
        assertFalse(pageLogin.asText().contains("Bitte zunächst einloggen"));

        return webClient().getPage("http://localhost:8080/#/");
//        return pageLogin;
    }

    public static String randomString(int len) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

}
