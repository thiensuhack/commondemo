package com.orange.studio.bobo.objects;

public class ProductDTO {
	public String id;
	public String id_manufacturer;
	public String id_supplier;
	public String id_category_default;
	public String id_default_image;
	public String id_tax_rules_group;
	public String quantity;
	public String id_shop_default;
	public String width;
	public String height;
	public String depth;
	public String weight;
	public String quantity_discount;
	public String on_sale;
	public String online_only;
	public String ecotax;
	public String minimal_quantity;
	public double price;
	public double wholesale_price;
	public double unit_price_ratio;
	public String additional_shipping_cost;
	public String text_fields;
	public String active;
	public String available_for_order;
	public String available_date;
	public String condition;
	public String show_price;
	public String indexed;
	public String visibility;
	public String date_add;
	public String date_upd;
	public String name;
	public String description;
	public String description_short;
	public String available_now;
	public String available_later;
	public String reference;
	public AssociationDTO associations;
	

	public int cartCounter;
	
	public ProductDTO(){
		cartCounter=0;
		associations=new AssociationDTO();
		condition="";
		reference="";
	}
}
