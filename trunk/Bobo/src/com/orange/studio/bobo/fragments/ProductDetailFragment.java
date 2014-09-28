package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.test.MoreAsserts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.ProductDetailImageSlider;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.customviews.ColorHorizontalView;
import com.orange.studio.bobo.customviews.ColorHorizontalView.OnTabReselectedListener;
import com.orange.studio.bobo.models.ProductModel;
import com.orange.studio.bobo.objects.ColorDTO;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
import com.viewpagerindicator.CirclePageIndicator;

public class ProductDetailFragment extends BaseFragment implements OnClickListener{

	private ViewPager mViewPager = null;
	private CirclePageIndicator mCirclePageIndicator = null;

	private ProductDetailImageSlider mSilderAdapter = null;

	private LoadProductDetailTask mLoadHomeSliderTask = null;
	private TextView mProName = null;
	private TextView mProPrice = null;
	private TextView mProPriceDiscount = null;
	private TextView mProModel = null;
	private TextView mProCondition = null;
	private WebView mProShortDescription = null;
	private WebView mProMoreInfo = null;
	private Button mAddToCardBtn=null;
	private TextView mProDetailActiveColor = null;
	
	private ProductDTO mProduct = null;
	
	private ColorHorizontalView mColorHorizontalView=null;
	private OnTabReselectedListener mOnTabReselectedListener=null;
	
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
		initLoadingView();
		initNotFoundView();
		switchView(false,true);
		mViewPager = (ViewPager) mView
				.findViewById(R.id.viewPagerProductDetail);
		mCirclePageIndicator = (CirclePageIndicator) mView
				.findViewById(R.id.indicatorProductDetail);

		mProName = (TextView) mView.findViewById(R.id.productDetailName);
		mProPrice = (TextView) mView.findViewById(R.id.productDetailPrice);
		mProPriceDiscount = (TextView) mView
				.findViewById(R.id.productDetailPriceDiscount);
		mProModel = (TextView) mView
				.findViewById(R.id.productDetailModel);
		mProCondition = (TextView) mView
				.findViewById(R.id.productDetailCondition);
		
		mProShortDescription=(WebView)mView.findViewById(R.id.productDetailDiscription);
		mProMoreInfo=(WebView)mView.findViewById(R.id.productDetailMoreInfo);
		mAddToCardBtn=(Button)mView.findViewById(R.id.addToCardBtn);
		
		mColorHorizontalView=(ColorHorizontalView)mView.findViewById(R.id.proDetailColorView);
		mProDetailActiveColor=(TextView)mView.findViewById(R.id.proDetailActiveColor);
	}

	private void showDetail() {
		mProduct = getHomeActivity().getCurrentProduct();
		loadProductDetailData();
	}

	private void initListener() {
		mAddToCardBtn.setOnClickListener(this);
		mOnTabReselectedListener=new OnTabReselectedListener() {
			
			@Override
			public void onTabReselected(ColorDTO color) {
				try {
					mProDetailActiveColor.setBackgroundColor(Color.parseColor(color.color));
				} catch (Exception e) {
				}
			}
		};
		mColorHorizontalView.setOnTabReselectedListener(mOnTabReselectedListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		getHomeActivity().updateItemCartCounter();
		showDetail();
	}

	private void loadProductDetailData() {
		if (mLoadHomeSliderTask == null
				|| mLoadHomeSliderTask.getStatus() == Status.FINISHED) {
			mLoadHomeSliderTask = new LoadProductDetailTask();
			mLoadHomeSliderTask.execute();
		}
	}

	private class LoadProductDetailTask extends
			AsyncTask<Void, Void, ProductDTO> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false,true);
		}

		@Override
		protected ProductDTO doInBackground(Void... arg0) {
			Bundle mParams = OrangeUtils
					.createRequestBundle2(null,null);
			RequestDTO request=new RequestDTO();
			request.proId=mProduct.id;
			return ProductModel.getInstance().getProductDetail(
					UrlRequest.PRODUCT_DETAIL, request, mParams);
		}

		@Override
		protected void onPostExecute(ProductDTO result) {
			super.onPostExecute(result);
			if (result != null) {
				mProduct=result;
				mProName.setText(mProduct.name);
				mProPrice.setText("$"+String.valueOf(mProduct.price));
				mProPriceDiscount.setText("$"+String.valueOf(mProduct.wholesale_price));
				String shortDescriptions="<body style='text-align: justify;font-size:17px'>"+mProduct.description_short+"</body>";
				String descriptions="<body style='text-align: justify;font-size:17px'>"+mProduct.description+"</body>";
				//mProPriceDescription.setText(Html.fromHtml(descriptions));
				mProShortDescription.loadData(shortDescriptions,"text/html", "utf-8");
				mProMoreInfo.loadData(descriptions,"text/html", "utf-8");
				if(mProduct.associations.images.size()>0){
					mSilderAdapter = new ProductDetailImageSlider(getActivity(),
							mProduct.associations.images);
					mViewPager.setAdapter(mSilderAdapter);
					mCirclePageIndicator.setViewPager(mViewPager);
				}	
				List<ColorDTO> listColor=new ArrayList<ColorDTO>();
				ColorDTO item1=new ColorDTO();
				item1.color="#D2007D";
				item1.id="1";
				
				ColorDTO item2=new ColorDTO();
				item2.color="#FF6600";
				item2.id="2";
				ColorDTO item3=new ColorDTO();
				item3.color="#74C219";
				item3.id="3";
				listColor.add(item3);
				listColor.add(item2);
				listColor.add(item1);
				listColor.add(item3);
				listColor.add(item2);
				listColor.add(item1);

				mColorHorizontalView.updateView(listColor);
				
				switchView(false,false);
			}else{
				switchView(true, false);
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addToCardBtn:
			getHomeActivity().addToCart(mProduct);
			break;

		default:
			break;
		}
	}
}
