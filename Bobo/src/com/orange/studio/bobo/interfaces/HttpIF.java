package com.orange.studio.bobo.interfaces;

import java.io.InputStream;

import android.os.Bundle;

public interface HttpIF {
	public String getStringFromServer(String url,Bundle params);
	public String postDataToServer(String url,Bundle params);
	public String postDataToServer(String url,String rawData,int _statusCode);
	public InputStream postDataToServer(String url,String rawData);
}
