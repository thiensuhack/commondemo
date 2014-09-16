package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.ProductDetailImageSlider;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.objects.HomeSliderDTO;
import com.viewpagerindicator.CirclePageIndicator;

public class ProductDetailFragment extends BaseFragment {

	private ViewPager mViewPager = null;
	private CirclePageIndicator mCirclePageIndicator = null;

	private ProductDetailImageSlider mSilderAdapter = null;

	private LoadHomeSliderTask mLoadHomeSliderTask = null;

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
	}

	private void initListener() {

	}

	@Override
	public void onResume() {
		super.onResume();
		loadProductDetailSliderData();
	}

	private void loadProductDetailSliderData() {
		if (mLoadHomeSliderTask == null
				|| mLoadHomeSliderTask.getStatus() == Status.FINISHED) {
			mLoadHomeSliderTask = new LoadHomeSliderTask();
			mLoadHomeSliderTask.execute();
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
				mSilderAdapter = new ProductDetailImageSlider(getActivity(),
						result);
				mViewPager.setAdapter(mSilderAdapter);
				mCirclePageIndicator.setViewPager(mViewPager);
			}
		}
	}

}
