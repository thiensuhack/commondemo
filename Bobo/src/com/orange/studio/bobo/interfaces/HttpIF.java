package com.orange.studio.bobo.interfaces;

import android.os.Bundle;

public interface HttpIF {
	public String getDataXMLFromServer(String url,Bundle params);
	public String postDataToServer(String url,Bundle params);
}
