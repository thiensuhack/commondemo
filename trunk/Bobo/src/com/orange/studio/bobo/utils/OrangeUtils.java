package com.orange.studio.bobo.utils;

import com.orange.studio.bobo.configs.OrangeConfig.REQUEST_PARAMS_NAME;

import android.os.Bundle;

public class OrangeUtils {
	
	public static Bundle createRequestBundle(String limit) {		
		Bundle result = new Bundle();
		result.putString(REQUEST_PARAMS_NAME.WS_KEY, "LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT");
		result.putString(REQUEST_PARAMS_NAME.DISPLAY, "full");
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
