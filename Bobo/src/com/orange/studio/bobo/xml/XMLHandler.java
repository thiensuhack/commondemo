package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.os.Debug;
import android.util.Log;

import com.orange.studio.bobo.objects.ProductDTO;
 
public class XMLHandler extends DefaultHandler {
 
    
    public List<ProductDTO> mListProducts=null;
    
    public ProductDTO data = null;
    
    private String elementValue = null;
    private Boolean elementOn = false;
    
    public XMLHandler(){
    	super();
    	mListProducts=new ArrayList<ProductDTO>();
    }
 
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        elementOn = true;
        
        if (localName.equals("product"))
        {        	
            data = new ProductDTO();
        } else if (localName.equals("language")) {
        	Log.i("language", elementValue);
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
 
        elementOn = false;

        if (localName.equalsIgnoreCase("id"))
            data.id=elementValue;
        else if (localName.equalsIgnoreCase("name"))
            data.name=elementValue;
        else if (localName.equalsIgnoreCase("quantity"))
            data.quantity=elementValue;
        else if (localName.equalsIgnoreCase("id_manufacturer"))
            data.id_manufacturer=elementValue;
        else if (localName.equalsIgnoreCase("price"))
            data.price=elementValue;
        else if (localName.equalsIgnoreCase("wholesale_price"))
            data.wholesale_price=elementValue;
        else if (localName.equalsIgnoreCase("product"))
        	mListProducts.add(data);
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
