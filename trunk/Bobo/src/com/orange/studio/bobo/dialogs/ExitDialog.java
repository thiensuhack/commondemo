package com.orange.studio.bobo.dialogs;

import android.content.Context;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity.MainHomeActivityHandler;
import com.zuzu.dialogs.BaseDialog;

public class ExitDialog extends BaseDialog {
	private MainHomeActivityHandler mHandler = null;

	public ExitDialog(Context context, MainHomeActivityHandler _handler) {
		super(context, context.getString(R.string.app_name),
				TYPE_2_BUTTON, R.layout.dialog_exit_confirm_layout);
		mHandler = _handler;
		setNegativeButtonTitle(R.string.dialog_exit_app_btn_Cancel);
		setPositiveButtonTitle(R.string.dialog_exit_app_btn_Ok);
		setDefaultButtonTitle(R.string.dialog_exit_app_btn_Ok);
		setOnDialogListener(new OnDialogListener() {
			@Override
			public void onPositiveButtonClicked() {
				dismiss();
				mHandler.exitApplication();
			}

			@Override
			public void onNegativeButtonClicked() {
				dismiss();
			}

			@Override
			public void onDefaultButtonClicked() {
				dismiss();
			}
		});
	}
}
