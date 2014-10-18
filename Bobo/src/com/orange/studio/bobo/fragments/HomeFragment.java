package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity.HOME_TABS;
import com.orange.studio.bobo.adapters.GridProductAdapter;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.customviews.ExpandableHeightGridView;
import com.orange.studio.bobo.models.ProductModel;
import com.orange.studio.bobo.objects.HomeSliderDTO;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeFragment extends BaseFragment implements OnItemClickListener,
		OnClickListener {
	private ViewPager mViewPager = null;
	private CirclePageIndicator mCirclePageIndicator = null;

	private ImageHomeSlider mSilderAdapter = null;

	private LoadHomeSliderTask mLoadHomeSliderTask = null;
	private ExpandableHeightGridView mGridView = null;
	private GridProductAdapter mProductAdapter = null;
	private LoadProductsTask mLoadProductsTask = null;

	private TextView mMenuAll = null;
	private TextView mMenuBestSeller = null;
	private TextView mMenuPopular = null;
	public HOME_TABS mCurrentTab = HOME_TABS.ALL;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_home, container, false);
			initView();
			initListener();
		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}

	private void initView() {
		mHomeActivity=getHomeActivity();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_bobo_app)
				.showImageOnFail(R.drawable.ic_bobo_app)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		mViewPager = (ViewPager) mView.findViewById(R.id.viewPagerHome);
		mCirclePageIndicator = (CirclePageIndicator) mView
				.findViewById(R.id.indicatorHome);
		mGridView = (ExpandableHeightGridView) mView
				.findViewById(R.id.homeGridView);
		mGridView.setExpanded(true);
		mProductAdapter = new GridProductAdapter(getActivity());
		mGridView.setAdapter(mProductAdapter);

		mMenuAll = (TextView) mView.findViewById(R.id.fragmentHomeMenuAll);
		mMenuBestSeller = (TextView) mView
				.findViewById(R.id.fragmentHomeMenuBestSeller);
		mMenuPopular = (TextView) mView
				.findViewById(R.id.fragmentHomeMenuPopular);				
		initLoadingView();
		initNotFoundView();
		switchView(false, false);
	}

	private void initListener() {
		mGridView.setOnItemClickListener(this);
		mMenuAll.setOnClickListener(this);
		mMenuBestSeller.setOnClickListener(this);
		mMenuPopular.setOnClickListener(this);
	}

	private void loadHomeSliderData() {
		if (mLoadHomeSliderTask == null
				|| mLoadHomeSliderTask.getStatus() == Status.FINISHED) {
			mLoadHomeSliderTask = new LoadHomeSliderTask();
			mLoadHomeSliderTask.execute();
		}
	}

	private void loadProductData() {
		if (mLoadProductsTask == null
				|| mLoadProductsTask.getStatus() == Status.FINISHED) {
			mLoadProductsTask = new LoadProductsTask();
			mLoadProductsTask.execute();
		}
	}

	@Override
	public void onResume() {
		super.onResume();						
		if(mHomeActivity==null){
			mHomeActivity=getHomeActivity();			
		}
		loadData();
	}
	public void setCurrentTab(HOME_TABS mTab){
		mCurrentTab=mTab;
	}
	public void loadData() {
		loadHomeSliderData();
		switchTabViewsCurrentTabs(mCurrentTab);		
	}

	private class ImageHomeSlider extends PagerAdapter {
		private LayoutInflater inflater;
		private List<HomeSliderDTO> mData = new ArrayList<HomeSliderDTO>();

		public ImageHomeSlider(List<HomeSliderDTO> _mData) {
			inflater = LayoutInflater.from(getActivity());
			mData = _mData;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View mContainer = inflater.inflate(
					R.layout.layout_image_home_slider, view, false);
			assert mContainer != null;
			ImageView imageView = (ImageView) mContainer
					.findViewById(R.id.imageHomeSlider);

			ImageLoader.getInstance().displayImage(
					mData.get(position).imageURL, imageView, options,null);

			view.addView(mContainer, 0);
			return mContainer;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	private class LoadHomeSliderTask extends
			AsyncTask<Void, Void, List<HomeSliderDTO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<HomeSliderDTO> doInBackground(Void... arg0) {
			List<HomeSliderDTO> result = new ArrayList<HomeSliderDTO>();
			for (int i = 0; i < OrangeConfig.IMAGES.length; i++) {
				HomeSliderDTO item = new HomeSliderDTO();
				item.imageURL = OrangeConfig.IMAGES[i];
				result.add(item);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<HomeSliderDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				mSilderAdapter = new ImageHomeSlider(result);
				mViewPager.setAdapter(mSilderAdapter);
				mCirclePageIndicator.setViewPager(mViewPager);
			}
		}
	}

	private class LoadProductsTask extends
			AsyncTask<Void, Void, List<ProductDTO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();			
			mGridView.setVisibility(View.INVISIBLE);
			switchView(false, true);
			mProductAdapter.clearAllData();
		}

		@Override
		protected List<ProductDTO> doInBackground(Void... arg0) {
			if(mCurrentTab==HOME_TABS.ALL){
				Bundle mParams = OrangeUtils
						.createRequestBundle(OrangeConfig.ITEMS_PAGE,null);
				return ProductModel.getInstance().getListProduct(
						UrlRequest.PRODUCT_HOME, null, mParams);
			}
			if(mCurrentTab==HOME_TABS.BEST_SELLER){
				return ProductModel.getInstance().getListProductFeatures(UrlRequest.HOME_BEST_SELLER_PRODUCT+OrangeConfig.ITEMS_PAGE, null, null);
			}
			if(mCurrentTab==HOME_TABS.POPULAR){
				return ProductModel.getInstance().getListProductFeatures(UrlRequest.HOME_POPULAR_PRODUCT+OrangeConfig.ITEMS_PAGE, null, null);
			}
			return null;			
		}

		@Override
		protected void onPostExecute(List<ProductDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				// mHomeActivity.mListItemCart = result;
				mGridView.setVisibility(View.VISIBLE);
				mProductAdapter.updateDataList(result);
				switchView(false, false);
			}else{
				switchView(true, false);
				mGridView.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ProductDTO mProduct = mProductAdapter.getItem(position);
		if (mProduct != null) {
			mHomeActivity.setCurrentProduct(mProduct);
			mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.PRODUCT_DETAIL_FRAGMENT);
		}
	}

	@Override
	public void onClick(View v) {
		switchTabViews(v.getId());
	}

	private void switchTabViews(int resId) {
		switch (resId) {
		case R.id.fragmentHomeMenuAll:
			mCurrentTab=HOME_TABS.ALL;
			switchMenuTabByViewId(R.id.fragmentHomeMenuAll);
			loadProductData();
			break;
		case R.id.fragmentHomeMenuBestSeller:
			mCurrentTab=HOME_TABS.BEST_SELLER;
			switchMenuTabByViewId(R.id.fragmentHomeMenuBestSeller);
			loadProductData();
			break;
		case R.id.fragmentHomeMenuPopular:
			mCurrentTab=HOME_TABS.POPULAR;
			switchMenuTabByViewId(R.id.fragmentHomeMenuPopular);
			loadProductData();
			break;		
		default:
			break;
		}
	}
	private void switchTabViewsCurrentTabs(HOME_TABS tabIndex) {
		if(tabIndex==HOME_TABS.ALL)
		{
			mCurrentTab=HOME_TABS.ALL;
			switchMenuTabByViewId(R.id.fragmentHomeMenuAll);
			loadProductData();
			return;
		}
		if(tabIndex==HOME_TABS.BEST_SELLER)
		{
			mCurrentTab=HOME_TABS.BEST_SELLER;
			switchMenuTabByViewId(R.id.fragmentHomeMenuBestSeller);
			loadProductData();
			return;
		}
		if(tabIndex==HOME_TABS.POPULAR)
		{
			mCurrentTab=HOME_TABS.POPULAR;
			switchMenuTabByViewId(R.id.fragmentHomeMenuPopular);
			loadProductData();
			return;
		}		
	}
	public void switchMenuTabByViewId(int viewId) {
		switch (viewId) {
		case R.id.fragmentHomeMenuAll:
			mMenuAll.setTextColor(getActivity().getResources().getColor(R.color.black));
			mMenuBestSeller.setTextColor(getActivity().getResources().getColor(R.color.gray));
			mMenuPopular.setTextColor(getActivity().getResources().getColor(R.color.gray));
//			mMenuAll.setBackgroundResource(R.drawable.item_menu_home_fragment_active);
//			mMenuBestSeller
//					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
//			mMenuPopular
//					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			break;
		case R.id.fragmentHomeMenuBestSeller:
			mMenuAll.setTextColor(getActivity().getResources().getColor(R.color.gray));
			mMenuBestSeller.setTextColor(getActivity().getResources().getColor(R.color.black));
			mMenuPopular.setTextColor(getActivity().getResources().getColor(R.color.gray));
//			mMenuAll.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
//			mMenuBestSeller
//					.setBackgroundResource(R.drawable.item_menu_home_fragment_active);
//			mMenuPopular
//					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			break;
		case R.id.fragmentHomeMenuPopular:
			mMenuAll.setTextColor(getActivity().getResources().getColor(R.color.gray));
			mMenuBestSeller.setTextColor(getActivity().getResources().getColor(R.color.gray));
			mMenuPopular.setTextColor(getActivity().getResources().getColor(R.color.black));
//			mMenuAll.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
//			mMenuBestSeller
//					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
//			mMenuPopular
//					.setBackgroundResource(R.drawable.item_menu_home_fragment_active);
			break;		
		default:
			break;
		}
	}	
	
}
