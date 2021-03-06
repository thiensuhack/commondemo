package com.orange.studio.bobo.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.AddressesAdapter;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.AddressDTO;

public class SelectAddressShoppingCartFragment extends BaseFragment implements OnClickListener, OnItemSelectedListener{
	private Spinner mListAddress=null;
	private Button mConfirmBtn=null;
	private Button mCreateAddressBtn=null;
	private AddressesAdapter mAdapter=null;
	
	private GetListAddressTask mGetListAddressTask=null;
	
//	private AddressDTO mCurrentAddress=null;
//	private CarrierDTO mCurrentCarrier=null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_select_address, container,
					false);
			initView();
			initListener();
		} else {
			if((ViewGroup) mView.getParent() != null){
				((ViewGroup) mView.getParent()).removeView(mView);
			}
		}
		return mView;
	}
	private void initView(){
		mHomeActivity=getHomeActivity();
		mListAddress=(Spinner)mView.findViewById(R.id.listAddress);
		
		mConfirmBtn=(Button)mView.findViewById(R.id.confirmBtn);
		mConfirmBtn.setVisibility(View.GONE);
		mCreateAddressBtn=(Button)mView.findViewById(R.id.createAddressBtn);
		
		mAdapter=new AddressesAdapter(mHomeActivity);
		mListAddress.setAdapter(mAdapter);
		
		
		initLoadingView();
		initNotFoundView();
	}
	private void initListener(){
		mListAddress.setOnItemSelectedListener(this);
		mConfirmBtn.setOnClickListener(this);
		mCreateAddressBtn.setOnClickListener(this);
	}
	private void loadAddresses(){
		if(mGetListAddressTask==null || mGetListAddressTask.getStatus()==Status.FINISHED){
			mGetListAddressTask=new GetListAddressTask();
			mGetListAddressTask.execute();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirmBtn:
			mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.SELECT_CARRIER);
			break;
		case R.id.createAddressBtn:
			goToCreateAddressFragment();
			break;
		default:
			break;
		}
	}
	private void goToCreateAddressFragment() {
		mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.CREATE_ADDRESS);
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,
			long arg3) {
		try {
			switch (arg0.getId()) {
			case R.id.listAddress:
				//mCurrentAddress =mAdapter.getItem(position);
				mHomeActivity.setAddress(mAdapter.getItem(position));
				break;
			default:
				break;
			}			
			//mHomeActivity.showToast("Address:"+mCurrentAddress.alias+"-ID:" + mCurrentAddress.id);
		} catch (Exception e) {
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	class GetListAddressTask extends AsyncTask<Void, Void, List<AddressDTO>>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
		}
		@Override
		protected List<AddressDTO> doInBackground(Void... params) {
			try {
				return CommonModel.getInstance().getListAddress(UrlRequest.GET_USER_ADDRESS+ mHomeActivity.getUserInfo().id);
				//return CommonModel.getInstance().getListAddress(UrlRequest.GET_USER_ADDRESS+ "73");
			} catch (Exception e) {
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<AddressDTO> result) {
			super.onPostExecute(result);
			if(result!=null && result.size()>0){
				mAdapter.updateDataList(result);				
				//switchView(false, false);
				mConfirmBtn.setVisibility(View.VISIBLE);
				switchView(false, false);
			}
			else{
				mConfirmBtn.setVisibility(View.GONE);
				switchView(false, false);
				goToCreateAddressFragment();//go 2 page create address if have no address
			}
		}
		
	}
	@Override
	public void onResume() {
		super.onResume();
		loadAddresses();
	}
}
