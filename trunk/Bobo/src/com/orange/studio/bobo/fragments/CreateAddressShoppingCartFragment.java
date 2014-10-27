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
import android.widget.EditText;
import android.widget.Spinner;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.CountriesAdapter;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.CountryDTO;
import com.orange.studio.bobo.objects.CustomerDTO;

public class CreateAddressShoppingCartFragment extends BaseFragment implements OnItemSelectedListener, OnClickListener{
	private EditText mAddress=null;
	private Spinner mListCountry=null;
	private Button mSubmitBtn=null;
	
	private CountriesAdapter mAdapter=null;
	private GetListContryTask mGetListContryTask=null;
	private CreateAddressTask mCreateAddressTask=null;
	
	private String mStrAddress="";
	private CountryDTO mCurrentCountry=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_create_address, container,
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
		mAddress=(EditText)mView.findViewById(R.id.myAddress);
		mListCountry=(Spinner)mView.findViewById(R.id.listCountry);
		mSubmitBtn=(Button)mView.findViewById(R.id.createBtn);
		mAdapter=new CountriesAdapter(mHomeActivity);
		mListCountry.setAdapter(mAdapter);
		initLoadingView();
		initNotFoundView();
	}
	private void initListener(){
		mListCountry.setOnItemSelectedListener(this);
		mSubmitBtn.setOnClickListener(this);
	}
	private void loadCountries(){
		if(mGetListContryTask==null || mGetListContryTask.getStatus()==Status.FINISHED){
			mGetListContryTask=new GetListContryTask();
			mGetListContryTask.execute();
		}
	}
	private void submitAddress(){
		if(mCreateAddressTask==null|| mCreateAddressTask.getStatus()==Status.FINISHED){
			mCreateAddressTask=new CreateAddressTask();
			mCreateAddressTask.execute();
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		loadCountries();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createBtn:
			mStrAddress=mAddress.getText().toString().trim();
			if(mStrAddress.length()<1){
				mHomeActivity.showToast(getActivity().getString(R.string.empty_field));
				return;
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {		
		try {
			mCurrentCountry =mAdapter.getItem(position);
			mHomeActivity.showToast("Country:"+mCurrentCountry.name+"-ID:"+mCurrentCountry.id);
		} catch (Exception e) {
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	class GetListContryTask extends AsyncTask<Void, Void, List<CountryDTO>>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
		}
		@Override
		protected List<CountryDTO> doInBackground(Void... params) {
			return CommonModel.getInstance().getListCountry(UrlRequest.GET_LIST_COUNTRY);
		}
		@Override
		protected void onPostExecute(List<CountryDTO> result) {
			super.onPostExecute(result);
			if(result!=null && result.size()>0){
				mAdapter.updateDataList(result);				
				switchView(false, false);
			}else{
				mHomeActivity.showToast(mHomeActivity.getString(R.string.address_country_loading_error));
				switchView(true, false);
			}
		}
		
	}
	class CreateAddressTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
	private String createStringAddress(){
		try {
			CustomerDTO cus=mHomeActivity.getUserInfo();
			String result="<?xml version=\"1.0\" encoding=\"UTF-8\"?><prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\"><address>";
			result+="<id_customer>"+cus.id+"</id_customer>";
			result+="<id_manufacturer>0</id_manufacturer><id_supplier>0</id_supplier><id_warehouse>0</id_warehouse>";
			result+="<id_country>"+mCurrentCountry.id+"</id_country><id_state></id_state>";
			result+="<alias>"+mStrAddress+"</alias>";
			result+="<company></company><lastname></lastname><firstname></firstname>";
			result+="<vat_number></vat_number><address1></address1><address2></address2><postcode></postcode><city></city>";
			result+="<phone></phone><phone_mobile></phone_mobile>";
			result+="</address></prestashop>";
			return result;
		} catch (Exception e) {
		}
		return null;
	}
}
