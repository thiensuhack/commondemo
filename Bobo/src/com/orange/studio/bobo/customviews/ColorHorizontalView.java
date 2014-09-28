/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orange.studio.bobo.customviews;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.objects.ColorDTO;
import com.orange.studio.bobo.utils.OrangeUtils;
/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class ColorHorizontalView extends HorizontalScrollView {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";
	
	public interface OnTabReselectedListener {

		void onTabReselected(ColorDTO color);
	}
	private int oldTabSelected=0;
	private Runnable mTabSelector;
	private int textSize=40;//40dp
	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView) view;
//			final int oldSelected = oldTabSelected;
//			final int newSelected = tabView.getIndex();			
//			if (oldSelected == newSelected && mTabReselectedListener != null) {
//				mTabReselectedListener.onTabReselected(newSelected);
//			}
			if (tabView !=null && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(tabView.mColor);
			}
		}
	};

	private final OrangeIcsLinearLayout mTabLayout;


	private int mMaxTabWidth;
	private int mSelectedTabIndex;

	private OnTabReselectedListener mTabReselectedListener;

	public ColorHorizontalView(Context context) {
		this(context, null);
	}

	public ColorHorizontalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);

		mTabLayout = new OrangeIcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private void addTab(int index, CharSequence text, int iconResId,ColorDTO color) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);
		tabView.mColor=color;
		tabView.setBackgroundColor(Color.parseColor(color.color));
		tabView.setWidth((int)OrangeUtils.convertDpToPixel(textSize));
		tabView.setHeight((int)OrangeUtils.convertDpToPixel(textSize));
		tabView.setGravity(Gravity.CENTER);
//		if(index==activeTabIndex){
//			tabView.isActive=true;
//		}
		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}

		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0,
				MATCH_PARENT, 1));
	}

	

//	private Paint paintActive=null;
//	private int activeTabIndex=1;
	public void updateView(List<ColorDTO> data) {
//		paintActive=new Paint();
//		paintActive.setStyle(Paint.Style.STROKE);
//		paintActive.setColor(Color.BLACK);
//		paintActive.setStrokeWidth(7);		
		if(data==null || data.size()<1){
			return;
		}
		mTabLayout.removeAllViews();
		
		final int count = data.size();
		for (int i = 0; i < count; i++) {
			CharSequence title = null;
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			addTab(i, title, iconResId,data.get(i));
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
		if (mTabReselectedListener != null) {
			mTabReselectedListener.onTabReselected(data.get(0));
		}
	}

	
	public void setCurrentItem(int item) {
		mSelectedTabIndex = item;

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	private class TabView extends TextView {
		private int mIndex;
		private ColorDTO mColor;
//		private boolean isActive;
//		Paint paintNormal=null;
		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
//			paintNormal=new Paint();
			
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//			// Re-measure if we went beyond our maximum size.
//			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
//				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
//						MeasureSpec.EXACTLY), heightMeasureSpec);
//			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
//			if(isActive){
//				canvas.drawLine(0, 0, getWidth(), 0, paintActive);
//				canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paintActive);
//				canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paintActive);
//				canvas.drawLine(0, 0, 0, getHeight(), paintActive);
//			}
		}
		public int getIndex() {
			return mIndex;
		}
	}
}
