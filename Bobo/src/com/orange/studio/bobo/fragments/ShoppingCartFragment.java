package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;
import com.orange.studio.bobo.adapters.ListItemsCartAdapter;
import com.orange.studio.bobo.objects.ProductDTO;

public class ShoppingCartFragment extends BaseFragment implements
		OnItemClickListener,OnClickListener {
	private ListView mListView = null;
	private ListItemsCartAdapter mAdapter = null;
	private ShoppingCartHandler mHandler=null;
	private TextView mNotFoundMessage=null;
	private View mNotFoundContainer=null;
	private TextView mTotalPrice=null;
	private Button mCheckOutBtn=null;
	private HomeActivity mHomeActivity=null;
	
	public interface ShoppingCartHandler{
		public void removeItemCart(String proId);
		public void increaseItemCart(ProductDTO item);
		public void decreaseItemCart(ProductDTO item);
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
		mHomeActivity=getHomeActivity();
		mHandler=new ShoppingCartHandler() {			
			@Override
			public void removeItemCart(String proId) {
				try {
					mHomeActivity.removeCartItem(proId);
					mAdapter.updateDataList(mHomeActivity.mListItemCart);
					checkItemsCart();
					updateTotalPrice();
				} catch (Exception e) {
					return;
				}
			}

			@Override
			public void increaseItemCart(ProductDTO item) {
				try {
					mHomeActivity.addCart(item);
					mAdapter.updateDataList(mHomeActivity.mListItemCart);
					checkItemsCart();
					updateTotalPrice();
				} catch (Exception e) {
					return;
				}
			}

			@Override
			public void decreaseItemCart(ProductDTO item) {
				try {
					mHomeActivity.decreaseCartItem(item);
					mAdapter.updateDataList(mHomeActivity.mListItemCart);
					checkItemsCart();
					updateTotalPrice();
				} catch (Exception e) {
					return;
				}
			}
		};
		mTotalPrice=(TextView)mView.findViewById(R.id.itemCartTotalPrice);
		mCheckOutBtn=(Button)mView.findViewById(R.id.itemCartCheckOutBtn);
		
		mNotFoundContainer=(RelativeLayout)mView.findViewById(R.id.notFoundContainer);
		mNotFoundMessage=(TextView)mView.findViewById(R.id.notFoundTextMessage);
		mNotFoundMessage.setText(getActivity().getString(R.string.msg_empty_items_cart));
		
		mListView = (ListView) mView.findViewById(R.id.myListView);
		mAdapter = new ListItemsCartAdapter(getActivity(),mHandler);
		mListView.setAdapter(mAdapter);
		if (mHomeActivity.isHasItemsCart()) {
			mAdapter.updateDataList(mHomeActivity.mListItemCart);
		}
		
	}
	private void initListener() {
		mCheckOutBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}
	private void updateTotalPrice(){
		double total=mHomeActivity.getTotalPriceItemsCart();
		mTotalPrice.setText("$"+String.valueOf(total));
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
			mHomeActivity.setCurrentProduct(mProduct);
			mHomeActivity.onNavigationDrawerItemSelected(-11);
		}
		return;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.itemCartCheckOutBtn:
			mHomeActivity.checkOut();
			break;

		default:
			break;
		}
	}
	@Override
	public void onResume() {
		super.onResume();	
		checkItemsCart();
		updateTotalPrice();
	}
}
