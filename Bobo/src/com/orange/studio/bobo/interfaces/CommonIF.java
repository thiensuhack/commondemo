package com.orange.studio.bobo.interfaces;

import java.util.List;

import android.os.Bundle;

import com.orange.studio.bobo.objects.AboutUsDTO;
import com.orange.studio.bobo.objects.ContactUsDTO;
import com.orange.studio.bobo.objects.CustomerDTO;
import com.orange.studio.bobo.objects.ItemCartDTO;
import com.orange.studio.bobo.objects.MenuItemDTO;
import com.orange.studio.bobo.objects.ProductFeatureDTO;
import com.orange.studio.bobo.objects.ProductFeatureValueDTO;
import com.orange.studio.bobo.objects.ProductOptionValueDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.objects.StockDTO;

public interface CommonIF {
	public List<MenuItemDTO> getListMenuCategory(String url,RequestDTO request,Bundle params);
	public List<ProductOptionValueDTO> getListProductOptionValue(String url,RequestDTO request,Bundle params);
	public CustomerDTO registerUser(String url,String data);
	public CustomerDTO loginUser(String url);
	public StockDTO getStock(String url);
	public ItemCartDTO addToCart(String url,String data);
	public List<ProductFeatureDTO> getListProductFeatures(String url);
	public List<ProductFeatureValueDTO> getListProductFeatureValues(String url);
	public String getColorStockAvailable(String url);
	public AboutUsDTO getAboutUs(String url);
	public String sendContactUs(String url,ContactUsDTO contact);
}
