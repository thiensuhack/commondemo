package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.CountryDTO;

public class XMLHandlerCountry extends DefaultHandler {

	public List<CountryDTO> mListCountry = null;

	public CountryDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	private boolean isCateNameTag = false;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerCountry(String language) {
		super();
		mCurrentLanguage=language;
		mListCountry = new ArrayList<CountryDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("country")) {
			data = new CountryDTO();
			return;
		}
		if (localName.equals("name")) {
			isCateNameTag = true;
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
			data.id = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_zone")) {
			data.id_zone = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_currency")) {
			data.id_currency = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("call_prefix")) {
			data.call_prefix = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("iso_code")) {
			data.iso_code = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("active")) {
			data.active = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("contains_states")) {
			data.contains_states = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("need_identification_number")) {
			data.need_identification_number = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("need_zip_code")) {
			data.need_zip_code = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("zip_code_format")) {
			data.zip_code_format = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("display_tax_label")) {
			data.display_tax_label = elementValue;
			return;
		}	
		if(localName.equalsIgnoreCase("language")){
			if(attrId.equals(mCurrentLanguage)){
				if(isCateNameTag){
					data.name = elementValue;					
				}
				return;
			}else{
				attrId="";
			}
			return;
		}
		if (localName.equalsIgnoreCase("name")) {
			isCateNameTag = false;
			return;
		}		
		if (localName.equalsIgnoreCase("country") && data.name!=null && data.name.trim().length()>0 && data.id!=null && data.id.trim().length()>0)
		{
			mListCountry.add(data);
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
