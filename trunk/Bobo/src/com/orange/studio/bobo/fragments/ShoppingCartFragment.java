package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.ListItemShoppingCartAdapter;

public class ShoppingCartFragment extends BaseFragment {
	private ListView mListView=null;
	private ListItemShoppingCartAdapter mAdapter=null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_shopping_cart,
					container, false);
			initView();
			initListener();

		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}
	private void initView(){
		mListView=(ListView)mView.findViewById(R.id.myListView);
		mAdapter=new ListItemShoppingCartAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		
		if(getHomeActivity().mListItemCart!=null){
			mAdapter.updateDataList(getHomeActivity().mListItemCart);
		}
	}
	private void initListener(){
		mListView.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
	}
}
