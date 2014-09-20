package com.orange.studio.bobo.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
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

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.configs.OrangeConfig.CartItemsRule;
import com.orange.studio.bobo.fragments.AboutFragment;
import com.orange.studio.bobo.fragments.ContactUsFragment;
import com.orange.studio.bobo.fragments.HomeFragment;
import com.orange.studio.bobo.fragments.NavigationDrawerFragment;
import com.orange.studio.bobo.fragments.ProductDetailFragment;
import com.orange.studio.bobo.fragments.ShoppingCartFragment;
import com.orange.studio.bobo.objects.ProductDTO;

public class HomeActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, OnClickListener {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private ActionBar mActionbar;
	private CharSequence mTitle;
	private TextView mAppTitle = null;
	private ImageView mNavIconMenu = null;
	private View mShoppingCartBtn = null;
	private ImageView mAppIcon = null;
	private TextView mTotalItemsCart=null;
	
	private ProductDTO mCurrentProduct = null;

	public List<ProductDTO> mListItemCart = null;

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
	}

	private void initView() {
		mNavIconMenu = (ImageView) findViewById(R.id.naviMenuIcon);
		mAppIcon = (ImageView) findViewById(R.id.iconBobo);
		mAppTitle = (TextView) findViewById(R.id.appTitle);
		mShoppingCartBtn = (RelativeLayout) findViewById(R.id.homeShoppingCartBtn);
		mTotalItemsCart=(TextView)findViewById(R.id.totalItemsCart);
	}

	private void initListener() {
		mNavIconMenu.setOnClickListener(this);
		mAppIcon.setOnClickListener(this);
		mShoppingCartBtn.setOnClickListener(this);
	}

	public void setAppTitle(String title) {
		mAppTitle.setText(title);
	}

	private void updateTitleAndDrawer(Fragment fragment) {
		String mFragmentName = fragment.getClass().getName();
		if (mFragmentName.equals(HomeFragment.class.getName())) {
			setAppTitle(getString(R.string.app_name));
			return;
		}
		if (mFragmentName.equals(ProductDetailFragment.class.getName())) {
			if (mCurrentProduct != null) {
				setAppTitle(mCurrentProduct.name);
			}
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
		setAppTitle(getString(R.string.app_name));
		return;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		Fragment mFragment = null;
		switch (position) {
		case 0:
			mFragment = HomeFragment.instantiate(getApplicationContext(),
					HomeFragment.class.getName());
			break;
		case 1:
			break;
		case 2:
			break;
		case 9:
			mFragment = AboutFragment.instantiate(getApplicationContext(),
					AboutFragment.class.getName());
			break;
		case 10:
			mFragment = ContactUsFragment.instantiate(getApplicationContext(),
					ContactUsFragment.class.getName());
			break;
		case -11:
			mFragment = ProductDetailFragment.instantiate(
					getApplicationContext(),
					ProductDetailFragment.class.getName());
			break;
		case -12:
			mFragment = ShoppingCartFragment.instantiate(
					getApplicationContext(),
					ShoppingCartFragment.class.getName());
			break;
		default:
			mFragment = HomeFragment.instantiate(getApplicationContext(),
					HomeFragment.class.getName());
			break;
		}

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
			finish();
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
		if (mListItemCart != null && mListItemCart.size() > 0) {
			return true;
		}
		return false;
	}

	public void addToCart(ProductDTO proItem) {
		if (proItem == null) {
			return;
		}
		if (mListItemCart == null) {
			mListItemCart = new ArrayList<ProductDTO>();
		}
		if (mListItemCart.size() > CartItemsRule.MAX_ITEMS_CART) {
			Toast.makeText(
					getApplicationContext(),
					"max cart items:" + String.valueOf(CartItemsRule.MAX_ITEMS_CART),
					Toast.LENGTH_LONG).show();
			return;
		}
		boolean isExisted=false;
		for (ProductDTO item : mListItemCart) {
			if (item.id.equals(proItem.id)) {
				item.cartCounter++;
				isExisted=true;
				break;
			}
		}
		
		if(!isExisted){			
			proItem.cartCounter=1;
			mListItemCart.add(proItem);
		}		
		updateItemCartCounter();
		Toast.makeText(getApplicationContext(),"Added:" + proItem.name,Toast.LENGTH_LONG).show();
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
	public void updateItemCartCounter(){
		if(isHasItemsCart()){
			mTotalItemsCart.setText(String.valueOf(mListItemCart.size()));
			mTotalItemsCart.setVisibility(View.VISIBLE);
		}else{
			mTotalItemsCart.setText("0");
			mTotalItemsCart.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		updateItemCartCounter();
	}
}
