package com.orange.studio.bobo.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.objects.AddressDTO;

public class AddressesAdapter extends OrangeBaseAdapter {

	private class CustomViewHolder {
		public TextView addressName;
	}

	private Activity mActivity;
	private List<AddressDTO> mListData;
	private LayoutInflater mInflater = null;

	public AddressesAdapter(Activity _mActivity) {
		super();
		mActivity = _mActivity;
		mListData = new ArrayList<AddressDTO>();
		mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateDataList(List<AddressDTO> _mListData) {
		mListData.clear();
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	public void insertListData(List<AddressDTO> _mListData) {
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public AddressDTO getItem(int arg0) {
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
		CustomViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_item_address,
					parent, false);
			viewHolder = new CustomViewHolder();
			viewHolder.addressName = (TextView) convertView
					.findViewById(R.id.addressName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (CustomViewHolder) convertView.getTag();
		}
		AddressDTO mData = mListData.get(position);
		viewHolder.addressName.setText(mData.alias);
		return convertView;
	}

}
