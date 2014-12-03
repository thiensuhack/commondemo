package com.orange.studio.bobo.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.fragments.ShoppingCartFragment.ShoppingCartHandler;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.utils.OrangeUtils;

public class ListItemsCartAdapter extends OrangeBaseAdapter {

	private class ProductViewHolder {
		public TextView proName;
		public TextView proPrice;
		public TextView proPriceDiscount;
		public TextView proCounter;
		public ImageView proImage;
		public ImageView proRemoveImage;
		public ImageView proDecrease;
		public ImageView proIncrease;
		public View color;
	}

	private Activity mActivity;
	private List<ProductDTO> mListData;
	private LayoutInflater mInflater = null;
	private ShoppingCartHandler mHandler = null;

	public ListItemsCartAdapter(Activity _mActivity,
			ShoppingCartHandler _mHandler) {
		super();
		mActivity = _mActivity;
		mHandler = _mHandler;
		mListData = new ArrayList<ProductDTO>();
		mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateDataList(List<ProductDTO> _mListData) {
		mListData.clear();
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	public void insertListData(List<ProductDTO> _mListData) {
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public ProductDTO getItem(int arg0) {
		if (mListData == null) {
			return null;
		}
		return mListData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_item_shopping_cart,
					parent, false);
			viewHolder = new ProductViewHolder();
			viewHolder.proImage = (ImageView) convertView
					.findViewById(R.id.proImageShoppingCart);
			viewHolder.proRemoveImage = (ImageView) convertView
					.findViewById(R.id.proRemoveShoppingCart);
			viewHolder.proName = (TextView) convertView
					.findViewById(R.id.proNameShoppingCart);
			viewHolder.proPrice = (TextView) convertView
					.findViewById(R.id.proPriceShoppingCart);
			viewHolder.proPriceDiscount = (TextView) convertView
					.findViewById(R.id.proPriceDiscountShoppingCart);
			viewHolder.proCounter = (TextView) convertView
					.findViewById(R.id.proCounterShoppingCart);
			viewHolder.proDecrease = (ImageView) convertView
					.findViewById(R.id.itemCartDecreaseBtn);
			viewHolder.proIncrease = (ImageView) convertView
					.findViewById(R.id.itemCartIncreaseBtn);
			viewHolder.color=(View)convertView.findViewById(R.id.itemColor);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ProductViewHolder) convertView.getTag();
		}
		final ProductDTO mData = mListData.get(position);
		viewHolder.proName.setText(mData.name);
		viewHolder.proPrice.setText("$" + String.format("%,.2f",OrangeUtils.getPriceBeforeTax(mData)));
		viewHolder.proPriceDiscount.setText("$"
				+ String.valueOf(mData.wholesale_price));
		viewHolder.proCounter.setText(String.valueOf(mData.cartCounter));
		viewHolder.proRemoveImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mHandler.removeItemCart(mData.id);
				} catch (Exception e) {
					return;
				}
			}
		});
		viewHolder.proDecrease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					mHandler.decreaseItemCart(mData);
				} catch (Exception e) {
					return;
				}
			}
		});
		viewHolder.proIncrease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					mHandler.increaseItemCart(mData);
				} catch (Exception e) {
					return;
				}
			}
		});
		try {
			if(mData.color!=null && mData.color.color!=null){
				viewHolder.color.setBackgroundColor(Color.parseColor(mData.color.color));
			}
		} catch (Exception e) {
			viewHolder.color.setBackgroundColor(Color.WHITE);
		}
		
		ImageLoader.getInstance().displayImage(mData.id_default_image,
				viewHolder.proImage, options, null);
		return convertView;
	}

}
