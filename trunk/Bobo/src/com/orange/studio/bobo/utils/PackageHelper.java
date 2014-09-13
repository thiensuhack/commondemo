package com.orange.studio.bobo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class PackageHelper {
	private Context mContext = null;
	private static PackageHelper mInstance = null;

	private PackageHelper(Context context) {
		mContext = context;
	}

	public static PackageHelper getInstance(Context context) {
		if (mInstance == null)
			mInstance = new PackageHelper(context);
		return mInstance;
	}

	public void activeApp(String packName) {
		//boolean isInstalled = isInstalled(packName);

		Intent launchIntent = null;
//		if (isInstalled) {
//			launchIntent = mContext.getPackageManager()
//					.getLaunchIntentForPackage(packName);
//			mContext.startActivity(launchIntent);
//		} else {
			try {
				launchIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + packName));
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(launchIntent);
			} catch (android.content.ActivityNotFoundException anfe) {
				launchIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ packName));
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(launchIntent);
			}
		//}
	}

	public void activeApp(String packName, String actionLink) {
		boolean isInstalled = isInstalled(packName);

		Intent launchIntent = null;
		if (isInstalled) {
			launchIntent = mContext.getPackageManager()
					.getLaunchIntentForPackage(packName);
			mContext.startActivity(launchIntent);
		} else {
			try {
				launchIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse(actionLink + "&utm_medium=thegioitruyentranh"));
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(launchIntent);
			} catch (android.content.ActivityNotFoundException anfe) {
				launchIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ packName));
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(launchIntent);
			}
		}
	}

	public boolean isInstalled(String packName) {
		boolean is = false;

		PackageManager packageManager = mContext.getPackageManager();
		try {
			if (packageManager.getPackageInfo(packName,
					PackageManager.GET_ACTIVITIES) != null)
				is = true;
		} catch (NameNotFoundException e) {

		}

		return is;
	}
}
