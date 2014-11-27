package com.orange.studio.bobo.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.CustomerDTO;
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
	private ProgressDialog mProgressWaitting=null;
	private RegisterTask mRegisterTask=null;
	private HomeActivity mHomeActivity=null;
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
		mHomeActivity=getHomeActivity();
		mFirstName = (EditText) mView.findViewById(R.id.firstNameRegister);
		mLastName = (EditText) mView.findViewById(R.id.lastNameRegister);
		mEmail = (EditText) mView.findViewById(R.id.emailRegister);
		mPassword = (EditText) mView.findViewById(R.id.passwordRegister);
		mConfirmPassword = (EditText) mView
				.findViewById(R.id.confirmPasswordRegister);
		mRegisterBtn = (Button) mView.findViewById(R.id.registerBtn);

		mRegisterInfo = new RegisterDTO();
		mProgressWaitting=new ProgressDialog(getActivity());
		mProgressWaitting.setMessage(getActivity().getString(R.string.waitting_register_message));
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
			mHomeActivity.showToast(getActivity().getString(R.string.empty_field));			
			mFirstName.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.lastName.length() < 1) {
			mHomeActivity.showToast(getActivity().getString(R.string.empty_field));
			mLastName.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.email.length() < 1) {
			mHomeActivity.showToast(getActivity().getString(R.string.empty_field));
			mEmail.setFocusable(true);
			return false;
		}
		if(!OrangeUtils.validateEmail(mRegisterInfo.email)){
			mHomeActivity.showToast(getActivity().getString(R.string.email_not_correct));
			mEmail.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.password.length() < 1) {
			mHomeActivity.showToast(getActivity().getString(R.string.empty_field));
			mPassword.setFocusable(true);
			return false;
		}
		if (mRegisterInfo.confirmPassword.length() < 1) {
			mHomeActivity.showToast(getActivity().getString(R.string.empty_field));
			mConfirmPassword.setFocusable(true);
			return false;
		}
		if (!mRegisterInfo.password.equals(mRegisterInfo.confirmPassword)) {
			mHomeActivity.showToast(getActivity().getString(R.string.password_not_matched));
			mPassword.setFocusable(true);
			return false;
		}
		//mHomeActivity.showToast("PASSED");
		/*<?xml version="1.0" encoding="UTF-8"?>
		<prestashop xmlns:xlink="http://www.w3.org/1999/xlink">
		<customer>
			<passwd>123456</passwd>
			<lastname>Danh</lastname>	
			<firstname>Vo</firstname>
			<email>abc123@gmail.com</email>
		    <active>1</active>
		</customer>
		</prestashop>
		*/
		String regisData="<?xml version=\"1.0\" encoding=\"UTF-8\"?><prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\"><customer>";
		regisData+="<passwd>"+mRegisterInfo.password+"</passwd>";
		regisData+="<lastname>"+mRegisterInfo.lastName+"</lastname>";	
		regisData+="<firstname>"+mRegisterInfo.firstName+"</firstname>";
		regisData+="<email>"+mRegisterInfo.email+"</email>";
		regisData+="<id_default_group>3</id_default_group>";
		regisData+="<active>1</active>";
		regisData+="<associations><groups node_type=\"group\">";
		regisData+="<group><id><![CDATA[1]]></id></group>";
		regisData+="<group><id><![CDATA[2]]></id></group>";
		regisData+="<group><id><![CDATA[3]]></id></group>";
		regisData+="</groups></associations>";
		regisData+="</customer></prestashop>";
		registerNow(regisData);
		return true;
	}

	private void registerNow(String regisData) {
		if(mRegisterTask==null || mRegisterTask.getStatus()==Status.FINISHED){
			mRegisterTask=new RegisterTask(regisData);
			mRegisterTask.execute();
		}
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
	private void resetView(){
		mFirstName.setText("");
		mLastName.setText("");
		mEmail.setText("");
		mPassword.setText("");
		mConfirmPassword.setText("");
	}
	class RegisterTask extends AsyncTask<Void, Void, CustomerDTO>{
		String regisData="";
		public RegisterTask(String _regisData){
			regisData=_regisData;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressWaitting.show();
		}
		@Override
		protected CustomerDTO doInBackground(Void... arg0) {
			String url=UrlRequest.REGISTER+"&ws_key="+OrangeConfig.App_Key;
			return CommonModel.getInstance().registerUser(url, regisData);
		}
		@Override
		protected void onPostExecute(CustomerDTO result) {
			super.onPostExecute(result);
			if(result!=null){
				if(result.id!=null && result.id.trim().length()>0){					
					mHomeActivity.showToast(mHomeActivity.getString(R.string.register_success_message));
					mHomeActivity.onBackPressed();
				}
			}else{
				mHomeActivity.showToast(mHomeActivity.getString(R.string.register_failed_message));
			}
			if(mProgressWaitting.isShowing()){
				mProgressWaitting.dismiss();
			}
		}
	}
}
