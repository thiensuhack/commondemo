package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.ListItemsCartAdapter;
import com.orange.studio.bobo.objects.ProductDTO;

public class ShoppingCartFragment extends BaseFragment implements
		OnItemClickListener {
	private ListView mListView = null;
	private ListItemsCartAdapter mAdapter = null;
	private ShoppingCartHandler mHandler=null;
	private TextView mNotFoundMessage=null;
	private View mNotFoundContainer=null;
	
	public interface ShoppingCartHandler{
		public void removeItemCart(String proId);
	}
	
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

	private void initView() {
		
		mHandler=new ShoppingCartHandler() {			
			@Override
			public void removeItemCart(String proId) {
				try {
					getHomeActivity().removeCartItem(proId);
					mAdapter.updateDataList(getHomeActivity().mListItemCart);
					checkItemsCart();
				} catch (Exception e) {
					return;
				}
			}
		};
		mNotFoundContainer=(LinearLayout)mView.findViewById(R.id.notFoundContainer);
		mNotFoundMessage=(TextView)mView.findViewById(R.id.notFoundTextMessage);
		mNotFoundMessage.setText(getActivity().getString(R.string.msg_empty_items_cart));
		
		mListView = (ListView) mView.findViewById(R.id.myListView);
		mAdapter = new ListItemsCartAdapter(getActivity(),mHandler);
		mListView.setAdapter(mAdapter);
		if (getHomeActivity().isHasItemsCart()) {
			mAdapter.updateDataList(getHomeActivity().mListItemCart);
		}
		checkItemsCart();
	}
	private void initListener() {
		mListView.setOnItemClickListener(this);
	}
	private void checkItemsCart(){
		if(mAdapter!=null && mAdapter.getCount()>0){
			mListView.setVisibility(View.VISIBLE);
			mNotFoundContainer.setVisibility(View.GONE);			
		}else{
			mNotFoundContainer.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ProductDTO mProduct = mAdapter.getItem(position);
		if (mProduct != null) {
			getHomeActivity().setCurrentProduct(mProduct);
			getHomeActivity().onNavigationDrawerItemSelected(-11);
		}
		return;
	}
	@Override
	public void onResume() {
		super.onResume();				
	}
}
