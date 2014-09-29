package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.CustomerDTO;

public class XMLHandlerCustomer extends DefaultHandler {

	public List<CustomerDTO> mListCustomer = null;

	public CustomerDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;
	
	private boolean isIdAdded=false;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerCustomer(String language) {
		super();
		mCurrentLanguage=language;
		mListCustomer = new ArrayList<CustomerDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("customer")) {
			data = new CustomerDTO();
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		elementOn = false;
		
		if (localName.equalsIgnoreCase("id") && !isIdAdded) {
			data.id = elementValue;
			isIdAdded=true;
			return;
		}		
		if (localName.equalsIgnoreCase("email")) {
			data.email=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("lastname")) {
			data.lastname=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("firstname")) {
			data.firstname=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_gender")) {
			data.id_gender=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("birthday")) {
			data.birthday=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("active")) {
			data.active=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("date_add")) {
			data.date_add=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("date_upd")) {
			data.date_upd=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("customer") && data.id!=null && data.id.trim().length()>0)
		{
			mListCustomer.add(data);
			isIdAdded=true;
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
	private double convertStringToFloat(String value){
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
}
