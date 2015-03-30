package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.GameDTO;

public class SpinToWinFragment extends BaseFragment implements OnClickListener {
	private ImageView mFirstCard;
	private ImageView mSecondCard;
	private ImageView mThirdCard;
	private Button mSpinBtn;
	private WebView mWebView;
	
	private boolean isSpining=false;
	private Handler handler=null;
	private Runnable runnable=null;
	private boolean isWinner=false;

	private GameDTO mGame=null;
	private List<Integer> mGameResult= new ArrayList<Integer>();
	private String mContent=null;
	
	private SpinToWinTask mSpinToWinTask=null;
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
		mHomeActivity=getHomeActivity();
		mFirstCard = (ImageView) mView.findViewById(R.id.cartItemFirst);
		mSecondCard = (ImageView) mView.findViewById(R.id.cartItemSecond);
		mThirdCard = (ImageView) mView.findViewById(R.id.cartItemThird);
		mSpinBtn = (Button) mView.findViewById(R.id.spinToWinBtn);
		mWebView=(WebView) mView.findViewById(R.id.spinToWinWebView);		
	}

	private void initListener() {
		mSpinBtn.setOnClickListener(this);
	}	

	@Override
	public void onResume() {
		super.onResume();
		loadDefaultContent();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.spinToWinBtn:
			if(!isSpining){
				mGame=null;
				isSpining=true;
				mSpinBtn.setText(getActivity().getString(R.string.stop_spin_to_win_label));
				loadDefaultContent();
				getGameResult();
				spinToWin();
			}
//			else{
//				showSpinResult();
//			}
			break;

		default:
			break;
		}
	}

	private void showSpinResult() {
		checkGameResult();
		createContentGameResult();
		loadDataContent(mContent);
		isSpining=false;
		mSpinBtn.setText(getActivity().getString(R.string.spin_to_win_label));
		isWinner=true;
	}

	private void loadDefaultContent() {
		mContent=mHomeActivity.getString(R.string.spin_to_win_description);
		loadDataContent(mContent);
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void loadDataContent(String data){
		if(data==null){
			return;
		}		
		//mWebView.loadData("", "text/html; charset=UTF-8", null);
		String htmlData="<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head><body style=\"width:100%; height: 120px;background: #EDEDEF; text-align: center; color: #CB0078;\">"+data+"</body></html>";
		//String htmlData="<div style=\"background: #EDEDEF; text-align: center; color: #CB0078;\"><font color=\"#CB0078\">"+data+"</font><br/>";
		//mWebView.setText(Html.fromHtml(htmlData));
		//mWebView.loadData(htmlData, "text/html; charset=UTF-8", null);
		
		String mime = "text/html";
		String encoding = "utf-8";
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadDataWithBaseURL(null, htmlData, mime, encoding, null);
	}
	private void getGameResult(){
		if(mSpinToWinTask==null || mSpinToWinTask.getStatus()==Status.FINISHED){
			mSpinToWinTask=new SpinToWinTask();
			mSpinToWinTask.execute();
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
					if(mGameResult!=null && mGameResult.size()==3){
						mFirstCard.setBackgroundResource(imageArray[mGameResult.get(0)]);
						mSecondCard.setBackgroundResource(imageArray[mGameResult.get(1)]);
						mThirdCard.setBackgroundResource(imageArray[mGameResult.get(2)]);
						//loadDataContent(mContent);
						isWinner=false;
						handler.removeCallbacks(runnable);
					}else{
						int ii = rand.nextInt(3);
						mFirstCard.setBackgroundResource(imageArray[ii]);
						int ji = rand.nextInt(3);
						mSecondCard.setBackgroundResource(imageArray[ji]);
//						handler.postDelayed(this, 100); // for interval...
						int ki = rand.nextInt(3);
						mThirdCard.setBackgroundResource(imageArray[ki]);					
						handler.postDelayed(this, 50); // for interval...	
						if(ii!=ji || ii!=ki || ji!=ki){
							isWinner=false;
							handler.removeCallbacks(runnable);
						}
					}													
				}else{
					i = rand.nextInt(3);
					mFirstCard.setBackgroundResource(imageArray[i]);
					i = rand.nextInt(3);
					mSecondCard.setBackgroundResource(imageArray[i]);
					i = rand.nextInt(3);
					mThirdCard.setBackgroundResource(imageArray[i]);
					i++;
					handler.postDelayed(this, 50); // for interval...	
				}					
			}
		};
		
		handler.postDelayed(runnable, 50); // for initial delay..
		
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
            	showSpinResult();
            }
        }, 5000);
	}
	class SpinToWinTask extends AsyncTask<Void, Void, GameDTO>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected GameDTO doInBackground(Void... params) {
			return CommonModel.getInstance().getSpinToWin();
		}
		@Override
		protected void onPostExecute(GameDTO result) {
			super.onPostExecute(result);
			mGame=result;
			//showSpinResult();
		}
	}
	private void checkGameResult(){
		if(mGame==null){
			return;
		}
		if(mGameResult!=null){
			mGameResult.clear();
		}
		try {			
			String data = mGame.id.replace("[", "").replace("]", "");
			String[] result = data.split(",");
			for (int i = 0; i < result.length; i++) {
				int value=Integer.parseInt(result[i]);
				if(value>2){
					value=2;
				}
				mGameResult.add(value);
			}			
		} catch (Exception e) {
		}
	}
	private void createContentGameResult(){
		try {
			if(mGame!=null){
				mContent="<div style=\"margin: auto;position: relative;top: 25%;\">";
				if(mGame.msg.trim().equals("Successful")){					
					mContent="<b>CONGRATULATION!!!</b><br />";
					mContent+="Your code id: <b>"+ mGame.voucher+"</b><br/>";
					mContent+="Value: <b>$"+mGame.value+"</b>. <br/>Expire date: <b>"+ mGame.date+"</b><br/>";
					mContent+="You can use it on the next purchase.";
				}else{
					mContent="<b> LET'S TRY AGAIN TO GET VOUCHER :D </b>";
				}
				mContent+="</div>";
			}else{
				mContent=mHomeActivity.getString(R.string.spin_to_win_description);
			}
		} catch (Exception e) {
			mContent=mHomeActivity.getString(R.string.spin_to_win_description);
		}
	}
}
