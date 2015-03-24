package com.orange.studio.bobo.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.CarrierAdapter;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.CarrierDTO;

public class GiftCodeFragment extends BaseFragment implements OnClickListener{
	private TextView mVoucher=null;
	private Button mConfirmBtn=null;
	private Button mRefreshBtn=null;
	private CarrierAdapter mCarrierAdapter=null;
	
	private GetListCarrierTask mGetListCarrierTask=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_add_giftcode, container,
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
		mVoucher=(TextView)mView.findViewById(R.id.txtVoucher);
		
		mConfirmBtn=(Button)mView.findViewById(R.id.confirmBtn);
		mConfirmBtn.setVisibility(View.GONE);
		
		mRefreshBtn=(Button)mView.findViewById(R.id.refreshBtn);
		mRefreshBtn.setVisibility(View.GONE);
		
		mCarrierAdapter=new CarrierAdapter(mHomeActivity);
		//mVoucher.setAdapter(mCarrierAdapter);
		
		initLoadingView();
		initNotFoundView();
	}
	private void initListener(){
		//mVoucher.setOnItemSelectedListener(this);
		mConfirmBtn.setOnClickListener(this);
		mRefreshBtn.setOnClickListener(this);
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
			mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.SUMMARY);
			break;
		case R.id.refreshBtn:
			loadCarrier();
			break;
		case R.id.createAddressBtn:
			mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.CREATE_ADDRESS);
			break;
		default:
			break;
		}
	}
//	@Override
//	public void onItemSelected(AdapterView<?> arg0, View view, int position,
//			long arg3) {
//		try {
//			switch (arg0.getId()) {
//			case R.id.listCarrier:
//				//mCurrentCarrier=mCarrierAdapter.getItem(position);
//				mHomeActivity.setCarrier(mCarrierAdapter.getItem(position));
//				break;
//			default:
//				break;
//			}			
//			//mHomeActivity.showToast("Address:"+mCurrentAddress.alias+"-ID:" + mCurrentAddress.id);
//		} catch (Exception e) {
//		}
//	}
//	@Override
//	public void onNothingSelected(AdapterView<?> arg0) {
//		
//	}
	class GetListCarrierTask extends AsyncTask<Void, Void, List<CarrierDTO>>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
		}
		@Override
		protected List<CarrierDTO> doInBackground(Void... params) {
			try {
				String url=UrlRequest.GET_CARRIER_URL+mHomeActivity.getCurItemCart().id;
				//String url=UrlRequest.GET_CARRIER_URL+"73";
				Log.i("CARRIER URL: ", url);
				return CommonModel.getInstance().getListCarrier(url);
			} catch (Exception e) {
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<CarrierDTO> result) {
			super.onPostExecute(result);
			if(result!=null && result.size()>0){
				mCarrierAdapter.updateDataList(result);				
				mConfirmBtn.setVisibility(View.VISIBLE);
				mRefreshBtn.setVisibility(View.GONE);
			}else{
				mConfirmBtn.setVisibility(View.GONE);
				mRefreshBtn.setVisibility(View.VISIBLE);
			}
			switchView(false, false);
		}
		
	}
	@Override
	public void onResume() {
		super.onResume();
		loadCarrier();
	}
}
