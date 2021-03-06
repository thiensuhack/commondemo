package com.orange.studio.bobo.objects;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
	public String id;
	public String id_address_delivery;
	public String id_address_invoice;
	public String id_cart;
	public String id_currency;
	public String id_customer;
	public String id_carrier;
	public String payment;
	public double total_paid;
	public double total_paid_tax_incl;
	public double total_paid_tax_excl;
	public double total_paid_real;
	public int  total_products;
	public int total_products_wt;
	public double total_shipping;
	public double total_shipping_tax_incl;
	public double total_shipping_tax_excl;
	public double carrier_tax_rate;
	public double total_wrapping;
	public double total_wrapping_tax_incl;
	public double total_wrapping_tax_excl;
	public int conversion_rate;
	public List<OrderRowDTO> mListProduct;
	public OrderDTO(){
		mListProduct=new ArrayList<OrderRowDTO>();
	}
}
