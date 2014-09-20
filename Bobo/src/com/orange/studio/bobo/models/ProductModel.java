package com.orange.studio.bobo.models;

import java.net.URL;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Bundle;

import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.interfaces.ProductIF;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
import com.orange.studio.bobo.xml.XMLHandler;

public class ProductModel implements ProductIF{
	private static ProductIF _instance;
	private static final Lock createLock = new ReentrantLock();
	private static final int STORE_EXPIRE = -1;
		
	public ProductModel() {
	}

	public static ProductIF getInstance() {
		if (_instance == null) {
			createLock.lock();
			if (_instance == null) {
				_instance = new ProductModel();
			}
			createLock.unlock();
		}
		return _instance;
	}
	@Override
	public List<ProductDTO> getListProduct(String url, RequestDTO request,
			Bundle params) {
		try {
			//String xmlUrl="http://bobo.vdigi.vn/api/products?ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT&output_format=JSON&display=full&sort=id_DESC&limit=3";
			url+=OrangeUtils.createUrl(params);
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			URL mUrl = new URL(url); 
			XMLHandler myXMLHandler = new XMLHandler(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(mUrl.openStream()));
			return myXMLHandler.mListProducts;			
		} catch (Exception e) {
			return null;
		} 
	}

	@Override
	public ProductDTO getProductDetail(String url, RequestDTO request,
			Bundle params) {
		return null;
	}

}
