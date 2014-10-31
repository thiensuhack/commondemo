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
import com.orange.studio.bobo.objects.CarrierDTO;

public class CarrierAdapter extends OrangeBaseAdapter {

	private class CustomViewHolder {
		public TextView addressName;
	}

	private Activity mActivity;
	private List<CarrierDTO> mListData;
	private LayoutInflater mInflater = null;

	public CarrierAdapter(Activity _mActivity) {
		super();
		mActivity = _mActivity;
		mListData = new ArrayList<CarrierDTO>();
		mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateDataList(List<CarrierDTO> _mListData) {
		mListData.clear();
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	public void insertListData(List<CarrierDTO> _mListData) {
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public CarrierDTO getItem(int arg0) {
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
		CarrierDTO mData = mListData.get(position);
		viewHolder.addressName.setText(mData.value);
		return convertView;
	}

}
