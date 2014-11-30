package com.orange.studio.bobo.configs;

import java.util.List;

import com.orange.studio.bobo.objects.ProductFeatureDTO;
import com.orange.studio.bobo.objects.ProductFeatureValueDTO;

public final class OrangeConfig {

	public static int DBVERSION = 1;
	public static String LANGUAGE_DEFAULT = "1";
	public static String ITEMS_PAGE = "20";
	public static final String App_Key = "LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT";
	public static final String App_Private_Key = "CCXKyMUaJk20zROniIOMhJDiUCxxiKnMvN9xvF7WMGtzVzyu5an362rq";
	public static String DISPLAY_FIELDS = "full";

	public static List<ProductFeatureDTO> mListProductFeatures = null;
	public static List<ProductFeatureValueDTO> mListProductFeatureValues = null;

	public static class MENU_NAME {
		public static final int PRODUCT_DETAIL_FRAGMENT = -11;
		public static final int SHOPING_CART_FRAGMENT = -12;
		public static final int REGISTER_FRAGMENT = -13;
		public static final int SEARCH_RESULT_FRAGMENT = -14;
		public static final int PRODUCT_CATEGORY_FRAGMENT = -15;
		public static final int LOGIN_FRAGMENT = -16;
		public static final int SPIN_TO_WIN_FRAGMENT = -17;
		public static final int CREATE_ADDRESS = -18;
		public static final int SELECT_ADDRESS = -19;
		public static final int SUMMARY = -20;
		public static final int SELECT_CARRIER = -21;
	}

	public static class CartItemsRule {
		public static final int MAX_ITEMS_CART = 11; // max type products cart
														// is 11
		public static final int MAX_PRODUCT_ITEMS_CART = 5; // max products
															// counter is 5
	}

	public static class UrlRequest {
		public static final String main_domain = "http://bobo.vdigi.vn/";
		public static final String domain = "http://bobo.vdigi.vn/api";
		public static String PRODUCT_HOME = domain + "/products";
		public static String PRODUCT_DETAIL = domain + "/products/"; // products/{proId}/
		public static String CATEGORY_MENU = domain + "/categories/";
		public static String REGISTER = domain + "/customers?Create=Creating";
		public static String PRODUCT_OPTION_VALUES = domain
				+ "/product_option_values/";
		public static String LOGN_URL = domain + "/customers";
		public static String PRODUCT_FEATURE_URL = domain
				+ "/product_features?ws_key=" + App_Key + "&display=full";
		public static String PRODUCT_FEATURE_VALUE_URL = domain
				+ "/product_feature_values?ws_key=" + App_Key + "&display=full";		
		public static final String ABOUT_US_URL=domain+"/content_management_system/4?ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT&output_format=JSON&id=1";		
		public static String PRODUCT_COLOR_ITEM_STOCK ="http://www.bobo-u.com/modules/paypal/express_checkout/ajax.php?get_qty=1&id_product=%s&id_product_attribute=%s";
		public static final String CONTACT_US_URL=main_domain+"/index.php?controller=CustomAPI&action=contact&ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT";
		public static String HOME_POPULAR_PRODUCT=main_domain+"/index.php?controller=CustomAPI&action=popular&limit=";
		public static String HOME_BEST_SELLER_PRODUCT=main_domain+"/index.php?controller=CustomAPI&action=bestsales&limit=";
		public static String ADD_CART_URL=domain+"/carts?Create=Creating"+"&ws_key="+App_Key;
		//public static String UPDATE_CART_URL=domain+"/carts?Create=Creating"+"&ws_key="+App_Key;
		public static String GET_LIST_COUNTRY=domain+"/countries?ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT&display=full&filter[active]=1";
		public static String GET_USER_ADDRESS=domain+"/addresses?display=full&ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT&output_format=JSON&filter[id_customer]=";
		public static String CREATE_ADDRESS=domain+"/addresses?Create=Creating&ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT&output_format=JSON&display=full&sort=id_DESC&limit=10";
		public static String GET_CARRIER_URL=main_domain+"index.php?controller=CustomAPI&action=carrier&id_cart=";
		public static String GET_SUMMARY_URL=main_domain+"?controller=CustomAPI&action=summary&id_cart=";
		public static String CREATE_ORDER=domain+"/orders?Create=Creating&ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT";
		public static String HOME_SLIDER=main_domain+"index.php?controller=CustomAPI&action=slide";
		public static String GET_GAME_RESULT=main_domain+"index.php?controller=CustomAPI&action=game";
		
	}

	public static class REQUEST_PARAMS_NAME {
		public static final String WS_KEY = "ws_key";
		public static final String OUTPUT_FORMAT = "output_format";
		public static final String DISPLAY = "display";
		public static final String SORT = "sort";
		public static final String LITMIT = "limit";
	}

	public static class Cache {
		public static final String LIST_PRODUCT_CACHE_KEY = "producthomecache";
		public static final int LIST_PRODUCT_CACHE_NUMBER = 100;

		public static final String LIST_COMMON_CACHE_KEY = "categorycache";
		public static final int LIST_COMMON_CACHE_NUMBER = 100;
	}

	public static final String[] IMAGES = new String[] {
			"http://www.bobo-u.com/modules/homeslider/images/66f9b19f8d53b909d424e360092b502df5b2b2a9_slider_3.jpg",
			"http://www.bobo-u.com/modules/homeslider/images/4e71c6b5b8ea8e4047dc6891a65d9d55c822fa93_slider_1.jpg",
			"http://www.bobo-u.com/modules/homeslider/images/6a034292a3383b1c9d7e6b56d0953dc1677a4bc4_slider_2.jpg", };

	private OrangeConfig() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}

	public class URLRequestApi {
		public static final String domain = "http://test.adreward.onetechonline.com/v2";
		public static final String GET_REGIST_CGM = domain + "/registergcm";
		public static final String GET_REMOVE_CGM = domain + "/unregistergcm";
	}
}
