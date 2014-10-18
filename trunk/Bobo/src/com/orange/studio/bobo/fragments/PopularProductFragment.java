package com.orange.studio.bobo.fragments;

import java.util.List;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
import com.orange.studio.bobo.objects.ProductDTO;

public class PopularProductFragment extends BaseFragment implements OnItemClickListener,
		OnClickListener {


	private ExpandableHeightGridView mGridView = null;
	private GridProductAdapter mProductAdapter = null;
	private LoadProductsTask mLoadProductsTask = null;
	public HOME_TABS mCurrentTab = HOME_TABS.BEST_SELLER;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_product_category, container, false);
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

		mGridView = (ExpandableHeightGridView) mView
				.findViewById(R.id.productCategoryGridView);
		//mGridView.setExpanded(true);
		mProductAdapter = new GridProductAdapter(getActivity());
		mGridView.setAdapter(mProductAdapter);
		initNotFoundView();
		initLoadingView();
	}

	private void initListener() {
		mGridView.setOnItemClickListener(this);
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
		loadProductData();		
	}
	public void setCurrentTab(HOME_TABS mTab){
		mCurrentTab=mTab;
	}
	private class LoadProductsTask extends
			AsyncTask<Void, Void, List<ProductDTO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
			if(mProductAdapter!=null){
				mProductAdapter.clearAllData();
			}			
		}

		@Override
		protected List<ProductDTO> doInBackground(Void... arg0) {
			try {				
				return ProductModel.getInstance().getListProductFeatures(UrlRequest.HOME_POPULAR_PRODUCT+OrangeConfig.ITEMS_PAGE, null, null);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<ProductDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				mProductAdapter.updateDataList(result);
				switchView(false, false);
			}else{
				switchView(true, false);
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
		switch (v.getId()) {		
		default:
			break;
		}
	}
	
}
