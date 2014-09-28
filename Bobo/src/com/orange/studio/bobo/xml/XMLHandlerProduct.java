package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.ProductImageDTO;

public class XMLHandlerProduct extends DefaultHandler {

	public List<ProductDTO> mListProducts = null;

	public ProductDTO data = null;
	public ProductImageDTO imagePro=null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	private boolean isProNameTag = false;
	private boolean isDescription=false;
	private boolean isShortDescription=false;
	private boolean isListImages=false;
	private boolean isProId=false;
	
	private String attrId="";
	
	
	private String mCurrentLanguage="1";
	public XMLHandlerProduct(String language) {
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
		if (localName.equals("description_short")) {
			isShortDescription = true;
			return;
		}
		if(localName.equals("language")){
			attrId=attributes.getValue("id");
			return;
		}
		if(localName.equals("id_default_image")){
			data.id_default_image=attributes.getValue("xlink:href")+"?ws_key="+OrangeConfig.App_Key;
			return;
		}
		if(localName.equals("images")){
			isListImages=true;
			return;
		}
		if(localName.equalsIgnoreCase("image") && isListImages){
			imagePro=new ProductImageDTO();
			imagePro.url=attributes.getValue("xlink:href")+"?ws_key="+OrangeConfig.App_Key;			
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		elementOn = false;
		
		if (localName.equalsIgnoreCase("id") && !isProId) {
			data.id = elementValue;
			isProId=true;
			return;
		}		
		//parser image list
		if(localName.equalsIgnoreCase("images")){
			isListImages=false;
			return;
		}
		if(localName.equalsIgnoreCase("image") && isListImages){	
			imagePro.id=elementValue;
			data.associations.images.add(imagePro);
			return;
		}
		//end parser image list
		
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
				if(isShortDescription){
					data.description_short=elementValue;
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
		if (localName.equalsIgnoreCase("description_short")) {
			isShortDescription = false;
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
			data.price = convertStringToFloat(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("wholesale_price")) {
			data.wholesale_price = convertStringToFloat(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("unit_price_ratio")) {
			data.unit_price_ratio = convertStringToFloat(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("product"))
		{
			mListProducts.add(data);
			isProId=false;
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