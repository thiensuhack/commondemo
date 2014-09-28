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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.ListItemSearchAdapter;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.ProductModel;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.utils.OrangeUtils;

public class SearchResultFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener {
	private ListView mListView = null;
	private ListItemSearchAdapter mAdapter = null;
	private View mSearchBtn=null;
	private EditText mSearchEditText=null;
	private SearchProductTask mSearchProductTask=null;
	private String mSearchKey=null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_search_result,
					container, false);
			initView();
			initListener();
		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}

	private void initView() {
		mListView = (ListView) mView.findViewById(R.id.mySearchListView);
		mSearchBtn=(ImageView)mView.findViewById(R.id.searchBtn);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText=(EditText)mView.findViewById(R.id.searchTextBox);
		
		initNotFoundView();
		mAdapter = new ListItemSearchAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		initNotFoundView();
		initLoadingView();	
		switchView(true, false);
	}

	private void initListener() {
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		ProductDTO mProduct = mAdapter.getItem(position);
		if (mProduct != null) {
			getHomeActivity().setCurrentProduct(mProduct);
			getHomeActivity().onNavigationDrawerItemSelected(MENU_NAME.PRODUCT_DETAIL_FRAGMENT);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchBtn:
			mSearchKey=mSearchEditText.getText().toString().trim();
			if(mSearchKey.length()<1){
				return;
			}
			getHomeActivity().setSearchKey(mSearchKey);
			
			searchProduct();
			break;
		
		default:
			break;
		}		
	}
	private void searchProduct(){
		if(mSearchProductTask==null || mSearchProductTask.getStatus()==Status.FINISHED){
			mSearchProductTask=new SearchProductTask();
			mSearchProductTask.execute();
		}
	}
	private class SearchProductTask extends
			AsyncTask<Void, Void, List<ProductDTO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
		}

		@Override
		protected List<ProductDTO> doInBackground(Void... arg0) {
			
			Bundle mParams = OrangeUtils.createRequestBundle(
					OrangeConfig.ITEMS_PAGE, null);
			return ProductModel.getInstance().searchProduct(
					UrlRequest.PRODUCT_HOME, null, mParams,mSearchKey);
		}

		@Override
		protected void onPostExecute(List<ProductDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				mAdapter.updateDataList(result);
				switchView(false, false);
			}else{
				switchView(true, false);
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		try {
			mSearchKey=getHomeActivity().mSearchKey;
			if(mSearchKey!=null && mSearchKey.trim().length()>0){
				mSearchEditText.setText(mSearchKey.trim());
				searchProduct();
			}			
		} catch (Exception e) {
		}
	}
	
}
