package com.orange.studio.bobo.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.configs.OrangeConfig.CartItemsRule;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.dialogs.ExitDialog;
import com.orange.studio.bobo.fragments.AboutFragment;
import com.orange.studio.bobo.fragments.BestSellerProductFragment;
import com.orange.studio.bobo.fragments.ContactUsFragment;
import com.orange.studio.bobo.fragments.HomeFragment;
import com.orange.studio.bobo.fragments.LoginFragment;
import com.orange.studio.bobo.fragments.NavigationDrawerFragment;
import com.orange.studio.bobo.fragments.PopularProductFragment;
import com.orange.studio.bobo.fragments.ProductCategoryFragment;
import com.orange.studio.bobo.fragments.ProductDetailFragment;
import com.orange.studio.bobo.fragments.RegisterFragment;
import com.orange.studio.bobo.fragments.SearchResultFragment;
import com.orange.studio.bobo.fragments.ShoppingCartFragment;
import com.orange.studio.bobo.fragments.SpinToWinFragment;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.ItemCartDTO;
import com.orange.studio.bobo.objects.MenuItemDTO;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.utils.OrangeUtils;

public class HomeActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, OnClickListener {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private ActionBar mActionbar;
	private CharSequence mTitle;
	private TextView mAppTitle = null;
	private ImageView mNavIconMenu = null;
	private View mShoppingCartBtn = null;
	private ImageView mAppIcon = null;
	private TextView mTotalItemsCart = null;

	private ProductDTO mCurrentProduct = null;

	public List<ProductDTO> mListItemCart = null;

	public String mSearchKey=null;
	public MenuItemDTO mCurCategory=new MenuItemDTO();
	
	public interface MainHomeActivityHandler {
		public void exitApplication();
	}

	private MainHomeActivityHandler mHandler = null;
	private ExitDialog mExitDialog = null;
	private Fragment mCurFragment=null;
	
	private ProgressDialog mProgress=null;
	private AddCartTask mAddCartTask=null;
	
	public enum HOME_TABS{
		ALL,BEST_SELLER,POPULAR
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		mActionbar = getSupportActionBar();
		mActionbar.hide();
		getSupportFragmentManager().addOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						Fragment f = getSupportFragmentManager()
								.findFragmentById(R.id.container);
						if (f != null) {
							updateTitleAndDrawer(f);
						}

					}
				});
		initView();
		initListener();
		initProgress(null);
	}
	protected void initProgress(String message){
		mProgress=new ProgressDialog(HomeActivity.this);
		if(message!=null){
			mProgress.setMessage(message);
		}else{
			mProgress.setMessage(getString(R.string.waitting_message));
		}
	}
	private void initView() {
		mNavIconMenu = (ImageView) findViewById(R.id.naviMenuIcon);
		mAppIcon = (ImageView) findViewById(R.id.iconBobo);
		mAppTitle = (TextView) findViewById(R.id.appTitle);
		mShoppingCartBtn = (RelativeLayout) findViewById(R.id.homeShoppingCartBtn);
		mTotalItemsCart = (TextView) findViewById(R.id.totalItemsCart);
		mHandler = new MainHomeActivityHandler() {

			@Override
			public void exitApplication() {
				try {
					ImageLoader.getInstance().clearMemoryCache();
					finish();
				} catch (Exception e) {
					return;
				}
			}
		};
		mExitDialog = new ExitDialog(this, mHandler);
	}

	private void initListener() {
		mNavIconMenu.setOnClickListener(this);
		mAppIcon.setOnClickListener(this);
		mShoppingCartBtn.setOnClickListener(this);
	}

	public void setAppTitle(String title) {
		mAppTitle.setText(title);
	}
	public void addCart(ProductDTO product){
		if(mAddCartTask==null || mAddCartTask.getStatus()==Status.FINISHED){
			mAddCartTask=new AddCartTask(product);
			mAddCartTask.execute();
		}
	}
	private void updateTitleAndDrawer(Fragment fragment) {
		String mFragmentName = fragment.getClass().getName();
		if (mFragmentName.equals(HomeFragment.class.getName())) {
			setAppTitle(getString(R.string.app_name));
			return;
		}
		if (mFragmentName.equals(PopularProductFragment.class.getName())) {
			setAppTitle(getString(R.string.menu_drawer_product));
			return;
		}
		if (mFragmentName.equals(BestSellerProductFragment.class.getName())) {
			setAppTitle(getString(R.string.menu_drawer_best_seller));
			return;
		}
		if (mFragmentName.equals(ProductDetailFragment.class.getName())) {
			setAppTitle(getString(R.string.detail_title_label));
			return;
		}
		if (mFragmentName.equals(ShoppingCartFragment.class.getName())) {
			setAppTitle(getString(R.string.app_name));
			return;
		}
		if (mFragmentName.equals(AboutFragment.class.getName())) {
			setAppTitle(getString(R.string.menu_drawer_about_us));
			return;
		}
		if (mFragmentName.equals(ContactUsFragment.class.getName())) {
			setAppTitle(getString(R.string.menu_drawer_contact_us));
			return;
		}
		if (mFragmentName.equals(ProductCategoryFragment.class.getName())) {
			setAppTitle(mCurCategory.name);
			return;
		}
		setAppTitle(getString(R.string.app_name));
		return;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Fragment mFragment = null;
		switch (position) {
		case 0:			
//		case 1:
//			if(mCurFragment!=null && mCurFragment.getClass().getName().equals(HomeFragment.class.getName())){
//				HomeFragment mHomeFragment=((HomeFragment)mCurFragment);
//				mHomeFragment.setCurrentTab(HOME_TABS.ALL);
//				mHomeFragment.loadData();
//			}
//			mFragment = HomeFragment.instantiate(getApplicationContext(),
//					HomeFragment.class.getName());
//			mCurFragment=mFragment;
//			break;
//		case 2:
//			if(mCurFragment!=null && mCurFragment.getClass().getName().equals(HomeFragment.class.getName())){
//				HomeFragment mHomeFragment=((HomeFragment)mCurFragment);
//				mHomeFragment.setCurrentTab(HOME_TABS.POPULAR);
//				mHomeFragment.loadData();
//			}
//			mFragment = HomeFragment.instantiate(getApplicationContext(),
//					HomeFragment.class.getName());
//			mCurFragment=mFragment;
//			break;
//		case 3:
//			if(mCurFragment!=null && mCurFragment.getClass().getName().equals(HomeFragment.class.getName())){
//				HomeFragment mHomeFragment=((HomeFragment)mCurFragment);
//				mHomeFragment.setCurrentTab(HOME_TABS.BEST_SELLER);
//				mHomeFragment.loadData();
//			}
//			mFragment = HomeFragment.instantiate(getApplicationContext(),
//					HomeFragment.class.getName());
//			mCurFragment=mFragment;
//			break;
		case 1:
			mFragment = HomeFragment.instantiate(getApplicationContext(),
					HomeFragment.class.getName());
			break;
		case 2:
			mFragment = PopularProductFragment.instantiate(getApplicationContext(),
					PopularProductFragment.class.getName());
			break;
		case 3:
			mFragment = BestSellerProductFragment.instantiate(getApplicationContext(),
					BestSellerProductFragment.class.getName());
			break;
		case 9:
			mFragment = AboutFragment.instantiate(getApplicationContext(),
					AboutFragment.class.getName());
			break;
		case 10:
			mFragment = ContactUsFragment.instantiate(getApplicationContext(),
					ContactUsFragment.class.getName());
			break;
		case MENU_NAME.PRODUCT_DETAIL_FRAGMENT:
			mFragment = ProductDetailFragment.instantiate(
					getApplicationContext(),
					ProductDetailFragment.class.getName());
			break;
		case MENU_NAME.SHOPING_CART_FRAGMENT:
			mFragment = ShoppingCartFragment.instantiate(
					getApplicationContext(),
					ShoppingCartFragment.class.getName());
			break;
		case MENU_NAME.REGISTER_FRAGMENT:
			mFragment = RegisterFragment.instantiate(
					getApplicationContext(),
					RegisterFragment.class.getName());
			break;
		case MENU_NAME.SEARCH_RESULT_FRAGMENT:
			if(mCurFragment!=null && mCurFragment.getClass().getName().equals(SearchResultFragment.class.getName())){
				((SearchResultFragment)mCurFragment).onResume();
				return;
			}
			mFragment = SearchResultFragment.instantiate(
					getApplicationContext(),
					SearchResultFragment.class.getName());
			break;
		case MENU_NAME.PRODUCT_CATEGORY_FRAGMENT:
			if(mCurFragment!=null && mCurFragment.getClass().getName().equals(ProductCategoryFragment.class.getName())){
				((ProductCategoryFragment)mCurFragment).onResume();
				return;
			}
			mFragment = ProductCategoryFragment.instantiate(
					getApplicationContext(),
					ProductCategoryFragment.class.getName());			
			break;
		case MENU_NAME.LOGIN_FRAGMENT:
			mFragment = LoginFragment.instantiate(
					getApplicationContext(),
					LoginFragment.class.getName());
			break;
		case MENU_NAME.SPIN_TO_WIN_FRAGMENT:
			mFragment = SpinToWinFragment.instantiate(
					getApplicationContext(),
					SpinToWinFragment.class.getName());
			break;
		default:
			break;
		}
		mCurFragment=mFragment;
		replaceFragment(mFragment);
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	private void replaceFragment(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		String backStateName = fragment.getClass().getName();
		String fragmentTag = backStateName;
		FragmentManager fragmentManager = getSupportFragmentManager();
		boolean fragmentPopped = fragmentManager.popBackStackImmediate(
				backStateName, 0);

		if (!fragmentPopped
				&& fragmentManager.findFragmentByTag(fragmentTag) == null) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.container, fragment, fragmentTag);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(backStateName);
			ft.commit();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.naviMenuIcon:
			mNavigationDrawerFragment.openNaviDrawer();
			break;
		case R.id.iconBobo:
			onNavigationDrawerItemSelected(9);
			break;
		case R.id.homeShoppingCartBtn:
			onNavigationDrawerItemSelected(-12);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			mExitDialog.show();
			return;
		} else {
			super.onBackPressed();
		}
	}

	public ProductDTO getCurrentProduct() {
		return mCurrentProduct;
	}

	public void setCurrentProduct(ProductDTO mCurrentProduct) {
		this.mCurrentProduct = mCurrentProduct;
	}

	public boolean isHasItemsCart() {
		try {
			if (mListItemCart != null && mListItemCart.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private void addToCart(ProductDTO proItem) {
		if (proItem == null) {
			return;
		}
		if (mListItemCart == null) {
			mListItemCart = new ArrayList<ProductDTO>();
		}

		boolean isExisted = false;
		for (ProductDTO item : mListItemCart) {
			if (item.id.equals(proItem.id)) {
				if (item.cartCounter >= CartItemsRule.MAX_ITEMS_CART) {
					return;
				}
				item.cartCounter++;
				isExisted = true;
				break;
			}
		}

		if (!isExisted) {
			proItem.cartCounter = 1;
			mListItemCart.add(0, proItem);
		}
		updateItemCartCounter();
		Toast.makeText(getApplicationContext(), "Added:" + proItem.name,
				Toast.LENGTH_SHORT).show();
		return;
	}

	public void removeCartItem(String proId) {
		if (proId == null || mListItemCart == null) {
			return;
		}
		for (int i = 0; i < mListItemCart.size(); i++) {
			if (mListItemCart.get(i).id.equals(proId)) {
				mListItemCart.remove(i);
				break;
			}
		}
		updateItemCartCounter();
	}

	public void decreaseCartItem(String proId) {
		if (proId == null || mListItemCart == null) {
			return;
		}
		for (int i = 0; i < mListItemCart.size(); i++) {
			if (mListItemCart.get(i).id.equals(proId)
					&& mListItemCart.get(i).cartCounter > 0) {
				mListItemCart.get(i).cartCounter--;
				break;
			}
		}
		updateItemCartCounter();
	}

	public void updateItemCartCounter() {
		try {
			if (isHasItemsCart()) {
				mTotalItemsCart.setText(String.valueOf(mListItemCart.size()));
				mTotalItemsCart.setVisibility(View.VISIBLE);
			} else {
				mTotalItemsCart.setText("0");
				mTotalItemsCart.setVisibility(View.GONE);
			}
		} catch (Exception e) {
		}
	}

	public double getTotalPriceItemsCart() {
		if (mListItemCart == null || mListItemCart.size() < 1) {
			return 0;
		}
		double result = 0;
		for (ProductDTO item : mListItemCart) {
			result += item.price * item.cartCounter;
		}
		result = result*100;
		result = Math.round(result);
		result = result /100;
		return result;
	}
	public void setSearchKey(String searchKey){
		mSearchKey=searchKey;
		if(mNavigationDrawerFragment!=null){
			mNavigationDrawerFragment.setSearchKey(searchKey);
		}
	}
	public void hideSoftKeyBoard(){
//		try {
//			InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
//			View view = this.getCurrentFocus();
//		    if (view != null) {
//		        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		    }
//		} catch (Exception e) {
//		}
	}
	public void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	class AddCartTask extends AsyncTask<Void, Void, ItemCartDTO> {
		private ProductDTO mProductDTO = null;

		public AddCartTask(ProductDTO _product) {
			mProductDTO = _product;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress.show();
		}

		@Override
		protected ItemCartDTO doInBackground(Void... params) {
			try {
				String data=OrangeUtils.createCartData(mProductDTO);
				return CommonModel.getInstance().addToCart(UrlRequest.ADD_CART_URL, data);
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(ItemCartDTO result) {
			super.onPostExecute(result);
			try {
				if(result!=null && result.id.trim().length()>0){
					addToCart(mProductDTO);
				}else{
					showToast(getString(R.string.add_cart_failed));
				}
			} catch (Exception e) {
			}	
			finally{
				if(mProgress.isShowing()){
					mProgress.dismiss();
				}
			}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		updateItemCartCounter();
	}
}
