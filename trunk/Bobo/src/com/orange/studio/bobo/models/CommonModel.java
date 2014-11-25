package com.orange.studio.bobo.models;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
import com.orange.studio.bobo.interfaces.CommonIF;
import com.orange.studio.bobo.objects.AboutUsDTO;
import com.orange.studio.bobo.objects.AddressDTO;
import com.orange.studio.bobo.objects.CarrierDTO;
import com.orange.studio.bobo.objects.ContactUsDTO;
import com.orange.studio.bobo.objects.CountryDTO;
import com.orange.studio.bobo.objects.CustomerDTO;
import com.orange.studio.bobo.objects.ItemCartDTO;
import com.orange.studio.bobo.objects.MenuItemDTO;
import com.orange.studio.bobo.objects.OrderDTO;
import com.orange.studio.bobo.objects.ProductFeatureDTO;
import com.orange.studio.bobo.objects.ProductFeatureValueDTO;
import com.orange.studio.bobo.objects.ProductOptionValueDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.objects.StockDTO;
import com.orange.studio.bobo.objects.SummaryDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
import com.orange.studio.bobo.xml.XMLHandlerAboutUs;
import com.orange.studio.bobo.xml.XMLHandlerAddress;
import com.orange.studio.bobo.xml.XMLHandlerCategory;
import com.orange.studio.bobo.xml.XMLHandlerCountry;
import com.orange.studio.bobo.xml.XMLHandlerCustomer;
import com.orange.studio.bobo.xml.XMLHandlerItemCart;
import com.orange.studio.bobo.xml.XMLHandlerProductFeatureValues;
import com.orange.studio.bobo.xml.XMLHandlerProductFeatures;
import com.orange.studio.bobo.xml.XMLHandlerProductOptionValue;
import com.orange.studio.bobo.xml.XMLHandlerStock;
import com.zuzu.db.store.SimpleStoreIF;

public class CommonModel implements CommonIF{

	private static CommonIF _instance;
	private static final Lock createLock = new ReentrantLock();
	private static final int STORE_EXPIRE = 1*60; //3 minutes
	private static final int STORE_EXPIRE_FIVE = 5*60; //3 minutes
	private static final int STORE_EXPIRE_A_DAY = 24*60*60; //3 minutes
		
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
			String strResult=OrangeHttpRequest.getInstance().getStringFromServer(url,null);
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
	@SuppressWarnings("unchecked")
	private List<ProductFeatureDTO> deserializeListProductFeatures(String json) {
		List<ProductFeatureDTO> result = null;
		if (json == null || json.equals(""))
			return result;
		try {
			result = new ArrayList<ProductFeatureDTO>();
			Gson gson = new Gson();
			Type listType = new TypeToken<List<ProductFeatureDTO>>() {
			}.getType();
			result = (List<ProductFeatureDTO>) gson.fromJson(json, listType);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	private List<ProductFeatureValueDTO> deserializeListProductFeatureValues(String json) {
		List<ProductFeatureValueDTO> result = null;
		if (json == null || json.equals(""))
			return result;
		try {
			result = new ArrayList<ProductFeatureValueDTO>();
			Gson gson = new Gson();
			Type listType = new TypeToken<List<ProductFeatureValueDTO>>() {
			}.getType();
			result = (List<ProductFeatureValueDTO>) gson.fromJson(json, listType);
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
			String strResult=OrangeHttpRequest.getInstance().getStringFromServer(url,null);
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
	@Override
	public ItemCartDTO addToCart(String url, String data) {
		try {			
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerItemCart myXMLHandler = new XMLHandlerItemCart(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().postDataToServer(url, data,201);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListItemCart!=null && myXMLHandler.mListItemCart.size()>0){
					return myXMLHandler.mListItemCart.get(0);
				}
			}			
			return null;			
		} catch (Exception e) {
			return null;
		} 
	}
	@Override
	public ItemCartDTO updateToCart(String url, String data) {
		try {			
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerItemCart myXMLHandler = new XMLHandlerItemCart(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().putDataToServer(url, data,200);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListItemCart!=null && myXMLHandler.mListItemCart.size()>0){
					return myXMLHandler.mListItemCart.get(0);
				}
			}			
			return null;			
		} catch (Exception e) {
			return null;
		} 
	}
	@Override
	public ItemCartDTO deleteToCart(String url, String data) {
		try {			
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerItemCart myXMLHandler = new XMLHandlerItemCart(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().deleteDataToServer(url);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListItemCart!=null && myXMLHandler.mListItemCart.size()>0){
					return myXMLHandler.mListItemCart.get(0);
				}
			}			
			return null;			
		} catch (Exception e) {
			return null;
		} 
	}
	@Override
	public CustomerDTO loginUser(String url){
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerCustomer myXMLHandler = new XMLHandlerCustomer(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListCustomer!=null && myXMLHandler.mListCustomer.size()>0){
					return myXMLHandler.mListCustomer.get(0);
				}
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public StockDTO getStock(String url) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerStock myXMLHandler = new XMLHandlerStock(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListStock!=null && myXMLHandler.mListStock.size()>0){
					return myXMLHandler.mListStock.get(0);
				}
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public SummaryDTO getSummary(String url) {
		try {
			String data=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			JSONObject jObject=new JSONObject(data);
			SummaryDTO sum=new SummaryDTO();						
			sum.total_discounts=jObject.optDouble("total_discounts", -1);
			sum.total_price=jObject.optDouble("total_price", -1);
			sum.total_price_without_tax=jObject.optDouble("total_price_without_tax", -1);
			sum.total_products=jObject.optDouble("total_products", -1);
			sum.total_shipping=jObject.optDouble("total_shipping", -1);
			sum.total_tax=jObject.optDouble("total_tax", -1);
			JSONObject jObjectDelivery=jObject.getJSONObject("delivery");
			sum.delivery.address1=jObjectDelivery.optString("address1", "");
			sum.delivery.alias=jObjectDelivery.optString("alias", "");
			sum.delivery.city=jObjectDelivery.optString("city", "");
			sum.delivery.country=jObjectDelivery.optString("country", "");
			sum.delivery.date_add=jObjectDelivery.optString("date_add", "");
			sum.delivery.date_upd=jObjectDelivery.optString("", "");
			sum.delivery.firstname=jObjectDelivery.optString("firstname", "");
			sum.delivery.id_country=jObjectDelivery.optString("id_country", "");
			sum.delivery.lastname=jObjectDelivery.optString("lastname", "");
			sum.delivery.phone=jObjectDelivery.optString("phone", "");
			sum.delivery.phone_mobile=jObjectDelivery.optString("phone_mobile", "");
			return sum;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public List<CarrierDTO> getListCarrier(String url) {
		try {
			List<CarrierDTO> result=new ArrayList<CarrierDTO>();
			CarrierDTO temp=null;
			String data=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			JSONObject jObject=new JSONObject(data);
			Iterator<String> iter=jObject.keys();			
			while (iter.hasNext()) {
				temp=new CarrierDTO();
				temp.id=iter.next();				
				temp.value=jObject.getString(temp.id);
				result.add(temp);
			}
			return result;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public List<ProductFeatureDTO> getListProductFeatures(String url) {
		try {
			List<ProductFeatureDTO> resultProductFeatures=null;
			String key=String.valueOf(url.hashCode());
			String json=getStoreAdapter().get(key);
			
			if(json!=null){
				
				resultProductFeatures=deserializeListProductFeatures(json);
			}
			if(resultProductFeatures!=null && resultProductFeatures.size()>0){
				
				return resultProductFeatures;
			}
			
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerProductFeatures myXMLHandler = new XMLHandlerProductFeatures(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListProductFeatures!=null && myXMLHandler.mListProductFeatures.size()>0){
					Gson gs=new Gson();
					String data=gs.toJson(myXMLHandler.mListProductFeatures);
					if(data!=null){
						setStore(key, data,STORE_EXPIRE_FIVE);
					}
				}
				return myXMLHandler.mListProductFeatures;
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public List<ProductFeatureValueDTO> getListProductFeatureValues(String url) {
		try {
			List<ProductFeatureValueDTO> resultProductFeatureValues=null;
			String key=String.valueOf(url.hashCode());
			String json=getStoreAdapter().get(key);
						if(json!=null){
				
				resultProductFeatureValues=deserializeListProductFeatureValues(json);
			}
			if(resultProductFeatureValues!=null && resultProductFeatureValues.size()>0){
				
				return resultProductFeatureValues;
			}
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerProductFeatureValues myXMLHandler = new XMLHandlerProductFeatureValues(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListProductFeatureValues!=null && myXMLHandler.mListProductFeatureValues.size()>0){
					Gson gs=new Gson();
					String data=gs.toJson(myXMLHandler.mListProductFeatureValues);
					if(data!=null){
						setStore(key, data,STORE_EXPIRE_FIVE);
					}
				}
				return myXMLHandler.mListProductFeatureValues;
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public String getColorStockAvailable(String url) {
		try {
			return OrangeHttpRequest.getInstance().getStringFromServer(url, null);
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public AboutUsDTO getAboutUs(String url) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerAboutUs myXMLHandler = new XMLHandlerAboutUs(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListAboutUs!=null && myXMLHandler.mListAboutUs.size()>0){
					return myXMLHandler.mListAboutUs.get(0);
				}
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public String sendContactUs(String url, ContactUsDTO contact) {
		try {
			Bundle mParams=new Bundle();
			mParams.putString("from", contact.from);
			mParams.putString("name", contact.name);
			mParams.putString("id_contact", contact.id_contact);
			mParams.putString("message", contact.message);
			String result=OrangeHttpRequest.getInstance().postDataToServer(UrlRequest.CONTACT_US_URL, mParams);
			JSONObject jb=new JSONObject(result);
			int status=jb.optInt("status");
			if(status==1){
				return "success";
			}else{
				return jb.optString("msg");
			}
			
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public OrderDTO createOrder(String url,String rawData) {
		try {
			String result=OrangeHttpRequest.getInstance().postDataToServer(url, rawData, 200);			
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public List<AddressDTO> getListAddress(String url) 
	{
		try {			
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerAddress myXMLHandler = new XMLHandlerAddress(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				return myXMLHandler.mListAddress;
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public AddressDTO createListAddress(String url,String rawData) 
	{
		try {			
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerAddress myXMLHandler = new XMLHandlerAddress(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().postDataToServer(url, rawData,201);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListAddress!=null && myXMLHandler.mListAddress.size()>0){
					return myXMLHandler.mListAddress.get(0);
				}
				return null;
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private List<CountryDTO> deserializeListCountry(String json) {
		List<CountryDTO> result = null;
		if (json == null || json.equals(""))
			return result;
		try {
			result = new ArrayList<CountryDTO>();
			Gson gson = new Gson();
			Type listType = new TypeToken<List<CountryDTO>>() {
			}.getType();
			result = (List<CountryDTO>) gson.fromJson(json, listType);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	@Override
	public List<CountryDTO> getListCountry(String url) {
		try {
			List<CountryDTO> resultProductFeatureValues=null;
			String key=String.valueOf(url.hashCode());
			String json=getStoreAdapter().get(key);
						if(json!=null){			
				resultProductFeatureValues=deserializeListCountry(json);
			}
			if(resultProductFeatureValues!=null && resultProductFeatureValues.size()>0){			
				return resultProductFeatureValues;
			}
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();						
			XMLHandlerCountry myXMLHandler = new XMLHandlerCountry(OrangeConfig.LANGUAGE_DEFAULT);
			xmlR.setContentHandler(myXMLHandler);			
			String result=OrangeHttpRequest.getInstance().getStringFromServer(url, null);
			if(result!=null && result.trim().length()>0){
				InputSource is = new InputSource(new StringReader(result));
				xmlR.parse(is);
				if(myXMLHandler.mListCountry!=null && myXMLHandler.mListCountry.size()>0){
					Gson gs=new Gson();
					String data=gs.toJson(myXMLHandler.mListCountry);
					if(data!=null){
						setStore(key, data,STORE_EXPIRE_A_DAY);
					}
				}
				return myXMLHandler.mListCountry;
			}			
			return null;
		} catch (Exception e) {
		}
		return null;
	}
}
