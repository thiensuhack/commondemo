package com.orange.studio.bobo;

import android.content.Context;

public class OrangeApplicationContext {
	public String applicationName = "OrangeApplicationContext";
	public Context context = null;

	private static OrangeApplicationContext _instance;

	public static OrangeApplicationContext getInstance() {
		if (_instance == null) {
			_instance = new OrangeApplicationContext();
		}
		return _instance;
	}

	public static void setApplicationName(String appName) {
		getInstance().applicationName = appName;
	}

	public static String getApplicationName() {
		return getInstance().applicationName;
	}

	public static void setContext(Context context) {
		getInstance().context = context;
	}

	public static Context getContext() {
		return getInstance().context;
	}

}
