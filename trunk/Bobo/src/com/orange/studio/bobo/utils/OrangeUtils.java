package com.orange.studio.bobo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;

import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.REQUEST_PARAMS_NAME;
import com.zuzu.db.store.SQLiteStore;
import com.zuzu.db.store.SimpleStoreIF;

public class OrangeUtils {
	
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
