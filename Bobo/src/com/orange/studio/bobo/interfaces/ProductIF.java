package com.orange.studio.bobo.interfaces;

import java.util.List;

import android.os.Bundle;

import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.RequestDTO;

public interface ProductIF {
	public List<ProductDTO> getListProduct(String url,RequestDTO request,Bundle params);
	public ProductDTO getProductDetail(String url,RequestDTO request,Bundle params);
	public List<ProductDTO> searchProduct(String url,RequestDTO request,Bundle params,String key);
}
