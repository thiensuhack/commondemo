package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;

public class BaseFragment extends Fragment{
	protected View mView = null;
	protected DisplayImageOptions options;
	
	protected View mNotFoundView=null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	protected HomeActivity getHomeActivity(){
		return (HomeActivity)getActivity();
	}
	protected void initNotFoundView(){
		if(mView!=null){
			mNotFoundView=(LinearLayout)mView.findViewById(R.id.notFoundContainer);	
		}		
	}
}
