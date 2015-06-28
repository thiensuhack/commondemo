package com.orange.studio.bobo.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.CarrierDTO;
import com.orange.studio.bobo.objects.VoucherResultDTO;

public class GiftCodeFragment extends BaseFragment implements OnClickListener,
		OnCheckedChangeListener {
	private EditText mVoucherCodeTxt = null;
	private Button mVoucherBtn = null;
	private View mGroupVourcher = null;
	private RadioGroup mRadioGroup = null;
	private RadioButton mNoVoucher = null;
	private RadioButton mHaveVoucher = null;

	private SubmitVoucherCodeTask mSubmitVoucherCodeTask = null;
	private String mGiftCode = null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_add_giftcode, container,
					false);
			initView();
			initListener();
		} else {
			if((ViewGroup) mView.getParent() != null){
				((ViewGroup) mView.getParent()).removeView(mView);
			}
		}
		return mView;
	}

	private void initView() {
		mHomeActivity = getHomeActivity();
		mRadioGroup = (RadioGroup) mView.findViewById(R.id.radioGroupVoucher);
		mGroupVourcher = (LinearLayout) mView.findViewById(R.id.groupVourcher);
		switchVoucherView(false);
		mVoucherCodeTxt = (EditText) mView.findViewById(R.id.txtVoucher);
		mVoucherBtn = (Button) mView.findViewById(R.id.voucherBtn);
		// mVoucherBtn.setVisibility(View.GONE);

		mNoVoucher = (RadioButton) mView.findViewById(R.id.radioNoVoucher);
		mHaveVoucher = (RadioButton) mView.findViewById(R.id.radioVoucher);

		initLoadingView();
		initNotFoundView();
	}

	private void switchVoucherView(boolean isShow) {
		mGroupVourcher.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
	}

	private void initListener() {
		// mVoucher.setOnItemSelectedListener(this);
		mVoucherBtn.setOnClickListener(this);
		// mRadioGroup.setOnCheckedChangeListener(this);
		mNoVoucher.setOnCheckedChangeListener(this);
		mHaveVoucher.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.radioNoVoucher:
			if (isChecked) {
				switchVoucherView(false);
			}
			break;
		case R.id.radioVoucher:
			if (isChecked) {
				switchVoucherView(true);
			}
			break;
		default:
			break;
		}
	}

	private void submitVoucherCode() {
		if (mSubmitVoucherCodeTask == null
				|| mSubmitVoucherCodeTask.getStatus() == Status.FINISHED) {
			mSubmitVoucherCodeTask = new SubmitVoucherCodeTask();
			mSubmitVoucherCodeTask.execute();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voucherBtn:
			if (mHaveVoucher.isChecked()) {
				mGiftCode = mVoucherCodeTxt.getText().toString();
				if (mGiftCode == null || mGiftCode.trim().length() < 3) {
					mHomeActivity.showToast(mHomeActivity
							.getString(R.string.voucher_fragment_invalid_code));
					mVoucherCodeTxt.setFocusable(true);
					return;
				} else {
					submitVoucherCode();
				}
			} else {
				goToSummaryFragment();
			}
			break;
		default:
			break;
		}
	}

	private void goToSummaryFragment() {
		mHomeActivity.onNavigationDrawerItemSelected(MENU_NAME.SUMMARY);
	}

	class SubmitVoucherCodeTask extends AsyncTask<Void, Void, VoucherResultDTO> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			switchView(false, true);
		}

		@Override
		protected VoucherResultDTO doInBackground(Void... params) {
			try {
				// id_cart=325&code=rVcmjP
				String url = UrlRequest.VOUCHER_URL + "&id_cart="
						+ mHomeActivity.getCurItemCart().id + "&code="
						+ mGiftCode;
				// String url=UrlRequest.GET_CARRIER_URL+"73";
				Log.i("CARRIER URL: ", url);
				return CommonModel.getInstance().submitVoucher(url);
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(VoucherResultDTO result) {
			super.onPostExecute(result);
			if (result != null && result.status == 1) {
				if (result.msg != null) {
					mHomeActivity.showToast(result.msg);
				}
				goToSummaryFragment();
			} else {
				if (result != null && result.msg != null) {
					mHomeActivity.showToast(result.msg);
				}
			}
			switchView(false, false);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// loadCarrier();
	}
}
