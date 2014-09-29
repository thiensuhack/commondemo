package com.orange.studio.bobo.models;

import java.io.StringReader;
import java.lang.reflect.Type;
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
import com.orange.studio.bobo.http.OrangeHttpRequest;
import com.orange.studio.bobo.interfaces.CommonIF;
import com.orange.studio.bobo.objects.CustomerDTO;
import com.orange.studio.bobo.objects.MenuItemDTO;
import com.orange.studio.bobo.objects.ProductOptionValueDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
import com.orange.studio.bobo.xml.XMLHandlerCategory;
import com.orange.studio.bobo.xml.XMLHandlerCustomer;
import com.orange.studio.bobo.xml.XMLHandlerProductOptionValue;
import com.zuzu.db.store.SimpleStoreIF;

public class CommonModel implements CommonIF{

	private static CommonIF _instance;
	private static final Lock createLock = new ReentrantLock();
	private static final int STORE_EXPIRE = 1*60; //3 minutes
	private static final int STORE_EXPIRE_FIVE = 5*60; //3 minutes
		
	public CommonModel() {
	}

	public static CommonIF getInstance() {
		if (_instance == null) {
			createLock.lock();
			if (_instance == null) {
				_instance = new CommonModel();
			}
			createLock.unlock();
		}
		return _instance;
	}
	private SimpleStoreIF getStoreAdapter() {
		return OrangeUtils.getStoreAdapter(Cache.LIST_COMMON_CACHE_KEY,
				OrangeApplicationContext.getContext(), Cache.LIST_COMMON_CACHE_NUMBER);
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
	private List<MenuItemDTO> deserializeListData(String json) {
		List<MenuItemDTO> result = null;
		if (json == null || json.equals(""))
			return result;
		try {
			result = new ArrayList<MenuItemDTO>();
			Gson gson = new Gson();
			Type listType = new TypeToken<List<MenuItemDTO>>() {
			}.getType();
			result = (List<MenuItemDTO>) gson.fromJson(json, listType);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	@Override
	public List<MenuItemDTO> getListMenuCategory(String url,
			RequestDTO request, Bundle params) {
		try {
			List<MenuItemDTO> result=null;
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
			XMLHandlerCategory myXMLHandler = new XMLHandlerCategory(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			String strResult=OrangeHttpRequest.getInstance().getDataXMLFromServer(url,null);
			if(strResult!=null && strResult.trim().length()>0){
				InputSource is = new InputSource(new StringReader(strResult));
				xmlR.parse(is);
				if(myXMLHandler.mListCategory!=null && myXMLHandler.mListCategory.size()>0){
					Gson gs=new Gson();
					String data=gs.toJson(myXMLHandler.mListCategory);
					if(data!=null){
						setStore(key, data,STORE_EXPIRE);
					}
				}
				return myXMLHandler.mListCategory;
			}
			return null;			
		} catch (Exception e) {
			return null;
		} 
	}
	@SuppressWarnings("unchecked")
	private List<ProductOptionValueDTO> deserializeListProductOptionValue(String json) {
		List<ProductOptionValueDTO> result = null;
		if (json == null || json.equals(""))
			return result;
		try {
			result = new ArrayList<ProductOptionValueDTO>();
			Gson gson = new Gson();
			Type listType = new TypeToken<List<ProductOptionValueDTO>>() {
			}.getType();
			result = (List<ProductOptionValueDTO>) gson.fromJson(json, listType);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	@Override
	public List<ProductOptionValueDTO> getListProductOptionValue(String url,RequestDTO request,Bundle params){
		try {
			List<ProductOptionValueDTO> result=null;
			url+=OrangeUtils.createUrl(params);
			String key=String.valueOf(url.hashCode());
			String json=getStoreAdapter().get(key);
			
			if(json!=null){
				
				result=deserializeListProductOptionValue(json);
			}
			if(result!=null && result.size()>0){
				
				return result;
			}
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			
			XMLHandlerProductOptionValue myXMLHandler = new XMLHandlerProductOptionValue(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			String strResult=OrangeHttpRequest.getInstance().getDataXMLFromServer(url,null);
			if(strResult!=null && strResult.trim().length()>0){
				InputSource is = new InputSource(new StringReader(strResult));
				xmlR.parse(is);
				if(myXMLHandler.mListProductOptionValue!=null && myXMLHandler.mListProductOptionValue.size()>0){
					Gson gs=new Gson();
					String data=gs.toJson(myXMLHandler.mListProductOptionValue);
					if(data!=null){
						setStore(key, data,STORE_EXPIRE_FIVE);
					}
				}
				return myXMLHandler.mListProductOptionValue;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public CustomerDTO registerUser(String url, String data) {
		try {			
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerCustomer myXMLHandler = new XMLHandlerCustomer(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().postDataToServer(url, data,201);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListCustomer!=null && myXMLHandler.mListCustomer.size()>0){
					return myXMLHandler.mListCustomer.get(0);
				}
			}			
			return null;			
		} catch (Exception e) {
			return null;
		} 
	}
}
