package com.orange.studio.bobo.models;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orange.studio.bobo.OrangeApplicationContext;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.Cache;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.http.OrangeHttpRequest;
import com.orange.studio.bobo.interfaces.ProductIF;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.ProductFeatureAndValueDTO;
import com.orange.studio.bobo.objects.ProductFeatureDTO;
import com.orange.studio.bobo.objects.ProductFeatureValueDTO;
import com.orange.studio.bobo.objects.ProductOptionValueDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.objects.StockDTO;
import com.orange.studio.bobo.objects.TaxDTO;
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
			XMLHandlerProduct myXMLHandler = new XMLHandlerProduct(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			String strResult=OrangeHttpRequest.getInstance().getStringFromServer(url,null);
			if(strResult!=null && strResult.trim().length()>0){
				InputSource is = new InputSource(new StringReader(strResult));
				xmlR.parse(is);
				if(myXMLHandler.mListProducts!=null && myXMLHandler.mListProducts.size()>0){
					TaxDTO tax=null;
					for (int i = 0; i < myXMLHandler.mListProducts.size(); i++) {
						if(tax==null || tax.id!=myXMLHandler.mListProducts.get(i).id_tax_rules_group){
							tax=CommonModel.getInstance().getTax(myXMLHandler.mListProducts.get(i).id_tax_rules_group);
						}
						myXMLHandler.mListProducts.get(i).tax=tax;
						if(myXMLHandler.mListProducts.get(i).tax!=null){
							myXMLHandler.mListProducts.get(i).priceBeforeTax=(myXMLHandler.mListProducts.get(i).price/(1+myXMLHandler.mListProducts.get(i).tax.rate/100));
							myXMLHandler.mListProducts.get(i).priceBeforeTax = Math.round(myXMLHandler.mListProducts.get(i).priceBeforeTax * 100);
							myXMLHandler.mListProducts.get(i).priceBeforeTax = myXMLHandler.mListProducts.get(i).priceBeforeTax/100;
						}
					}
					Gson gs=new Gson();
					String data=gs.toJson(myXMLHandler.mListProducts);
					if(data!=null){
						setStore(key, data,STORE_EXPIRE);
					}
				}
				return myXMLHandler.mListProducts;
			}
			return null;
						
		} catch (Exception e) {
			return null;
		} 
	}
	@Override
	public List<ProductDTO> getListProductFeatures(String url,
			RequestDTO request, Bundle params) {
		try {
			List<ProductDTO> result=null;
			//url+=OrangeUtils.createUrl(params);
			String key=String.valueOf(url.hashCode());
			String json=getStoreAdapter().get(key);
			
			if(json!=null){
				
				result=deserializeListData(json);
			}
			if(result!=null && result.size()>0){
				
				return result;
			}
			String data=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(data!=null && data.trim().length()>0){
				result = parserListProductFromJson(data);
				if(result!=null && result.size()>0){
					Gson gs=new Gson();
					String strData=gs.toJson(result);
					setStore(key, strData,STORE_EXPIRE);
				}
				return result;
			}
			return null;					
		} catch (Exception e) {
			return null;
		} 
	}
	
	private List<ProductDTO> parserListProductFromJson(String json){
		try {		
			List<ProductDTO> result=new ArrayList<ProductDTO>();
			JSONArray jArr=new JSONArray(json);			
			if(jArr!=null && jArr.length()>0){
				ProductDTO item=null;
//				TaxDTO tax=null;
				for (int i = 0; i < jArr.length(); i++) {
					item=new ProductDTO();
					JSONObject jObject=jArr.getJSONObject(i);
					item.id=jObject.optString("id_product");
					item.name=jObject.optString("name");
					item.id_tax_rules_group=OrangeUtils.convertStringToInt(jObject.optString("id_tax_rules_group"));
//					if(tax==null || tax.id!=item.id_tax_rules_group){
//						tax = CommonModel.getInstance().getTax(item.id_tax_rules_group);
//					}
//					item.tax=tax;
					item.price=OrangeUtils.convertStringToDouble(jObject.optString("price"));
					item.wholesale_price=OrangeUtils.convertStringToDouble(jObject.optString("wholesale_price"));
					item.unit_price_ratio=OrangeUtils.convertStringToDouble(jObject.optString("unit_price_ratio"));
					item.id_default_image=UrlRequest.domain+"/images/products/"+item.id+"/"+jObject.optString("id_image")+"?ws_key="+OrangeConfig.App_Key;
					result.add(item);
				}
				return result;
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public ProductDTO getProductDetail(String url, RequestDTO request,
			Bundle params) {
		try {
			url+=request.proId+"/"+OrangeUtils.createUrl(params);
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			
			XMLHandlerProduct myXMLHandler = new XMLHandlerProduct(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url,null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListProducts!=null && myXMLHandler.mListProducts.size()>0){
					ProductDTO product=myXMLHandler.mListProducts.get(0);
					TaxDTO tax = CommonModel.getInstance().getTax(product.id_tax_rules_group);
					if(tax!=null){
						product.tax=tax;
						product.priceBeforeTax=(product.price/(1+product.tax.rate/100));
						product.priceBeforeTax = Math.round(product.priceBeforeTax * 100);
						product.priceBeforeTax = product.priceBeforeTax/100;
					}
					else{
						product.priceBeforeTax=product.price;
					}
					if(product.productOptionValues!=null && product.productOptionValues.size()>0){
						product.listProductOptionValues=new ArrayList<ProductOptionValueDTO>();
						
						String urlProductOptionValue=UrlRequest.PRODUCT_OPTION_VALUES+"?ws_key="+OrangeConfig.App_Key+"&display=full";
						List<ProductOptionValueDTO> mListProductOptionValue=CommonModel.getInstance().getListProductOptionValue(urlProductOptionValue, null, null);
						if(mListProductOptionValue!=null && mListProductOptionValue.size()>0){
							for (String strItem : product.productOptionValues) {
								for (ProductOptionValueDTO item : mListProductOptionValue) {
									if(strItem.equals(item.id.trim())){
										product.listProductOptionValues.add(item);
									}
								}
							}
						}
					}		
					if(product.mListStock!=null && product.mListStock.size()>0){
						String stockUrl="";
						if(product.mListStock.size()==1){
							stockUrl=product.mListStock.get(0).linkHref+"?ws_key="+OrangeConfig.App_Key;
						}else{
							stockUrl=product.mListStock.get(1).linkHref+"?ws_key="+OrangeConfig.App_Key;
						}
						StockDTO stock=CommonModel.getInstance().getStock(stockUrl);
						product.stock=stock;
					}
					
					if(OrangeConfig.mListProductFeatures==null){
						OrangeConfig.mListProductFeatures = CommonModel.getInstance().getListProductFeatures(UrlRequest.PRODUCT_FEATURE_URL);
					}
					if(OrangeConfig.mListProductFeatureValues==null){
						OrangeConfig.mListProductFeatureValues = CommonModel.getInstance().getListProductFeatureValues(UrlRequest.PRODUCT_FEATURE_VALUE_URL);
					}
					if(product.listProductFeatures!=null && product.listProductFeatures.size()>0 && OrangeConfig.mListProductFeatures!=null && OrangeConfig.mListProductFeatures.size()>0 && OrangeConfig.mListProductFeatureValues!=null && OrangeConfig.mListProductFeatureValues.size()>0){
						for (ProductFeatureAndValueDTO pro : product.listProductFeatures) {
							for (ProductFeatureDTO item : OrangeConfig.mListProductFeatures) {
								if(pro.id.equals(item.id)){
									pro.feature_title=item.name;
									break;
								}
							}
							for (ProductFeatureValueDTO item2 : OrangeConfig.mListProductFeatureValues) {
								if(pro.id_feature_value.equals(item2.id)){
									pro.feature_value=item2.value;
									break;
								}
							}
						}						
					}
					return product;
				}
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
			url+="&filter[name]=%25%5b"+key+"%5d%25";
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
