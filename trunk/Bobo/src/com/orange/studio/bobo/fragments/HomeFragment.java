package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.GridProductAdapter;
import com.orange.studio.bobo.configs.OrangeConfig;
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
	private HorizontalScrollView mMenuHomeScrollView = null;

	private View mMenuAll = null;
	private View mMenuBestSeller = null;
	private View mMenuFeaturedProducts = null;
	private View mMenuFeaturedPopular = null;
//	private View mMenuFeaturedProducts3 = null;
	private View mPreviousBtn = null;
	private View mNextBtn = null;

	private int mCurrentTab = 1;

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
		mMenuFeaturedProducts = (TextView) mView
				.findViewById(R.id.fragmentHomeMenuFeaturedProducts);
		mMenuFeaturedPopular = (TextView) mView
				.findViewById(R.id.fragmentHomeMenuFeaturedPopular);		
		mPreviousBtn = (ImageView) mView
				.findViewById(R.id.fragmentHomeMenuPrevious);
		mNextBtn = (ImageView) mView.findViewById(R.id.fragmentHomeMenuNext);

		mMenuHomeScrollView = (HorizontalScrollView) mView
				.findViewById(R.id.menuHomeScrollView);
		mMenuHomeScrollView.setHorizontalScrollBarEnabled(false);
	}

	private void initListener() {
		mGridView.setOnItemClickListener(this);
		mMenuAll.setOnClickListener(this);
		mMenuBestSeller.setOnClickListener(this);
		mMenuFeaturedProducts.setOnClickListener(this);
		mMenuFeaturedPopular.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
		mPreviousBtn.setOnClickListener(this);
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
		loadHomeSliderData();
		// if(mProductAdapter==null || mProductAdapter.getCount()<1){
		// loadProductData();
		// }
		loadProductData();
		getHomeActivity().updateItemCartCounter();
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
			for (int i = 0; i < 10; i++) {
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

		}

		@Override
		protected List<ProductDTO> doInBackground(Void... arg0) {
			Bundle mParams = OrangeUtils
					.createRequestBundle(OrangeConfig.ITEMS_PAGE,null);
			return ProductModel.getInstance().getListProduct(
					UrlRequest.PRODUCT_HOME, null, mParams);
		}

		@Override
		protected void onPostExecute(List<ProductDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				// getHomeActivity().mListItemCart = result;
				mProductAdapter.updateDataList(result);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ProductDTO mProduct = mProductAdapter.getItem(position);
		if (mProduct != null) {
			getHomeActivity().setCurrentProduct(mProduct);
			getHomeActivity().onNavigationDrawerItemSelected(-11);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragmentHomeMenuAll:
			mCurrentTab=1;
			switchMenuTabByViewId(R.id.fragmentHomeMenuAll);
			loadProductData();
			break;
		case R.id.fragmentHomeMenuBestSeller:
			mCurrentTab=2;
			switchMenuTabByViewId(R.id.fragmentHomeMenuBestSeller);
			loadProductData();
			break;
		case R.id.fragmentHomeMenuFeaturedProducts:
			mCurrentTab=3;
			switchMenuTabByViewId(R.id.fragmentHomeMenuFeaturedProducts);
			loadProductData();
			break;
		case R.id.fragmentHomeMenuFeaturedPopular:
			mCurrentTab=4;
			switchMenuTabByViewId(R.id.fragmentHomeMenuFeaturedPopular);
			loadProductData();
			break;
		case R.id.fragmentHomeMenuNext:
			if(mCurrentTab==4){
				return;
			}
			mCurrentTab++;
			if (mCurrentTab > 4) {
				mCurrentTab = 4;
			}
			switchMenuTabByIndex(mCurrentTab, true);
			loadProductData();
			break;
		case R.id.fragmentHomeMenuPrevious:
			if(mCurrentTab==1){
				return;
			}
			mCurrentTab--;
			if (mCurrentTab < 1) {
				mCurrentTab = 1;
			}
			switchMenuTabByIndex(mCurrentTab, false);
			loadProductData();
			break;
		default:
			break;
		}
	}
	public void switchMenuTabByViewId(int viewId) {
		switch (viewId) {
		case R.id.fragmentHomeMenuAll:
			mMenuAll.setBackgroundResource(R.drawable.item_menu_home_fragment_active);
			mMenuBestSeller
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuFeaturedProducts
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuFeaturedPopular
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			break;
		case R.id.fragmentHomeMenuBestSeller:
			mMenuAll.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuBestSeller
					.setBackgroundResource(R.drawable.item_menu_home_fragment_active);
			mMenuFeaturedProducts
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuFeaturedPopular
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			break;
		case R.id.fragmentHomeMenuFeaturedProducts:
			mMenuAll.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuBestSeller
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuFeaturedProducts
					.setBackgroundResource(R.drawable.item_menu_home_fragment_active);
			mMenuFeaturedPopular
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			break;
		case R.id.fragmentHomeMenuFeaturedPopular:
			mMenuAll.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuBestSeller
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuFeaturedProducts
					.setBackgroundResource(R.drawable.item_menu_home_fragment_normal);
			mMenuFeaturedPopular
					.setBackgroundResource(R.drawable.item_menu_home_fragment_active);
			break;
		default:
			break;
		}
	}
	private void switchMenuTabByIndex(int tabIndex, boolean isNext) {
		int mVx = 0;
		switch (tabIndex) {
		case 1:
			switchMenuTabByViewId(R.id.fragmentHomeMenuAll);
			mVx = mMenuHomeScrollView.getScrollX();
			break;
		case 2:
			switchMenuTabByViewId(R.id.fragmentHomeMenuBestSeller);
			mVx = mMenuBestSeller.getWidth();
			break;
		case 3:
			switchMenuTabByViewId(R.id.fragmentHomeMenuFeaturedProducts);
			mVx = mMenuFeaturedProducts.getWidth();
			break;
		case 4:
			switchMenuTabByViewId(R.id.fragmentHomeMenuFeaturedPopular);
			mVx = mMenuFeaturedPopular.getWidth();
			break;
		default:
			break;
		}
		if (!isNext) {
			mVx = mVx * (-1);
		}
		final int tempVx = mVx;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				mMenuHomeScrollView.smoothScrollTo(
						(int) mMenuHomeScrollView.getScrollX() + tempVx,
						(int) mMenuHomeScrollView.getScrollY());
			}
		}, 100L);
	}
}
