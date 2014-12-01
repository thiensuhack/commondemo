package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.TaxDTO;

public class XMLHandlerTax extends DefaultHandler {

	public List<TaxDTO> mListTaxes = null;

	public TaxDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

//	private boolean isCateNameTag = false;
	
//	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	private String attrId="";
	private boolean isNameTag = false;
	
	public XMLHandlerTax(String language) {
		super();
		mCurrentLanguage=language;
		mListTaxes = new ArrayList<TaxDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("tax")) {
			data = new TaxDTO();
			return;
		}
		if (localName.equals("name")) {
			isNameTag = true;
			return;
		}
		if(localName.equals("language")){
			attrId=attributes.getValue("id");
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		elementOn = false;
		
		if (localName.equalsIgnoreCase("id")) {
			data.id = convertStringToInt(elementValue);
			return;
		}
		if(localName.equalsIgnoreCase("language")){
			if(attrId.equals(mCurrentLanguage)){
				if(isNameTag){
					data.name = elementValue;
					return;
				}
			}
		}
		if (localName.equalsIgnoreCase("name")) {
			isNameTag=false;
			attrId="";
			return;
		}
		if (localName.equalsIgnoreCase("rate")) {
			data.rate = convertStringToDouble(elementValue);
			return;
		}	
		
		if (localName.equalsIgnoreCase("active")) {
			data.active = convertStringToInt(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("deleted")) {
			data.deleted = convertStringToInt(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("tax"))
		{
			mListTaxes.add(0,data);
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
	private double convertStringToDouble(String value){
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return 0;
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
