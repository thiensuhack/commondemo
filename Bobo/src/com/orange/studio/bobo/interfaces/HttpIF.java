package com.orange.studio.bobo.interfaces;

import java.io.InputStream;

import android.os.Bundle;

public interface HttpIF {
	//GET
	public String getStringFromServer(String url,Bundle params);
	
	//POST
	public String postDataToServer(String url,Bundle params);
	public String postDataToServer(String url,String rawData,int _statusCode);
	public InputStream postDataToServer(String url,String rawData);
	
	//PUT
	public String putDataToServer(String url,String rawData,int _statusCode);
	
	//DELETE
	public String deleteDataToServer(String url);
}
