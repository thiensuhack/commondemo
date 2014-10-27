package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.AddressDTO;

public class XMLHandlerAddress extends DefaultHandler {

	public List<AddressDTO> mListAddress = null;

	public AddressDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

//	private boolean isCateNameTag = false;
	
//	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerAddress(String language) {
		super();
		mCurrentLanguage=language;
		mListAddress = new ArrayList<AddressDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("address")) {
			data = new AddressDTO();
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		elementOn = false;
		
		if (localName.equalsIgnoreCase("id")) {
			data.id = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_customer")) {
			data.id_customer = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_manufacturer")) {
			data.id_manufacturer = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_supplier")) {
			data.id_supplier = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_warehouse")) {
			data.id_warehouse = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_country")) {
			data.id_country = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_state")) {
			data.id_state = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("alias")) {
			data.alias = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("lastname")) {
			data.lastname = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("firstname")) {
			data.firstname = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("address1")) {
			data.address1 = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("address2")) {
			data.address2 = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("postcode")) {
			data.postcode = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("city")) {
			data.city = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("phone")) {
			data.phone = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("phone_mobile")) {
			data.phone_mobile = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("date_add")) {
			data.date_add = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("date_upd")) {
			data.date_upd = elementValue;
			return;
		}		
		if (localName.equalsIgnoreCase("address") && data.id!=null && data.id.trim().length()>0)
		{
			mListAddress.add(0,data);
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
