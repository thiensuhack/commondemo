package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.CartRowDTO;
import com.orange.studio.bobo.objects.ItemCartDTO;

public class XMLHandlerItemCart extends DefaultHandler {

	public List<ItemCartDTO> mListItemCart = null;
	
	public ItemCartDTO data = null;
	private CartRowDTO mCartRowDTO=null;
	
	private String elementValue = null;
	private boolean elementOn = false;

	
	private String mCurrentLanguage="1";
	public XMLHandlerItemCart(String language) {
		super();
		mCurrentLanguage=language;
		mListItemCart = new ArrayList<ItemCartDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("cart")) {
			data = new ItemCartDTO();
			return;
		}
		if (localName.equals("cart_row")) {
			mCartRowDTO=new CartRowDTO();
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
		if (localName.equalsIgnoreCase("id_guest")) {
			data.id_guest = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_product")) {
			data.id_product = elementValue;
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
		if (localName.equalsIgnoreCase("delivery_option")) {
			data.delivery_option = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("gift")) {
			data.gift = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("gift_message")) {
			data.gift_message = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("secure_key")) {
			data.secure_key = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_product")) {
			mCartRowDTO.id_product = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_product_attribute")) {
			mCartRowDTO.id_product_attribute = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_address_delivery")) {
			mCartRowDTO.id_address_delivery = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("quantity")) {
			mCartRowDTO.quantity = convertStringToInt(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("cart_row")&& mCartRowDTO!=null && mCartRowDTO.id_product!=null && mCartRowDTO.id_product.trim().length()>0) {
			data.mListCartRow.add(mCartRowDTO);
			return;
		}
		if (localName.equalsIgnoreCase("cart"))
		{
			mListItemCart.add(0,data);
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
	private int convertStringToInt(String value){
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
}
