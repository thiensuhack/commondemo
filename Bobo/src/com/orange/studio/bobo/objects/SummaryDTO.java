package com.orange.studio.bobo.objects;

public class SummaryDTO {
	public DeliveryDTO delivery;
	public double total_discounts;
	public double total_shipping;
	public double total_products;
	public double total_products_wt;
	public double total_price;
	public double total_tax;
	public double total_price_without_tax;
	public SummaryDTO(){
		delivery=new DeliveryDTO();
	}
}
