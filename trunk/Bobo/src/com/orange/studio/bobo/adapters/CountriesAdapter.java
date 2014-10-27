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
import com.orange.studio.bobo.objects.CountryDTO;

public class CountriesAdapter extends OrangeBaseAdapter {

	private class CustomViewHolder {
		public TextView countryName;
	}

	private Activity mActivity;
	private List<CountryDTO> mListData;
	private LayoutInflater mInflater = null;

	public CountriesAdapter(Activity _mActivity) {
		super();
		mActivity = _mActivity;
		mListData = new ArrayList<CountryDTO>();
		mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateDataList(List<CountryDTO> _mListData) {
		mListData.clear();
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	public void insertListData(List<CountryDTO> _mListData) {
		mListData.addAll(_mListData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public CountryDTO getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.layout_item_country,
					parent, false);
			viewHolder = new CustomViewHolder();
			viewHolder.countryName = (TextView) convertView
					.findViewById(R.id.countryName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (CustomViewHolder) convertView.getTag();
		}
		CountryDTO mData = mListData.get(position);
		viewHolder.countryName.setText(mData.name);
		return convertView;
	}

}
