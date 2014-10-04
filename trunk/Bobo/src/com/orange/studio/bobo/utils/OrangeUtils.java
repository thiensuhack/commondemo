package com.orange.studio.bobo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.orange.studio.bobo.OrangeApplicationContext;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.REQUEST_PARAMS_NAME;
import com.zuzu.db.store.SQLiteStore;
import com.zuzu.db.store.SimpleStoreIF;

public class OrangeUtils {
	public static String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
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
