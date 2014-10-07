package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.ProductFeatureDTO;

public class XMLHandlerProductFeatures extends DefaultHandler {

	public List<ProductFeatureDTO> mListProductFeatures = null;

	public ProductFeatureDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	private boolean isNameTag = false;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerProductFeatures(String language) {
		super();
		mCurrentLanguage=language;
		mListProductFeatures = new ArrayList<ProductFeatureDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("product_feature")) {
			data = new ProductFeatureDTO();
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
			data.id = elementValue;
			return;
		}		
		
		if(localName.equalsIgnoreCase("language")){
			if(attrId.equals(mCurrentLanguage)){
				if(isNameTag){
					data.name = elementValue;					
				}
				return;
			}else{
				attrId="";
			}
			return;
		}
		if (localName.equalsIgnoreCase("name")) {
			isNameTag = false;
			return;
		}		
		if (localName.equalsIgnoreCase("position")) {
			data.position=elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("product_feature") && data.name!=null && data.name.trim().length()>0)
		{
			mListProductFeatures.add(0,data);
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
