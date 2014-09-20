package com.orange.studio.bobo.models;

import java.util.List;

import android.os.Bundle;

import com.orange.studio.bobo.interfaces.ProductIF;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.RequestDTO;

public class ProductModel implements ProductIF{

	@Override
	public List<ProductDTO> getListProduct(String url, RequestDTO request,
			Bundle params) {
		return null;
	}

	@Override
	public ProductDTO getProductDetail(String url, RequestDTO request,
			Bundle params) {
		return null;
	}

}
