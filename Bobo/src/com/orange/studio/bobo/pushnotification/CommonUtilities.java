package com.orange.studio.bobo.pushnotification;

import com.orange.studio.bobo.OrangeApplicationContext;
import com.orange.studio.bobo.utils.DeviceUtils;


public final class CommonUtilities {
	//public static final String SERVER_BASE_URL = "http://test.adreward.onetechonline.com/v2/registergcm";
	
	public static final String SERVER_REGISTER_METHOD = "acceptNotificationAndroid";
	public static final String SENDER_ID = "942277664486";

	public static final String APP_NAME = "adreward";
	public static String DEVICE_ID = "";
	public static final String USER_ID = "";

	// public static final String DISPLAY_MESSAGE_ACTION =
	// "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

	public static final String EXTRA_MESSAGE = "message";

	static {
		DEVICE_ID = DeviceUtils.getDeviceId(OrangeApplicationContext.getContext());
	}
	
	
}
