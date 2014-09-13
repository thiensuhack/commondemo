package com.orange.studio.bobo;

import android.app.Application;

public class OrangeApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		OrangeApplicationContext.setContext(getApplicationContext());
	}	
}
