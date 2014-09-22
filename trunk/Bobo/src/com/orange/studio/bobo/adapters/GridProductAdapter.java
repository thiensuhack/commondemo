package com.orange.studio.bobo.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;
import com.orange.studio.bobo.objects.ProductDTO;

public class GridProductAdapter extends OrangeBaseAdapter {

	private class ProductViewHolder {
		public TextView proName;
		public TextView proPrice;
		public TextView proPriceDiscount;
		public ImageView proImage;
		public Button addToCart;
	}

	private Activity mActivity;
	private List<ProductDTO> mListData;
	private LayoutInflater mInflater = null;
	

	public GridProductAdapter(Activity _mActivity) {
		super();
		mActivity = _mActivity;
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
			convertView = mInflater.inflate(R.layout.layout_item_product,
					parent, false);
			viewHolder = new ProductViewHolder();
			viewHolder.proImage = (ImageView) convertView
					.findViewById(R.id.proImage);
			viewHolder.proName = (TextView) convertView
					.findViewById(R.id.proName);
			viewHolder.proPrice = (TextView) convertView
					.findViewById(R.id.proPrice);
			viewHolder.proPriceDiscount = (TextView) convertView
					.findViewById(R.id.proPriceDisCount);
			viewHolder.addToCart = (Button) convertView
					.findViewById(R.id.btnAddCartGrid);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ProductViewHolder) convertView.getTag();
		}
		final ProductDTO mData = mListData.get(position);
		viewHolder.proName.setText(mData.name);
		viewHolder.proPrice.setText(String.valueOf(mData.price));
		viewHolder.proPriceDiscount.setText(String
				.valueOf(mData.wholesale_price));
		viewHolder.addToCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					((HomeActivity)mActivity).addToCart(mData);
				} catch (Exception e) {
				}
			}
		});
		ImageLoader.getInstance().displayImage(mData.id_default_image,
				viewHolder.proImage, options, null);
		return convertView;
	}

}