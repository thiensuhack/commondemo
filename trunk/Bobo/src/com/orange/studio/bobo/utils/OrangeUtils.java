package com.orange.studio.bobo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.orange.studio.bobo.OrangeApplicationContext;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.REQUEST_PARAMS_NAME;
import com.orange.studio.bobo.objects.AddressDTO;
import com.orange.studio.bobo.objects.CarrierDTO;
import com.orange.studio.bobo.objects.CustomerDTO;
import com.orange.studio.bobo.objects.ItemCartDTO;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.SummaryDTO;
import com.zuzu.db.store.SQLiteStore;
import com.zuzu.db.store.SimpleStoreIF;

public class OrangeUtils {
	public static ProductDTO duplicateProduct(ProductDTO product){
		try {
			ProductDTO result = product;
			return result;			
		} catch (Exception e) {
		}
		return null;
	}
	public static String createStringOrder(ItemCartDTO cart,CustomerDTO customer,AddressDTO address,CarrierDTO carrier,SummaryDTO summary){
		String result=null;
		try {
			result="<?xml version=\"1.0\" encoding=\"UTF-8\"?><prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\"><order>";
			result+="<id_address_delivery required=\"true\" format=\"isUnsignedId\">"+address.id+"</id_address_delivery>";
			result+="<id_address_invoice required=\"true\" format=\"isUnsignedId\">9</id_address_invoice>";
			result+="<id_cart required=\"true\" format=\"isUnsignedId\">"+cart.id+"</id_cart>";
			result+="<id_currency required=\"true\" format=\"isUnsignedId\">1</id_currency>";
			result+="<id_lang required=\"true\" format=\"isUnsignedId\">2</id_lang>";
			result+="<id_customer required=\"true\" format=\"isUnsignedId\">"+customer.id+"</id_customer>";
			result+="<id_carrier required=\"true\" format=\"isUnsignedId\">"+carrier.id+"</id_carrier>";
			result+="<current_state format=\"isUnsignedId\"></current_state>";
			result+="<module required=\"true\" format=\"isModuleName\">bankwire</module>";
			result+="<invoice_number></invoice_number>";
			result+="<invoice_date></invoice_date>";
			result+="<delivery_number></delivery_number>";
			result+="<delivery_date></delivery_date>";
			result+="<valid></valid>";
			result+="<date_add format=\"isDate\"></date_add>";
			result+="<date_upd format=\"isDate\"></date_upd>";
			result+="<id_shop_group format=\"isUnsignedId\"></id_shop_group>";
			result+="<id_shop format=\"isUnsignedId\"></id_shop>";
			result+="<secure_key format=\"isMd5\"></secure_key>";
			result+="<payment required=\"true\" format=\"isGenericName\">Bank wire</payment>";
			result+="<recyclable format=\"isBool\"></recyclable>";
			result+="<gift format=\"isBool\"></gift>";
			result+="<gift_message format=\"isMessage\"></gift_message>";
			result+="<mobile_theme format=\"isBool\"></mobile_theme>";
			result+="<total_discounts format=\"isPrice\"></total_discounts>";
			result+="<total_discounts_tax_incl format=\"isPrice\"></total_discounts_tax_incl>";
			result+="<total_discounts_tax_excl format=\"isPrice\"></total_discounts_tax_excl>";
			result+="<total_paid required=\"true\" format=\"isPrice\">"+summary!=null?summary.total_products:0+"</total_paid>";
			result+="<total_paid_tax_incl format=\"isPrice\"></total_paid_tax_incl>";
			result+="<total_paid_tax_excl format=\"isPrice\"></total_paid_tax_excl>";
			result+="<total_paid_real required=\"true\" format=\"isPrice\">"+summary!=null?summary.total_price:0+"</total_paid_real>";
			result+="<total_products required=\"true\" format=\"isPrice\">"+summary!=null?summary.total_products:0+"</total_products>";
			result+="<total_products_wt required=\"true\" format=\"isPrice\">"+summary!=null?summary.total_products_wt:0+"</total_products_wt>";
			result+="<total_shipping format=\"isPrice\"></total_shipping>";
			result+="<total_shipping_tax_incl format=\"isPrice\"></total_shipping_tax_incl>";
			result+="<total_shipping_tax_excl format=\"isPrice\"></total_shipping_tax_excl>";
			result+="<carrier_tax_rate format=\"isFloat\"></carrier_tax_rate>";
			result+="<total_wrapping format=\"isPrice\"></total_wrapping>";
			result+="<total_wrapping_tax_incl format=\"isPrice\"></total_wrapping_tax_incl>";
			result+="<total_wrapping_tax_excl format=\"isPrice\"></total_wrapping_tax_excl>";
			result+="<shipping_number format=\"isTrackingNumber\"></shipping_number>";
			result+="<conversion_rate required=\"true\" format=\"isFloat\">1</conversion_rate>";
			result+="<reference></reference></order></prestashop>";
		} catch (Exception e) {
		}
		return result;
	}
	public static double convertStringToFloat(String value){
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
	public static String createCartData(List<ProductDTO> mListProducts,CustomerDTO customer,String cartID){
		String result="";
		try {						
			result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?><prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
			result+="<cart>";
			if(cartID!=null && cartID.trim().length()>0){
				result+="<id>"+cartID+"</id>";
			}
			result+="<id_currency>1</id_currency>";
			if(customer!=null){
				result+="<id_customer>"+customer.id+"</id_customer>";	
			}else{
				result+="<id_customer></id_customer>";
			}			
			result+="<id_guest></id_guest>";
			result+="<id_lang>"+OrangeConfig.LANGUAGE_DEFAULT+"</id_lang>";
			result+="<id_shop_group>"+""+"</id_shop_group>";
			result+="<id_shop>"+""+"</id_shop>";
			result+="<associations><cart_rows>";
			for (ProductDTO item : mListProducts) {
				result+="<cart_row>";
				result+="<id_product>"+item.id+"</id_product>";
				result+="<id_product_attribute>"+item.stock.id_product_attribute+"</id_product_attribute>";
				result+="<id_address_delivery>0</id_address_delivery></cart_row>";
				result+="<quantity>"+item.cartCounter+"</quantity>";
			}			
			result+="</cart_rows></associations>";
			result+="</cart></prestashop>";
		} catch (Exception e) {
			
		}		
		return result;
	}
	public static String createCartData(ProductDTO product){
		String result="";
		try {
			result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?><prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
			result+="<cart><id_currency>1</id_currency>";
			result+="<id_customer></id_customer>";
			result+="<id_guest></id_guest>";
			result+="<id_lang>"+OrangeConfig.LANGUAGE_DEFAULT+"</id_lang>";
			result+="<id_shop_group>"+product.stock.id_shop_group+"</id_shop_group>";
			result+="<id_shop>"+product.stock.id_shop+"</id_shop>";
			result+="<associations><cart_rows><cart_rows>";
			result+="<id_product>"+product.stock.id+"</id_product>";
			result+="<id_product_attribute>"+product.stock.id_product_attribute+"</id_product_attribute>";
			result+="<id_address_delivery>0</id_address_delivery>";
			result+="</cart_rows></cart_rows></associations>";
			result+="</cart></prestashop>";
		} catch (Exception e) {
			
		}		
		return result;
	}
	public static String md5(String str) {
	    String result="";
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return result;
		}

		byte[] b = null;
		try {
			b = str.getBytes("UTF-8");
			md5.update(b);
		} catch (UnsupportedEncodingException e1) {
		}

		byte hash[] = md5.digest();
		if (hash.length > 0) {
			result = new String(Hex.encodeHex(hash));
		}
		return result;
	}
	public static float convertDpToPixel(float dp){
	    Resources resources = OrangeApplicationContext.getContext().getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	public static boolean validateEmail(String email) {
		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}
	public static SimpleStoreIF getStoreAdapter(String name, Context mContext,
			int items) {
		return SQLiteStore.getInstance(name, mContext, OrangeConfig.DBVERSION,
				items);
	}
	public static Bundle createRequestBundle(String limit,String displayFields) {		
		Bundle result = new Bundle();
		result.putString(REQUEST_PARAMS_NAME.WS_KEY, OrangeConfig.App_Key);
		if(displayFields!=null){
			result.putString(REQUEST_PARAMS_NAME.DISPLAY, displayFields);
		}else{
			result.putString(REQUEST_PARAMS_NAME.DISPLAY, "full");
		}		
		result.putString(REQUEST_PARAMS_NAME.SORT, "id_DESC");
		if(limit!=null){
			result.putString(REQUEST_PARAMS_NAME.LITMIT, limit);
		}		
		return result;
	}
	public static Bundle createRequestBundle2(String limit,String displayFields) {		
		Bundle result = new Bundle();
		result.putString(REQUEST_PARAMS_NAME.WS_KEY, OrangeConfig.App_Key);
		if(displayFields!=null){
			result.putString(REQUEST_PARAMS_NAME.DISPLAY, displayFields);
		}	
		result.putString(REQUEST_PARAMS_NAME.SORT, "id_DESC");
		if(limit!=null){
			result.putString(REQUEST_PARAMS_NAME.LITMIT, limit);
		}		
		return result;
	}
	public static String createUrl(Bundle mBundle){
		if(mBundle==null){
			return "";
		}
		String result="?";
		for (String key : mBundle.keySet()) {
			result+= key+"="+mBundle.getString(key)+"&";
		}
		return result;
	}
}
