package com.orange.studio.bobo.interfaces;

import java.util.List;

import android.os.Bundle;

import com.orange.studio.bobo.objects.CustomerDTO;
import com.orange.studio.bobo.objects.MenuItemDTO;
import com.orange.studio.bobo.objects.RequestDTO;

public interface CommonIF {
	public List<MenuItemDTO> getListMenuCategory(String url,RequestDTO request,Bundle params);
	public CustomerDTO registerUser(String url,String data);
}
