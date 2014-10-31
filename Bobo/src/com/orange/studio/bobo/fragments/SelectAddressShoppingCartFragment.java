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
import com.orange.studio.bobo.adapters.CarrierAdapter;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.AddressDTO;
import com.orange.studio.bobo.objects.CarrierDTO;

public class SelectAddressShoppingCartFragment extends BaseFragment implements OnClickListener, OnItemSelectedListener{
	private Spinner mListAddress=null;
	private Spinner mListCarrier=null;
	private Button mConfirmBtn=null;
	private Button mCreateAddressBtn=null;
	private AddressesAdapter mAdapter=null;
	private CarrierAdapter mCarrierAdapter=null;
	
	private GetListAddressTask mGetListAddressTask=null;
	private GetListCarrierTask mGetListCarrierTask=null;
	
	private AddressDTO mCurrentAddress=null;
	private CarrierDTO mCurrentCarrier=null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_select_address, container,
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
		mListAddress=(Spinner)mView.findViewById(R.id.listAddress);
		mListCarrier=(Spinner)mView.findViewById(R.id.listCarrier);
		
		mConfirmBtn=(Button)mView.findViewById(R.id.confirmBtn);
		mCreateAddressBtn=(Button)mView.findViewById(R.id.createAddressBtn);
		
		mAdapter=new AddressesAdapter(mHomeActivity);
		mListAddress.setAdapter(mAdapter);
		
		mCarrierAdapter=new CarrierAdapter(mHomeActivity);
		mListCarrier.setAdapter(mCarrierAdapter);
		
		initLoadingView();
		initNotFoundView();
	}
	private void initListener(){
		mListAddress.setOnItemSelectedListener(this);
		mListCarrier.setOnItemSelectedListener(this);
		mConfirmBtn.setOnClickListener(this);
		mCreateAddressBtn.setOnClickListener(this);
	}
	private void loadAddresses(){
		if(mGetListAddressTask==null || mGetListAddressTask.getStatus()==Status.FINISHED){
			mGetListAddressTask=new GetListAddressTask();
			mGetListAddressTask.execute();
		}
	}
	private void loadCarrier(){
		if(mGetListCarrierTask==null || mGetListCarrierTask.getStatus()==Status.FINISHED){
			mGetListCarrierTask=new GetListCarrierTask();
			mGetListCarrierTask.execute();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirmBtn:
			
			break;
		case R.id.createAddressBtn:
			mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.CREATE_ADDRESS);
			break;
		default:
			break;
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,
			long arg3) {
		try {
			switch (view.getId()) {
			case R.id.listAddress:
				mCurrentAddress =mAdapter.getItem(position);
				mHomeActivity.setAddress(mCurrentAddress);
				break;
			case R.id.listCarrier:
				mCurrentCarrier=mCarrierAdapter.getItem(position);
				mHomeActivity.setCarrier(mCurrentCarrier);
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
				loadCarrier();
			}
			else{
				switchView(false, false);
			}
		}
		
	}
	class GetListCarrierTask extends AsyncTask<Void, Void, List<CarrierDTO>>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
		}
		@Override
		protected List<CarrierDTO> doInBackground(Void... params) {
			return CommonModel.getInstance().getListCarrier(UrlRequest.GET_CARRIER_URL);
		}
		@Override
		protected void onPostExecute(List<CarrierDTO> result) {
			super.onPostExecute(result);
			if(result!=null && result.size()>0){
				mCarrierAdapter.updateDataList(result);				
				switchView(false, false);
			}else{
				switchView(false, false);
			}
		}
		
	}
	@Override
	public void onResume() {
		super.onResume();
		loadAddresses();
	}
}
