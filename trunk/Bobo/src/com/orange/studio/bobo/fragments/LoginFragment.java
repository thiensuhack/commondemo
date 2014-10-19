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
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.CustomerDTO;
import com.orange.studio.bobo.utils.OrangeUtils;

public class LoginFragment extends BaseFragment implements OnClickListener{
	private EditText mEmail = null;
	private EditText mPassword = null;
	private Button mLoginBtn = null;
	private TextView mRegisterBtn=null;
	
	private ProgressDialog mProgress=null;
	private LoginTask mLoginTask=null;
	private String strEmail=null;
	private String strPassword=null;
	private HomeActivity mHomeActivity=null;
	
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
		mHomeActivity=getHomeActivity();
		
		mEmail = (EditText) mView.findViewById(R.id.nameLogin);
		mPassword = (EditText) mView.findViewById(R.id.passwordLogin);
		mLoginBtn=(Button)mView.findViewById(R.id.loginBtn);
		mProgress=new ProgressDialog(getActivity());
		mProgress.setMessage(getActivity().getString(R.string.waitting_login_message));
		mRegisterBtn=(TextView)mView.findViewById(R.id.registerLoginBtn);
	}
	private void initListener(){
		mLoginBtn.setOnClickListener(this);
		mRegisterBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			strEmail=mEmail.getText().toString().trim();
			strPassword=mPassword.getText().toString().trim();
			if(strEmail==null || strEmail.length()<1 || strPassword==null || strPassword.length()<1){
				mHomeActivity.showToast(getActivity().getString(R.string.empty_field));			
				return;
			}
			if(!OrangeUtils.validateEmail(strEmail)){
				mHomeActivity.showToast(getActivity().getString(R.string.email_not_correct));
				return;
			}
			login();
			break;
		case R.id.registerLoginBtn:
			mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.REGISTER_FRAGMENT);
			break;
		default:
			break;
		}
	}
	private void login(){
		if(mLoginTask==null || mLoginTask.getStatus()==Status.FINISHED){
			mLoginTask=new LoginTask();
			mLoginTask.execute();
		}
	}
	private class LoginTask extends AsyncTask<Void, Void, CustomerDTO>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress.show();
		}
		@Override
		protected CustomerDTO doInBackground(Void... params) {
			try {
				//filter[email]=akudanh@gmail.com
				//&filter[passwd]=ebfa0526d403f010d3ddf82ef51c2006
				//&display=full&ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT&output_format=JSON
				String passMD5=OrangeUtils.md5(OrangeConfig.App_Private_Key+strPassword);
				if(passMD5!=null && passMD5.length()==32){
					String url=UrlRequest.LOGN_URL+"?filter[email]="+strEmail;
					url+="&filter[passwd]="+passMD5;
					url+="&display=full&ws_key=LW6TL3P7Z7KRFM3UYKWHJ3N28GEZLRBT&output_format=JSON";
					return CommonModel.getInstance().loginUser(url);
				}
			} catch (Exception e) {
			}
			return null;
		}
		@Override
		protected void onPostExecute(CustomerDTO result) {
			super.onPostExecute(result);
			if(result!=null && result.id!=null && result.id.trim().length()>0){
				mHomeActivity.showToast("DONE");
				mHomeActivity.onBackPressed();
			}else{
				mHomeActivity.showToast(mHomeActivity.getString(R.string.login_failed_message));
			}
			if(mProgress.isShowing()){
				mProgress.dismiss();
			}
		}
	}
}
