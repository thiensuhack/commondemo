package com.orange.studio.bobo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.orange.studio.bobo.objects.ProductDTO;
import com.zuzu.db.store.SQLiteStore;
import com.zuzu.db.store.SimpleStoreIF;

public class OrangeUtils {
	public static String createCartData(ProductDTO product){
		String result="";
		try {
			result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?><prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
			result+="<cart><id_currency>1</id_currency>";
			result+="<id_customer></id_customer>";
			result+="<id_guest></id_guest>";
			result+="<id_lang>2</id_lang>";
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
