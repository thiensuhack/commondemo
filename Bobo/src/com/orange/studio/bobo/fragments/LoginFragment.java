package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.orange.studio.bobo.R;

public class LoginFragment extends BaseFragment implements OnClickListener{
	private EditText mName = null;
	private EditText mPassword = null;
	private Button mLoginBtn = null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_login, container, false);
			initView();
			initListener();

		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}
	private void initView(){
		mName = (EditText) mView.findViewById(R.id.nameLogin);
		mPassword = (EditText) mView.findViewById(R.id.passwordLogin);
		mLoginBtn=(Button)mView.findViewById(R.id.loginBtn);
	}
	private void initListener(){
		mLoginBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			
			break;

		default:
			break;
		}
	}
}
