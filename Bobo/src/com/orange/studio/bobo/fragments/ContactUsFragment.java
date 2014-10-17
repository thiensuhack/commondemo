package com.orange.studio.bobo.fragments;

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
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.ContactUsDTO;

public class ContactUsFragment extends BaseFragment implements OnClickListener{
	private EditText mName=null;
	private EditText mFrom=null;
	private EditText mMessage=null;
	private Button mSendBtn=null;
	private SendContactUsTask mSendContactUsTask=null;
	private ContactUsDTO mContact=null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_contact_us, container,
					false);
			initView();
			initListener();
		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}
	private void initView(){
		mHomeActivity=getHomeActivity();
		mName=(EditText)mView.findViewById(R.id.nameContact);
		mFrom=(EditText)mView.findViewById(R.id.fromContact);
		mMessage=(EditText)mView.findViewById(R.id.messageContact);
		mSendBtn=(Button)mView.findViewById(R.id.sendContactBtn);
	}
	private void initListener(){
		mSendBtn.setOnClickListener(this);
	}
	private void sendContactUs(){
		if(mSendContactUsTask==null || mSendContactUsTask.getStatus()==Status.FINISHED){
			mSendContactUsTask=new SendContactUsTask();
			mSendContactUsTask.execute();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendContactBtn:
			mContact=new ContactUsDTO();
			mContact.name=mName.getText().toString().trim();
			mContact.from=mFrom.getText().toString().trim();
			mContact.message=mMessage.getText().toString().trim();
			if (mContact.name.length() < 1) {
				mHomeActivity.showToast(getActivity().getString(R.string.empty_field));			
				mName.setFocusable(true);
				return;
			}
			if (mContact.from.length() < 1) {
				mHomeActivity.showToast(getActivity().getString(R.string.empty_field));			
				mFrom.setFocusable(true);
				return;
			}
			if (mContact.message.length() < 1) {
				mHomeActivity.showToast(getActivity().getString(R.string.empty_field));			
				mMessage.setFocusable(true);
				return;
			}
			sendContactUs();
			break;

		default:
			break;
		}	
	}
	@Override
	public void onResume() {
		super.onResume();
		sendContactUs();
	}
	class SendContactUsTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			CommonModel.getInstance().sendContactUs(UrlRequest.CONTACT_US_URL, mContact);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}
