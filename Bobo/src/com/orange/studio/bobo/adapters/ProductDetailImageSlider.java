package com.orange.studio.bobo.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.objects.HomeSliderDTO;

public class ProductDetailImageSlider extends PagerAdapter {

	private LayoutInflater inflater;
	private List<HomeSliderDTO> mData = new ArrayList<HomeSliderDTO>();
	private DisplayImageOptions options;
	private Activity mActivity = null;

	public ProductDetailImageSlider(Activity _mActivity,
			List<HomeSliderDTO> _mData) {
		mActivity = _mActivity;
		inflater = LayoutInflater.from(mActivity);
		mData = _mData;
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View mContainer = inflater.inflate(R.layout.layout_image_home_slider,
				view, false);
		assert mContainer != null;
		ImageView imageView = (ImageView) mContainer
				.findViewById(R.id.imageHomeSlider);

		ImageLoader.getInstance().displayImage(mData.get(position).imageURL,
				imageView, options, null);

		view.addView(mContainer, 0);
		return mContainer;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}
