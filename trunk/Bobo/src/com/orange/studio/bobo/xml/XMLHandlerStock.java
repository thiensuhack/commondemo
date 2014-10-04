package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.StockDTO;

public class XMLHandlerStock extends DefaultHandler {

	public List<StockDTO> mListStock = null;

	public StockDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	
	private String mCurrentLanguage="1";
	public XMLHandlerStock(String language) {
		super();
		mCurrentLanguage=language;
		mListStock = new ArrayList<StockDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("stock_available")) {
			data = new StockDTO();
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		elementOn = false;
		
		if (localName.equalsIgnoreCase("quantity")) {
			data.quantity = convertStringToInt(elementValue);
			return;
		}		
		
		if (localName.equalsIgnoreCase("depends_on_stock")) {
			data.depends_on_stock = convertStringToInt(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("out_of_stock")) {
			data.out_of_stock = convertStringToInt(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("stock_available"))
		{
			mListStock.add(0,data);
			return;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (elementOn) {
			elementValue = new String(ch, start, length);
			elementOn = false;
		}
	}
	private int convertStringToInt(String value){
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
}
