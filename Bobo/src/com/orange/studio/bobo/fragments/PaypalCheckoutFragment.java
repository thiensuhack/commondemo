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

public class PaypalCheckoutFragment extends BaseFragment implements OnClickListener{
	private Button mPaypalBtn=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_paypal_checkout, container,
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
		mPaypalBtn=(Button)mView.findViewById(R.id.paypalCheckoutBtn);
	}
	private void initListener(){
		mPaypalBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.paypalCheckoutBtn:
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
		}		
	}			
}
