package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.ProductFeatureValueDTO;

public class XMLHandlerProductFeatureValues extends DefaultHandler {

	public List<ProductFeatureValueDTO> mListCategory = null;

	public ProductFeatureValueDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	private boolean isValueTag = false;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerProductFeatureValues(String language) {
		super();
		mCurrentLanguage=language;
		mListCategory = new ArrayList<ProductFeatureValueDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("product_feature_value")) {
			data = new ProductFeatureValueDTO();
			return;
		}
		if (localName.equals("value")) {
			isValueTag = true;
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
				if(isValueTag){
					data.value = elementValue;					
				}
				return;
			}else{
				attrId="";
			}
			return;
		}
		if (localName.equalsIgnoreCase("id_feature")) {
			data.id_feature=elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("custom")) {
			data.custom=elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("value")) {
			isValueTag = false;
			return;
		}		
		if (localName.equalsIgnoreCase("product_feature_value") && data.value!=null && data.value.trim().length()>0)
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
