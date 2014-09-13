package com.orange.studio.bobo.pushnotification;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class PushNotificationHelper {
	private static final String TAG = PushNotificationHelper.class
			.getSimpleName();

	private Context mContext = null;
	private AsyncTask<Void, Void, Void> mRegisterTask = null;
	private static PushNotificationHelper mInstance = null;

	private PushNotificationHelper(Context context) {
		mContext = context;
		GCMRegistrar.checkDevice(mContext);
		GCMRegistrar.checkManifest(mContext);
	}

	public static PushNotificationHelper getInstance(Context context) {
		if (mInstance == null)
			mInstance = new PushNotificationHelper(context);
		return mInstance;
	}

	public boolean isRegistered() {
		return GCMRegistrar.isRegisteredOnServer(mContext);
	}

	public void unregister() {
		GCMRegistrar.unregister(mContext);
	}

	public void register() {
		try {
			final String regId = GCMRegistrar.getRegistrationId(mContext);
			Log.d(TAG, "register: regId = " + regId);

			if (regId.equals("")) {
				Log.d(TAG, "Registration is not present, register now with GCM");
				GCMRegistrar.register(mContext, CommonUtilities.SENDER_ID);
			} else {
				if (GCMRegistrar.isRegisteredOnServer(mContext)) {
					Log.i(TAG, "Already registered with GCM");
				} else {
					final Context context = mContext;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							ServerUtilities.register(context, regId);
							return null;
						}
						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
						}
					};
					mRegisterTask.execute(null, null, null);
				}
			}
		} catch (Exception e) {
		}
	}
}
