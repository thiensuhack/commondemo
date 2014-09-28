package com.orange.studio.bobo.models;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orange.studio.bobo.OrangeApplicationContext;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.Cache;
import com.orange.studio.bobo.interfaces.ProductIF;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
import com.orange.studio.bobo.xml.XMLHandlerProduct;
import com.zuzu.db.store.SimpleStoreIF;

public class ProductModel implements ProductIF{
	private static ProductIF _instance;
	private static final Lock createLock = new ReentrantLock();
	private static final int STORE_EXPIRE = 1*60; //3 minutes
		
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
	private SimpleStoreIF getStoreAdapter() {
		return OrangeUtils.getStoreAdapter(Cache.LIST_PRODUCT_CACHE_KEY,
				OrangeApplicationContext.getContext(), Cache.LIST_PRODUCT_CACHE_NUMBER);
	}
	public void setStore(String key, String value) {
		try {
			this.getStoreAdapter().put(key, value, STORE_EXPIRE);
		} catch (Exception e) {
		}		
	}
	public void setStore(String key, String value,int expiredTime) {
		try {
			this.getStoreAdapter().put(key, value, expiredTime);
		} catch (Exception e) {
		}		
	}
	@SuppressWarnings("unchecked")
	private List<ProductDTO> deserializeListData(String json) {
		List<ProductDTO> result = null;
		if (json == null || json.equals(""))
			return result;
		try {
			result = new ArrayList<ProductDTO>();
			Gson gson = new Gson();
			Type listType = new TypeToken<List<ProductDTO>>() {
			}.getType();
			result = (List<ProductDTO>) gson.fromJson(json, listType);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	@Override
	public List<ProductDTO> getListProduct(String url, RequestDTO request,
			Bundle params) {
		try {
			List<ProductDTO> result=null;
			url+=OrangeUtils.createUrl(params);
			String key=String.valueOf(url.hashCode());
			String json=getStoreAdapter().get(key);
			
			if(json!=null){
				
				result=deserializeListData(json);
			}
			if(result!=null && result.size()>0){
				
				return result;
			}
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			URL mUrl = new URL(url); 
			XMLHandlerProduct myXMLHandler = new XMLHandlerProduct(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(mUrl.openStream()));
			if(myXMLHandler.mListProducts!=null && myXMLHandler.mListProducts.size()>0){
				Gson gs=new Gson();
				String data=gs.toJson(myXMLHandler.mListProducts);
				if(data!=null){
					setStore(key, data,STORE_EXPIRE);
				}
			}
			return myXMLHandler.mListProducts;			
		} catch (Exception e) {
			return null;
		} 
	}

	@Override
	public ProductDTO getProductDetail(String url, RequestDTO request,
			Bundle params) {
		try {
			url+=request.proId+"/"+OrangeUtils.createUrl(params);
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			URL mUrl = new URL(url); 
			XMLHandlerProduct myXMLHandler = new XMLHandlerProduct(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(mUrl.openStream()));
			if(myXMLHandler.mListProducts!=null && myXMLHandler.mListProducts.size()>0){
				return myXMLHandler.mListProducts.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}		
	}
	@Override
	public List<ProductDTO> searchProduct(String url, RequestDTO request,
			Bundle params,String key) {
		try {
			url+=OrangeUtils.createUrl(params);
			url+="&'filter[name]=["+key+"]'";
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			URL mUrl = new URL(url); 
			XMLHandlerProduct myXMLHandler = new XMLHandlerProduct(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			xmlR.parse(new InputSource(mUrl.openStream()));			
			return myXMLHandler.mListProducts;			
		} catch (Exception e) {
			return null;
		} 
	}
}
