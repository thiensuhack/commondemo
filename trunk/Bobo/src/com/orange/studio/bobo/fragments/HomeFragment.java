package com.orange.studio.bobo.fragments;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.adapters.GridProductAdapter;
import com.orange.studio.bobo.configs.OrangeConfig;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.customviews.ExpandableHeightGridView;
import com.orange.studio.bobo.models.ProductModel;
import com.orange.studio.bobo.objects.HomeSliderDTO;
import com.orange.studio.bobo.objects.ProductDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
import com.orange.studio.bobo.xml.XMLHandler;
import com.viewpagerindicator.CirclePageIndicator;

public class HomeFragment extends BaseFragment implements OnItemClickListener {
	private ViewPager mViewPager = null;
	private CirclePageIndicator mCirclePageIndicator = null;

	private ImageHomeSlider mSilderAdapter = null;

	private LoadHomeSliderTask mLoadHomeSliderTask = null;
	private ExpandableHeightGridView mGridView = null;
	private List<ProductDTO> mListProducts = null;
	private GridProductAdapter mProductAdapter = null;
	private LoadProductsTask mLoadProductsTask = null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_home, container, false);
			initView();
			initListener();

		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}

	private void initView() {
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		mViewPager = (ViewPager) mView.findViewById(R.id.viewPagerHome);
		mCirclePageIndicator = (CirclePageIndicator) mView
				.findViewById(R.id.indicatorHome);
		mGridView = (ExpandableHeightGridView) mView
				.findViewById(R.id.homeGridView);
		mGridView.setExpanded(true);
		mProductAdapter = new GridProductAdapter(getActivity());
		mGridView.setAdapter(mProductAdapter);

	}

	private void initListener() {
		mGridView.setOnItemClickListener(this);
	}

	private void loadHomeSliderData() {
		if (mLoadHomeSliderTask == null
				|| mLoadHomeSliderTask.getStatus() == Status.FINISHED) {
			mLoadHomeSliderTask = new LoadHomeSliderTask();
			mLoadHomeSliderTask.execute();
		}
	}

	private void loadProductData() {
		if (mLoadProductsTask == null
				|| mLoadProductsTask.getStatus() == Status.FINISHED) {
			mLoadProductsTask = new LoadProductsTask();
			mLoadProductsTask.execute();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loadHomeSliderData();
		loadProductData();
	}

	private class ImageHomeSlider extends PagerAdapter {
		private LayoutInflater inflater;
		private List<HomeSliderDTO> mData = new ArrayList<HomeSliderDTO>();

		public ImageHomeSlider(List<HomeSliderDTO> _mData) {
			inflater = LayoutInflater.from(getActivity());
			mData = _mData;
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
			View mContainer = inflater.inflate(
					R.layout.layout_image_home_slider, view, false);
			assert mContainer != null;
			ImageView imageView = (ImageView) mContainer
					.findViewById(R.id.imageHomeSlider);

			ImageLoader.getInstance().displayImage(
					mData.get(position).imageURL, imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							Toast.makeText(getActivity(), message,
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
						}
					});

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

	private class LoadHomeSliderTask extends
			AsyncTask<Void, Void, List<HomeSliderDTO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<HomeSliderDTO> doInBackground(Void... arg0) {
			List<HomeSliderDTO> result = new ArrayList<HomeSliderDTO>();
			for (int i = 0; i < 10; i++) {
				HomeSliderDTO item = new HomeSliderDTO();
				item.imageURL = OrangeConfig.IMAGES[i];
				result.add(item);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<HomeSliderDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				mSilderAdapter = new ImageHomeSlider(result);
				mViewPager.setAdapter(mSilderAdapter);
				mCirclePageIndicator.setViewPager(mViewPager);
			}
		}
	}



	private class LoadProductsTask extends
			AsyncTask<Void, Void, List<ProductDTO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<ProductDTO> doInBackground(Void... arg0) {
			Bundle mParams=OrangeUtils.createRequestBundle(OrangeConfig.ITEMS_PAGE);
			
			return ProductModel.getInstance().getListProduct(UrlRequest.PRODUCT_HOME, null, mParams);
//			List<ProductDTO> result = new ArrayList<ProductDTO>();
//			for (int i = 0; i < 10; i++) {
//				ProductDTO item = new ProductDTO();
//				// item.proImageURL = OrangeConfig.IMAGES[i];
//				item.proImageURL = "http://www.bobo-u.com/24-home_default/black-dress-1.jpg";
//				item.name = "Product Name" + (i + 1);
//				item.price = "1000000";
//				item.wholesale_price = "2500000";
//				item.description = "Sẽ mãi luôn yêu em, luôn bên em quan tâm em mỗi ngày Vì anh không muốn mất em lần nữa, hãy lắng nghe lòng anh Bởi vì khi xa nhau, tim anh đau, nhớ đến em rất nhiều";
//				result.add(item);
//			}
//			return result;
		}

		@Override
		protected void onPostExecute(List<ProductDTO> result) {
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				getHomeActivity().mListItemCart = result;
				mProductAdapter.updateDataList(result);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ProductDTO mProduct = mProductAdapter.getItem(position);
		if (mProduct != null) {
			getHomeActivity().setCurrentProduct(mProduct);
			getHomeActivity().onNavigationDrawerItemSelected(-11);
		}
	}
}