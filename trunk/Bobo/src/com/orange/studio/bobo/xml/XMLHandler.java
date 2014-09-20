package com.orange.studio.bobo.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class XMLHandler extends DefaultHandler {
 
    String elementValue = null;
    Boolean elementOn = false;
    public static XMLGettersSetters data = null;
 
    public static XMLGettersSetters getXMLData() {
        return data;
    }
 
    public static void setXMLData(XMLGettersSetters data) {
        XMLHandler.data = data;
    }
 
    /** 
     * This will be called when the tags of the XML starts.
     **/
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        elementOn = true;
 
        if (localName.equals("CATALOG"))
        {
            data = new XMLGettersSetters();
        } else if (localName.equals("CD")) {
        	
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
 
        elementOn = false;
 
        if (localName.equalsIgnoreCase("title"))
            data.title=elementValue;
        else if (localName.equalsIgnoreCase("artist"))
            data.artist=elementValue;
        else if (localName.equalsIgnoreCase("country"))
            data.country=elementValue;
        else if (localName.equalsIgnoreCase("company"))
            data.comp=elementValue;
        else if (localName.equalsIgnoreCase("price"))
            data.price=elementValue;
        else if (localName.equalsIgnoreCase("year"))
            data.year=elementValue;
    }
 
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (elementOn) {
            elementValue = new String(ch, start, length);
            elementOn = false;
        }
 
    }
 
}
