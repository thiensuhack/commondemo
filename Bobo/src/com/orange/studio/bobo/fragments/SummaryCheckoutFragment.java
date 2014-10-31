package com.orange.studio.bobo.fragments;

import android.annotation.SuppressLint;
import android.graphics.LinearGradient;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.AboutUsDTO;
import com.orange.studio.bobo.objects.SummaryDTO;

public class SummaryCheckoutFragment extends BaseFragment {
	private View mSummaryContainer=null;
	private WebView mWebView=null;
	private GetSummaryTask mGetSummaryTask=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_summary, container,
					false);
			initView();
			initListener();
		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initView(){
		mHomeActivity=getHomeActivity();
		mSummaryContainer=(LinearLayout)mView.findViewById(R.id.summaryContainer);
		mWebView=(WebView)mView.findViewById(R.id.mSummaryWebView);
		WebSettings mSetting=mWebView.getSettings();
		mSetting.setJavaScriptEnabled(true);	
		initNotFoundView();
		initLoadingView();
		switchView(false, false);
		mSummaryContainer.setVisibility(View.GONE);
	}
	private void initListener(){
		
	}
	private void loadSummary(){
		if(mGetSummaryTask==null || mGetSummaryTask.getStatus()==Status.FINISHED){
			mGetSummaryTask=new GetSummaryTask();
			mGetSummaryTask.execute();
		}		
	}
	@Override
	public void onResume() {
		super.onResume();
		loadSummary();
	}
	class GetSummaryTask extends AsyncTask<Void, Void, SummaryDTO>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
			mSummaryContainer.setVisibility(View.GONE);
		}
		@Override
		protected SummaryDTO doInBackground(Void... arg0) {
			try {				
				return CommonModel.getInstance().getSummary(UrlRequest.GET_SUMMARY_URL+mHomeActivity.getCurItemCart().id);
			} catch (Exception e) {
			}
			return null;
		}
		@Override
		protected void onPostExecute(SummaryDTO result) {
			super.onPostExecute(result);
			if(result!=null){
				String htmlData="<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head><body>";
//				htmlData+="<div>"+"Country:" + result.delivery.country+"</div>";
//				htmlData+="<div>"+"City"+ result.delivery.city+"</div>";
//				htmlData+="<div>"+"Address:"+ result.delivery.address1+"</div>";
				htmlData+="<div>"+"Total price:"+ String.valueOf(result.total_price)+"</div>";
				htmlData+="<div>"+"Total tax:"+ String.valueOf(result.total_tax)+"</div>";
				htmlData+="<div>"+"Total price without tax:"+ String.valueOf(result.total_price_without_tax)+"</div>";
				htmlData+="</body>";
				mWebView.loadData(htmlData, "text/html; charset=UTF-8", null);
				mSummaryContainer.setVisibility(View.VISIBLE);
				switchView(false, false);
			}else{
				switchView(true, false);
				mSummaryContainer.setVisibility(View.GONE);
			}
		}
		
	}
}
