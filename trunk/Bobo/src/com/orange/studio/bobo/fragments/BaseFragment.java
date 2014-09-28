package com.orange.studio.bobo.fragments;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;
import com.todddavies.components.progressbar.ProgressWheel;

public class BaseFragment extends Fragment{
	protected View mView = null;
	protected DisplayImageOptions options;
	
	protected View mNotFoundView=null;
	protected View mLoadingView=null;
	protected ProgressWheel mProgress=null;
	
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
			mNotFoundView=(RelativeLayout)mView.findViewById(R.id.notFoundContainer);	
		}		
	}
	protected void initLoadingView(){
		if(mView!=null){
			mLoadingView=(RelativeLayout)mView.findViewById(R.id.loadingContainer);
			mProgress=(ProgressWheel)mLoadingView.findViewById(R.id.progressWheel);
		}
	}
	protected void switchView(boolean isShowNotFound, boolean isShowLoading){
		if(mNotFoundView!=null){
			mNotFoundView.setVisibility(isShowNotFound?View.VISIBLE:View.GONE);
		}
		if(mLoadingView!=null){
			mLoadingView.setVisibility(isShowLoading?View.VISIBLE:View.GONE);
			if(mProgress!=null){
				if(isShowLoading){
					mProgress.spin();
				}else{
					mProgress.stopSpinning();
				}
			}
		}
	}
}
