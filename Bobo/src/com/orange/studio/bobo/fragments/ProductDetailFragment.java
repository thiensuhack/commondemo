package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;
import com.orange.studio.bobo.adapters.ProductDetailImageSlider;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.customviews.ColorHorizontalView;
import com.orange.studio.bobo.customviews.ColorHorizontalView.OnTabReselectedListener;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.models.ProductModel;
import com.orange.studio.bobo.objects.ColorDTO;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.objects.ProductFeatureAndValueDTO;
import com.orange.studio.bobo.objects.ProductOptionValueDTO;
import com.orange.studio.bobo.objects.RequestDTO;
import com.orange.studio.bobo.objects.StockDTO;
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
	private WebView mDataSheet = null;
	private Button mAddToCardBtn=null;
	private TextView mProDetailActiveColor = null;
	private TextView mSaleOffIcon=null;
	private TextView mStockItem=null;
	
	private ProductDTO mProduct = null;
	
	private ColorHorizontalView mColorHorizontalView=null;
	private OnTabReselectedListener mOnTabReselectedListener=null;
	private HomeActivity mHomeActivity=null;
	private CheckColorStockAvailableTask mCheckColorStockAvailableTask=null;
	private ProgressDialog mProgressDialog=null;
	private StockDTO mStock=null;
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
		mDataSheet=(WebView)mView.findViewById(R.id.dataSheet);
		mAddToCardBtn=(Button)mView.findViewById(R.id.addToCardBtn);
		
		mColorHorizontalView=(ColorHorizontalView)mView.findViewById(R.id.proDetailColorView);
		mProDetailActiveColor=(TextView)mView.findViewById(R.id.proDetailActiveColor);
		
		mSaleOffIcon=(TextView)mView.findViewById(R.id.proSaleOff);
		mStockItem=(TextView)mView.findViewById(R.id.stockItems);
		
		mHomeActivity=getHomeActivity();
		mProgressDialog=new ProgressDialog(mHomeActivity);
		mProgressDialog.setMessage(mHomeActivity.getString(R.string.detail_checking_color_stock));
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
					checkColorAvailable(color);
				} catch (Exception e) {
				}
			}
		};
		mColorHorizontalView.setOnTabReselectedListener(mOnTabReselectedListener);
	}
	private void checkColorAvailable(ColorDTO color){
		if(mCheckColorStockAvailableTask==null || mCheckColorStockAvailableTask.getStatus()==Status.FINISHED){
			mCheckColorStockAvailableTask=new CheckColorStockAvailableTask(color);
			mCheckColorStockAvailableTask.execute();
		}
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

	private class CheckColorStockAvailableTask extends AsyncTask<Void, Void, StockDTO>{
		
		private ColorDTO mColor;
		public CheckColorStockAvailableTask(ColorDTO color){
			mColor=color;
		}
		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
			if(!mProgressDialog.isShowing()){
				mProgressDialog.show();
			}
		}
		@Override
		protected StockDTO doInBackground(Void... params) {
			try {
				StockDTO temp = mProduct.mListStock.get(mColor.index+1);
				return CommonModel.getInstance().getStock(temp.linkHref+"?ws_key="+OrangeConfig.App_Key);
//				if(mStock=null)
//				return CommonModel.getInstance().getColorStockAvailable(String.format(UrlRequest.PRODUCT_COLOR_ITEM_STOCK, mProduct.id,temp.id_product_attribute));
			} catch (Exception e) {
				
			}
			return null;
		}
		@Override
		protected void onPostExecute(StockDTO result) {
			super.onPostExecute(result);
			mStock=result;
			if(mStock!=null){
//				mHomeActivity.showToast(result);
				if(mStock!=null && mStock.quantity>0){
					mStockItem.setText(String.valueOf(mStock.quantity));
					mStockItem.setVisibility(View.VISIBLE);
				}else{
					mStockItem.setVisibility(View.INVISIBLE);
				}
			}else{
				mStockItem.setVisibility(View.INVISIBLE);
			}
			if(mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
			}
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
				mProPrice.setText("$"+String.format("%,.2f", mProduct.price));
				mProPriceDiscount.setText("$"+String.format("%,.2f", mProduct.wholesale_price));
				mProCondition.setText(mProduct.condition);
				mProModel.setText(mProduct.reference);
				if(mProduct.listProductFeatures!=null && mProduct.listProductFeatures.size()>0){
					//String dataSheet="<body><table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-bottom: 1px solid #d6d4d4;width: 100%;background: white;\"><tbody>";
					String dataSheet="<body><table style=\"background: white;\"><tbody>";
					for (ProductFeatureAndValueDTO item : mProduct.listProductFeatures) {
						dataSheet+="<tr style=\"border-top: 1px solid #d6d4d4; padding:0px;\"><td style=\"padding: 5px 20px 5px 0px;border-right: 1px solid #d6d4d4;width: 30%;font-weight: 900;color: #333333;\">"+item.feature_title+"</td><td style=\"text-align: left;font-weight: normal;vertical-align: middle;padding: 5px 20px 5px 20px;\">"+item.feature_value+"</td></tr>";
					}
					dataSheet+="</tbody></table></body>";
					mDataSheet.loadData(dataSheet,"text/html", "utf-8");
				}else{
					mView.findViewById(R.id.dataSheetLabel).setVisibility(View.GONE);
					mDataSheet.setVisibility(View.GONE);
				}
				String shortDescriptions="<body style='text-align: justify;font-size:17px;padding:0px;'>"+mProduct.description_short+"</body>";
				String descriptions="<body style='text-align: justify;font-size:17px;padding:0px;'>"+mProduct.description+"</body>";
				//mProPriceDescription.setText(Html.fromHtml(descriptions));
				mProShortDescription.loadData(shortDescriptions,"text/html", "utf-8");
				mProMoreInfo.loadData(descriptions,"text/html", "utf-8");
				mSaleOffIcon.setText(String.valueOf(result.wholesale_price));
//				if(result.stock!=null && result.stock.quantity>0){
//					mStockItem.setText(String.valueOf(result.stock.quantity));
//					mStockItem.setVisibility(View.VISIBLE);
//				}else{
//					mStockItem.setVisibility(View.INVISIBLE);
//				}
				if(result.wholesale_price>0){					
					mSaleOffIcon.setVisibility(View.VISIBLE);
				}else{
					mSaleOffIcon.setVisibility(View.INVISIBLE);
				}
				if(mProduct.associations.images.size()>0){
					mSilderAdapter = new ProductDetailImageSlider(mHomeActivity,
							mProduct.associations.images);
					mViewPager.setAdapter(mSilderAdapter);
					mCirclePageIndicator.setViewPager(mViewPager);
				}	
				
				if(result.listProductOptionValues!=null && result.listProductOptionValues.size()>0){
					List<ColorDTO> listColor=new ArrayList<ColorDTO>();
					ColorDTO color=null;
					int index=0;
					for (ProductOptionValueDTO item : result.listProductOptionValues) {						
						color=new ColorDTO();
						color.index=index;
						color.color=item.color;
						color.id=item.id;
						listColor.add(color);
						index++;
					}
					mColorHorizontalView.updateView(listColor);
				}								
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
