/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.xml.clicktt.Tournament;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by J. Melzer on 15.03.2017.
 */
public class XMLImporterTest {
    @Test
    public void unmarshal() throws Exception {
        try {
            new XMLImporter().parseClickTTPlayerExport(null);
            fail("is is null");
        } catch (IllegalArgumentException e) {
            //ok
        }
        InputStream is = getClass().getResourceAsStream("/xml-import/Turnierteilnehmer.xml");
        Tournament tournament = new XMLImporter().parseClickTTPlayerExport(is);
        assertNotNull(tournament);

    }

}