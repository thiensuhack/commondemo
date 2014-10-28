package com.orange.studio.bobo.objects;

import java.util.ArrayList;
import java.util.List;

public class ItemCartDTO {
	public String id;
	public String id_customer;
	public String id_address_delivery;
	public String id_guest;
	public String gift;
	public String gift_message;
	public String delivery_option;
	public String secure_key;
	public String date_add;
	public String date_upd;
//	public String id_product;
//	public int quantity;
	public List<CartRowDTO> mListCartRow;
	public ItemCartDTO(){
		mListCartRow=new ArrayList<CartRowDTO>();
	}
}
