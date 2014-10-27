package com.orange.studio.bobo.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.CountriesAdapter;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.CountryDTO;

public class CreateAddressShoppingCartFragment extends BaseFragment implements OnItemSelectedListener{
	private EditText mAddress=null;
	private Spinner mListCountry=null;
	private CountriesAdapter mAdapter=null;
	private GetListContryTask mGetListContryTask=null;
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
		mAdapter=new CountriesAdapter(mHomeActivity);
		mListCountry.setAdapter(mAdapter);
		initLoadingView();
		initNotFoundView();
	}
	private void initListener(){
		mListCountry.setOnItemSelectedListener(this);
	}
	private void loadCountries(){
		if(mGetListContryTask==null || mGetListContryTask.getStatus()==Status.FINISHED){
			mGetListContryTask=new GetListContryTask();
			mGetListContryTask.execute();
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		loadCountries();
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {		
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
				switchView(false, false);
			}
		}
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
}
