package com.orange.studio.bobo.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.orange.studio.bobo.R;

public class ContactUsFragment extends BaseFragment {
	private WebView mWebView=null;
	private String mAboutUrl="http://www.bobo-u.com/en/contact-us";
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
	}
	private void initListener(){
		
	}
	private void loadAboutInfo(){
		mWebView.loadUrl(mAboutUrl);
	}
	@Override
	public void onResume() {
		super.onResume();
		loadAboutInfo();
	}
}
