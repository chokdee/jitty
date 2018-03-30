/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.xml.clicktt.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

/**
 * Created by J. Melzer
 */
@Component
public class XMLImporter {
    static final Logger LOG = LoggerFactory.getLogger(XMLImporter.class);

    public Tournament parseClickTTPlayerExport(InputStream inputStream) {
        try {
            if (inputStream == null) {
                throw new IllegalArgumentException();
            }

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new StreamSource(getClass().getResourceAsStream("/androcup-export.xsd")));

            JAXBContext jaxbContext = JAXBContext.newInstance(Tournament.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(inputStream));

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(new MyValidationEventHandler());
            return (Tournament) unmarshaller.unmarshal(xsr);
        } catch (JAXBException | XMLStreamException | SAXException e) {
            LOG.error("", e);
            return null;
        }
    }

    private class MyValidationEventHandler implements ValidationEventHandler {

        public boolean handleEvent(ValidationEvent event) {
            String msg = ("\nEVENT");
            msg += "\nSEVERITY:  " + event.getSeverity();
            msg += "\nMESSAGE:  " + event.getMessage();
            msg += "\nLINKED EXCEPTION:  " + event.getLinkedException();
            msg += "\n    LINE NUMBER:  " + event.getLocator().getLineNumber();
            msg += "\n    COLUMN NUMBER:  " + event.getLocator().getColumnNumber();
            msg += "\n    OFFSET:  " + event.getLocator().getOffset();
            msg += "\n    OBJECT:  " + event.getLocator().getObject();
            msg += "\n    NODE:  " + event.getLocator().getNode();
            msg += "\n    URL:  " + event.getLocator().getURL();
            LOG.error(msg);
            return true;
        }

    }
}
