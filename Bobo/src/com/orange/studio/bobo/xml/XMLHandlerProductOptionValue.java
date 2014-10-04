package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.ProductOptionValueDTO;

public class XMLHandlerProductOptionValue extends DefaultHandler {

	public List<ProductOptionValueDTO> mListProductOptionValue = null;

	public ProductOptionValueDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;
	
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerProductOptionValue(String language) {
		super();
		mCurrentLanguage=language;
		mListProductOptionValue = new ArrayList<ProductOptionValueDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("product_option_value")) {
			data = new ProductOptionValueDTO();
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
		if (localName.equalsIgnoreCase("id_attribute_group")) {
			data.id_attribute_group=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("name")) {
			//data.name=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("position")) {
			data.position=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("color")) {
			data.color=elementValue;
			return;
		}
		if(localName.equalsIgnoreCase("language")&& attrId.equals(mCurrentLanguage)){
			data.name=elementValue;
		}
		if (localName.equalsIgnoreCase("product_option_value") && data.id!=null && data.id.trim().length()>0)
		{
			mListProductOptionValue.add(data);
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
