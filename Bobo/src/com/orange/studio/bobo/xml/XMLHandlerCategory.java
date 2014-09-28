package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.MenuItemDTO;

public class XMLHandlerCategory extends DefaultHandler {

	public List<MenuItemDTO> mListCategory = null;

	public MenuItemDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	private boolean isCateNameTag = false;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerCategory(String language) {
		super();
		mCurrentLanguage=language;
		mListCategory = new ArrayList<MenuItemDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("category")) {
			data = new MenuItemDTO();
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
		if (localName.equalsIgnoreCase("category") && data.name!=null && data.name.trim().length()>0 && !data.id.equals("1") && !data.id.equals("2"))
		{
			mListCategory.add(0,data);
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
