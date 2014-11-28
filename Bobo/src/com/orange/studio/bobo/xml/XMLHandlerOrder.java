package com.orange.studio.bobo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.orange.studio.bobo.objects.OrderDTO;
import com.orange.studio.bobo.objects.OrderRowDTO;

public class XMLHandlerOrder extends DefaultHandler {

	public List<OrderDTO> mListOrder = null;

	public OrderDTO data = null;
	public OrderRowDTO mOrderRow=null;
	
	private String elementValue = null;
	private boolean elementOn = false;
	private boolean isOrder=false;
	private boolean isOrderRow=false;
	
	private String mCurrentLanguage="1";
	public XMLHandlerOrder(String language) {
		super();
		mCurrentLanguage=language;
		mListOrder = new ArrayList<OrderDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementOn = true;
		if (localName.equals("order")) {
			mListOrder=new ArrayList<OrderDTO>();
			data = new OrderDTO();
			isOrder=true;
			return;
		}
		if (localName.equals("order_row")) {
			mOrderRow=new OrderRowDTO();
			isOrderRow=true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		elementOn = false;
		
		if (localName.equalsIgnoreCase("id")) {
			if(isOrder){
				data.id = elementValue;
				isOrder=false;
			}
			if(isOrderRow){
				mOrderRow.id=elementValue;
			}
			return;
		}	
		if (localName.equalsIgnoreCase("id_address_delivery")) {
			data.id_address_delivery = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_address_invoice")) {
			data.id_address_invoice = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_cart")) {
			data.id_cart = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_currency")) {
			data.id_currency = elementValue;
			return;
		}	
		if (localName.equalsIgnoreCase("id_customer")) {
			data.id_customer = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("id_carrier")) {
			data.id_carrier = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("payment")) {
			data.payment = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("total_paid")) {
			data.total_paid = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("total_paid_tax_incl")) {
			data.total_paid_tax_incl = convertStringToDouble(elementValue);
			return;
		}	
		if (localName.equalsIgnoreCase("total_paid_tax_excl")) {
			data.total_paid_tax_excl = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("total_paid_real")) {
			data.total_paid_real = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("total_products")) {
			data.total_products = convertStringToInt(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("total_products_wt")) {
			data.total_products_wt = convertStringToInt(elementValue);;
			return;
		}
		if (localName.equalsIgnoreCase("total_shipping")) {
			data.total_shipping = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("total_shipping_tax_incl")) {
			data.total_shipping_tax_incl = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("total_shipping_tax_excl")) {
			data.total_shipping_tax_excl = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("carrier_tax_rate")) {
			data.carrier_tax_rate = convertStringToDouble(elementValue);
			return;
		}	
		if (localName.equalsIgnoreCase("total_wrapping")) {
			data.total_wrapping = convertStringToDouble(elementValue);
			return;
		}	
		if (localName.equalsIgnoreCase("total_wrapping_tax_incl")) {
			data.total_wrapping_tax_incl = convertStringToDouble(elementValue);
			return;
		}	
		if (localName.equalsIgnoreCase("total_wrapping_tax_excl")) {
			data.total_wrapping_tax_excl = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("conversion_rate")) {
			data.conversion_rate = convertStringToInt(elementValue);
			return;
		}
		//OrderRow
		if (localName.equalsIgnoreCase("product_id")) {
			mOrderRow.product_id = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("product_attribute_id")) {
			mOrderRow.product_attribute_id = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("product_name")) {
			mOrderRow.product_name = elementValue;
			return;
		}
		if (localName.equalsIgnoreCase("product_reference")) {
			mOrderRow.product_reference = elementValue;
			return;
		}
		
		if (localName.equalsIgnoreCase("product_quantity")) {
			mOrderRow.product_quantity = convertStringToInt(elementValue);
			return;
		}
		
		if (localName.equalsIgnoreCase("product_price")) {
			mOrderRow.product_price = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("unit_price_tax_incl")) {
			mOrderRow.unit_price_tax_incl = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("unit_price_tax_excl")) {
			mOrderRow.unit_price_tax_excl = convertStringToDouble(elementValue);
			return;
		}
		if (localName.equalsIgnoreCase("order_row")) {
			data.mListProduct.add(mOrderRow);
			isOrderRow=false;
		}
		if (localName.equalsIgnoreCase("order") && data.id!=null && data.id.trim().length()>0)
		{
			mListOrder.add(data);
			isOrder=false;
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
	private double convertStringToDouble(String value){
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return -1;
		}
	}
	private int convertStringToInt(String value){
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return -1;
		}
	}
}
