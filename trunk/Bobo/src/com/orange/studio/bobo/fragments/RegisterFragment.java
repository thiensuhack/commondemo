package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orange.studio.bobo.R;

public class RegisterFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_register, container, false);
			initView();
			initListener();

		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}
	private void initView(){

//		<passwd>123456</passwd>
//		<lastname>Danh</lastname>	
//		<firstname>Vo</firstname>
//		<email>abc123@gmail.com</email>
//	    <active>1</active>

	}
	private void initListener(){
		
	}
}
