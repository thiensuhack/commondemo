package com.orange.studio.bobo.interfaces;

import java.util.List;

import android.os.Bundle;

import com.orange.studio.bobo.objects.MenuItemDTO;
import com.orange.studio.bobo.objects.RequestDTO;

public interface CategoryIF {
	public List<MenuItemDTO> getListMenuCategory(String url,RequestDTO request,Bundle params);
}
