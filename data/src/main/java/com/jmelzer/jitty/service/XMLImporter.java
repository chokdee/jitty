/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.xml.playerimport.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

/**
 * Created by J. Melzer
 */
@Component
public class XMLImporter {
    static final Logger LOG = LoggerFactory.getLogger(XMLImporter.class);

    Tournament parseClickTTPlayerExport(InputStream inputStream) {
        try {
            if (inputStream == null) throw new IllegalArgumentException();

            JAXBContext jaxbContext = JAXBContext.newInstance(Tournament.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(inputStream));

            return (Tournament) jaxbContext.createUnmarshaller().unmarshal(xsr);
        } catch (JAXBException | XMLStreamException e) {
            LOG.error("", e);
            return null;
        }
    }
}
