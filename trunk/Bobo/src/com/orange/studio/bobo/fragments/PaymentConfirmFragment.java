package com.orange.studio.bobo.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.SummaryDTO;

public class PaymentConfirmFragment extends BaseFragment implements OnClickListener{
	private View mSummaryContainer=null;
	private WebView mWebView=null;
	private GetSummaryTask mGetSummaryTask=null;
	private Button mConfirmBtn=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_payment, container,
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
		mConfirmBtn=(Button)mView.findViewById(R.id.confirmBtn);
	}
	private void initListener(){
		mConfirmBtn.setOnClickListener(this);
	}
	private void loadSummary(){
		if(mGetSummaryTask==null || mGetSummaryTask.getStatus()==Status.FINISHED){
			mGetSummaryTask=new GetSummaryTask();
			mGetSummaryTask.execute();
		}		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirmBtn:
			mHomeActivity.createOrder();
			break;
		default:
			break;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(mHomeActivity.IsCheckOutSuccess()){
			mHomeActivity.setCheckOutSuccess(false);
			mHomeActivity.onNavigationDrawerItemSelected(1);
			mHomeActivity.showSuccessPaymentDilog();
		}else{
			loadSummary();
		}		
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
				Gson gs=new Gson();
				Log.i("SUMMARY: ",gs.toJson(result));
				
				mHomeActivity.setSummaryDTO(result);
				String htmlData="<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">";
				htmlData+="</head><body style=\"padding:10px;\">";
//				htmlData+="<div>"+"Country:" + result.delivery.country+"</div>";
//				htmlData+="<div>"+"City"+ result.delivery.city+"</div>";
//				htmlData+="<div>"+"Address:"+ result.delivery.address1+"</div>";
				htmlData+="<div>"+"Total price:<b> "+ String.valueOf(result.total_price)+"$</b></div>";
				htmlData+="<div>"+"Total tax:<b> "+ String.valueOf(result.total_tax)+"$</b></div>";
				htmlData+="<div>"+"Total price without tax:<b> "+ String.valueOf(result.total_price_without_tax)+"$</b></div>";
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
