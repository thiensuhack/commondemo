package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.ProductDTO;

public class XMLHandler extends DefaultHandler {

	public List<ProductDTO> mListProducts = null;

	public ProductDTO data = null;

	private String elementValue = null;
	private boolean elementOn = false;

	private boolean isProNameTag = false;
	private boolean isDescription=false;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandler(String language) {
		super();
		mCurrentLanguage=language;
		mListProducts = new ArrayList<ProductDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;

		if (localName.equals("product")) {
			data = new ProductDTO();
			return;
		}
		if (localName.equals("name")) {
			isProNameTag = true;
			return;
		}
		if (localName.equals("description")) {
			isDescription = true;
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
		if(localName.equalsIgnoreCase("language")){
			if(attrId.equals(mCurrentLanguage)){
				if(isProNameTag){
					data.name = elementValue;
					return;
				}
				if(isDescription){
					data.description=elementValue;
					return;
				}
			}else{
				attrId="";
			}
		}
		if (localName.equalsIgnoreCase("name")) {
			isProNameTag = false;
			return;
		}
		if (localName.equalsIgnoreCase("description")) {
			isDescription = false;
			return;
		}
		if (localName.equalsIgnoreCase("quantity")) {
			data.quantity = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_manufacturer")) {
			data.id_manufacturer = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("price")) {
			data.price = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("wholesale_price")) {
			data.wholesale_price = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("product"))
		{
			mListProducts.add(data);
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

}
