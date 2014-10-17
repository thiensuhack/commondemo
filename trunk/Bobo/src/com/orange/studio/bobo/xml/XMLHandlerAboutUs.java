package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.AboutUsDTO;

public class XMLHandlerAboutUs extends DefaultHandler {

	public List<AboutUsDTO> mListAboutUs = null;

	public AboutUsDTO data = null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	private boolean isContent = false;
	private boolean isMetaDescription=false;
	private boolean isMetaTitle=false;
	private int contentCounter=0;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerAboutUs(String language) {
		super();
		mCurrentLanguage=language;
		mListAboutUs = new ArrayList<AboutUsDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("content") && contentCounter==0) {
			data = new AboutUsDTO();
			contentCounter=1;
			return;
		}
		if (localName.equals("meta_description")) {
			isMetaDescription = true;
			return;
		}
		if (localName.equals("meta_title")) {
			isMetaTitle = true;
			return;
		}
		if (localName.equals("content") && contentCounter==1) {
			isContent = true;
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
				if(isContent){
					data.content = elementValue;
					isContent=false;
				}else{
					if(isMetaDescription){
						data.meta_description=elementValue;
						isMetaDescription=false;
					}else{
						if(isMetaTitle){
							data.meta_title=elementValue;
							isMetaTitle=false;
						}
					}
				}
				return;
			}else{
				attrId="";
			}
			return;
		}
		if (localName.equalsIgnoreCase("content")&&contentCounter==1) {
			contentCounter=2;
			return;
		}		
		if (localName.equalsIgnoreCase("content") && contentCounter==2)
		{
			mListAboutUs.add(0,data);
			contentCounter=0;
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
