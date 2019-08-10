package com.home.airlinescanner.tikaairlinescanner;

import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The airport table columns handler to parse table data.
 */
public class AirlineTableColHandler extends ToXMLContentHandler {
    boolean col = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("td")) {
            col = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    }
}
