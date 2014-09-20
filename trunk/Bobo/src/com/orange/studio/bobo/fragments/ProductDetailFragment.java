package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.ProductDetailImageSlider;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.ProductImageDTO;
import com.viewpagerindicator.CirclePageIndicator;

public class ProductDetailFragment extends BaseFragment {

	private ViewPager mViewPager = null;
	private CirclePageIndicator mCirclePageIndicator = null;

	private ProductDetailImageSlider mSilderAdapter = null;

	private LoadHomeSliderTask mLoadHomeSliderTask = null;
	private TextView mProName = null;
	private TextView mProPrice = null;
	private TextView mProPriceDiscount = null;
	private TextView mProPriceDescription = null;
	
	private ProductDTO mProduct = null;
	private List<ProductImageDTO> mHomeSlider=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_product_detail,
					container, false);
			initView();
			initListener();

		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}

	private void initView() {
		mViewPager = (ViewPager) mView
				.findViewById(R.id.viewPagerProductDetail);
		mCirclePageIndicator = (CirclePageIndicator) mView
				.findViewById(R.id.indicatorProductDetail);

		mProName = (TextView) mView.findViewById(R.id.productDetailName);
		mProPrice = (TextView) mView.findViewById(R.id.productDetailPrice);
		mProPriceDiscount = (TextView) mView
				.findViewById(R.id.productDetailPriceDiscount);
		mProPriceDescription=(TextView)mView.findViewById(R.id.productDetailDiscription);
		mProduct = getHomeActivity().getCurrentProduct();
		if (mProduct != null) {
			mProName.setText(mProduct.name);
			mProPrice.setText(String.valueOf(mProduct.price));
			mProPriceDiscount.setText(String.valueOf(mProduct.wholesale_price));
			mProPriceDescription.setText(Html.fromHtml(mProduct.description));
			if(mProduct.associations.images.size()>0){
				mSilderAdapter = new ProductDetailImageSlider(getActivity(),
						mProduct.associations.images);
				mViewPager.setAdapter(mSilderAdapter);
				mCirclePageIndicator.setViewPager(mViewPager);
			}			
		}
	}

	private void initListener() {

	}

	@Override
	public void onResume() {
		super.onResume();
		//loadProductDetailSliderData();
	}

	private void loadProductDetailSliderData() {
		if (mLoadHomeSliderTask == null
				|| mLoadHomeSliderTask.getStatus() == Status.FINISHED) {
			mLoadHomeSliderTask = new LoadHomeSliderTask();
			mLoadHomeSliderTask.execute();
		}
	}

	private class LoadHomeSliderTask extends
			AsyncTask<Void, Void, List<ProductImageDTO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<ProductImageDTO> doInBackground(Void... arg0) {
			List<ProductImageDTO> result = new ArrayList<ProductImageDTO>();
			for (int i = 0; i < 10; i++) {
				ProductImageDTO item = new ProductImageDTO();
				item.url = OrangeConfig.IMAGES[i];
				result.add(item);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<ProductImageDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				mSilderAdapter = new ProductDetailImageSlider(getActivity(),
						result);
				mViewPager.setAdapter(mSilderAdapter);
				mCirclePageIndicator.setViewPager(mViewPager);
			}
		}
	}

}
