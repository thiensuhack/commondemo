package com.orange.studio.bobo.fragments;

import java.util.Random;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.objects.GameDTO;

public class SpinToWinFragment extends BaseFragment implements OnClickListener {
	private ImageView mFirstCard;
	private ImageView mSecondCard;
	private ImageView mThirdCard;
	private Button mSpinBtn;
	private boolean isSpining=false;
	private Handler handler=null;
	private Runnable runnable=null;
	private boolean isWinner=false;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_spin_to_win, container,
					false);
			initView();
			initListener();
		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		return mView;
	}

	private void initView() {
		mFirstCard = (ImageView) mView.findViewById(R.id.cartItemFirst);
		mSecondCard = (ImageView) mView.findViewById(R.id.cartItemSecond);
		mThirdCard = (ImageView) mView.findViewById(R.id.cartItemThird);
		mSpinBtn = (Button) mView.findViewById(R.id.spinToWinBtn);
		
	}

	private void initListener() {
		mSpinBtn.setOnClickListener(this);
	}

	private void loadAboutInfo() {
	}

	@Override
	public void onResume() {
		super.onResume();
		loadAboutInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.spinToWinBtn:
			if(!isSpining){
				isSpining=true;
				mSpinBtn.setText(getActivity().getString(R.string.stop_spin_to_win_label));
				spinToWin();
			}else{
				isSpining=false;
				mSpinBtn.setText(getActivity().getString(R.string.spin_to_win_label));
				isWinner=true;
				//handler.removeCallbacks(runnable);
			}
			
			break;

		default:
			break;
		}
	}

	private void spinToWin() {
		final int[] imageArray = { R.drawable.miss01,
				R.drawable.miss02, R.drawable.miss03 };
		final Random rand=new Random();
		handler = new Handler();
		runnable = new Runnable() {
			int i = 0;
			public void run() {
				if(isWinner){
					i = rand.nextInt(3);
					mFirstCard.setBackgroundResource(imageArray[i]);
					mSecondCard.setBackgroundResource(imageArray[i]);
					mThirdCard.setBackgroundResource(imageArray[i]);
					isWinner=false;
					handler.removeCallbacks(runnable);
				}else{
					i = rand.nextInt(3);
					mFirstCard.setBackgroundResource(imageArray[i]);
//					handler.postDelayed(this, 100); // for interval...
					i = rand.nextInt(3);
					mSecondCard.setBackgroundResource(imageArray[i]);
//					handler.postDelayed(this, 100); // for interval...
					i = rand.nextInt(3);
					mThirdCard.setBackgroundResource(imageArray[i]);
					i++;
//					if (i > imageArray.length - 1) {
//						i = 0;
//					}
					handler.postDelayed(this, 50); // for interval...	
				}					
			}
		};
		
		handler.postDelayed(runnable, 50); // for initial delay..
		
	}
	class SpinToWinTask extends AsyncTask<Void, Void, GameDTO>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected GameDTO doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(GameDTO result) {
			super.onPostExecute(result);
		}
	}
}
