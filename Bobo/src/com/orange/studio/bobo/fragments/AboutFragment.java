package com.orange.studio.bobo.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.AboutUsDTO;

public class AboutFragment extends BaseFragment {
	private WebView mWebView=null;
	private GetAboutUsTask mGetAboutUsTask=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_about, container,
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
		mWebView=(WebView)mView.findViewById(R.id.mAboutWebView);
		WebSettings mSetting=mWebView.getSettings();
		mSetting.setJavaScriptEnabled(true);	
		initNotFoundView();
		initLoadingView();
		switchView(false, false);
		mWebView.setVisibility(View.GONE);
	}
	private void initListener(){
		
	}
	private void loadAboutInfo(){
		if(mGetAboutUsTask==null || mGetAboutUsTask.getStatus()==Status.FINISHED){
			mGetAboutUsTask=new GetAboutUsTask();
			mGetAboutUsTask.execute();
		}		
	}
	@Override
	public void onResume() {
		super.onResume();
		loadAboutInfo();
	}
	class GetAboutUsTask extends AsyncTask<Void, Void, AboutUsDTO>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
			mWebView.setVisibility(View.GONE);
		}
		@Override
		protected AboutUsDTO doInBackground(Void... arg0) {
			return CommonModel.getInstance().getAboutUs(UrlRequest.ABOUT_US_URL);
		}
		@Override
		protected void onPostExecute(AboutUsDTO result) {
			super.onPostExecute(result);
			if(result!=null){
				String htmlData="<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head><body>"+result.content+"</body>";
				mWebView.loadData(htmlData, "text/html; charset=UTF-8", null);
				mWebView.setVisibility(View.VISIBLE);
				switchView(false, false);
			}else{
				switchView(true, false);
				mWebView.setVisibility(View.GONE);
			}
		}
		
	}
}
