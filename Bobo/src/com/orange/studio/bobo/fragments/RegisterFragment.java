package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.objects.RegisterDTO;
import com.orange.studio.bobo.utils.OrangeUtils;

public class RegisterFragment extends BaseFragment implements OnClickListener {
	private EditText mFirstName = null;
	private EditText mLastName = null;
	private EditText mEmail = null;
	private EditText mPassword = null;
	private EditText mConfirmPassword = null;
	private Button mRegisterBtn = null;
	private RegisterDTO mRegisterInfo = null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_register, container,
					false);
			initView();
			initListener();

		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}

	private void initView() {
		mFirstName = (EditText) mView.findViewById(R.id.firstNameRegister);
		mLastName = (EditText) mView.findViewById(R.id.lastNameRegister);
		mEmail = (EditText) mView.findViewById(R.id.emailRegister);
		mPassword = (EditText) mView.findViewById(R.id.passwordRegister);
		mConfirmPassword = (EditText) mView
				.findViewById(R.id.confirmPasswordRegister);
		mRegisterBtn = (Button) mView.findViewById(R.id.registerBtn);

		mRegisterInfo = new RegisterDTO();
	}

	private void initListener() {
		mRegisterBtn.setOnClickListener(this);
	}

	private boolean validateInfoSubmit() {
		mRegisterInfo.firstName = mFirstName.getText().toString().trim();
		mRegisterInfo.lastName = mLastName.getText().toString().trim();
		mRegisterInfo.email = mEmail.getText().toString().trim();
		mRegisterInfo.password = mPassword.getText().toString().trim();
		mRegisterInfo.confirmPassword = mConfirmPassword.getText().toString()
				.trim();
		if (mRegisterInfo.firstName.length() < 1) {
			getHomeActivity().showToast(getActivity().getString(R.string.empty_field));			
			mFirstName.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.lastName.length() < 1) {
			getHomeActivity().showToast(getActivity().getString(R.string.empty_field));
			mLastName.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.email.length() < 1) {
			getHomeActivity().showToast(getActivity().getString(R.string.empty_field));
			mEmail.setFocusable(true);
			return false;
		}
		if(!OrangeUtils.validateEmail(mRegisterInfo.email)){
			getHomeActivity().showToast(getActivity().getString(R.string.email_not_correct));
			mEmail.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.password.length() < 1) {
			getHomeActivity().showToast(getActivity().getString(R.string.empty_field));
			mPassword.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.confirmPassword.length() < 1) {
			getHomeActivity().showToast(getActivity().getString(R.string.empty_field));
			mConfirmPassword.setFocusable(true);
			return false;
		}
		if (!mRegisterInfo.password.equals(mRegisterInfo.confirmPassword)) {
			getHomeActivity().showToast(getActivity().getString(R.string.password_not_matched));
			mPassword.setFocusable(true);
			return false;
		}
		getHomeActivity().showToast("PASSED");
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerBtn:
			validateInfoSubmit();
			break;

		default:
			break;
		}
	}
}
