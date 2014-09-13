package com.orange.studio.bobo.utils;

import java.security.MessageDigest;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceUtils {
	/**
	 * Get the unique device identify. This method require the permission
	 * android.permission.ACCESS_WIFI_STATE and
	 * android.permission.READ_PHONE_STATE
	 * 
	 * @param context
	 *            The context of activity or application
	 * @return The string contain the device identify or null
	 */
	public static String getDeviceId(Context context) {
		String deviceId = null;

		try {
			String macAddress = getMacAddress(context);
			String telDevId = getTelephoneDeviceId(context);
			String secureId = getAndroidSecureId(context);

			StringBuffer buffer = new StringBuffer();
			if (macAddress != null)
				buffer.append(macAddress);
			if (telDevId != null)
				buffer.append(telDevId);
			if (secureId != null)
				buffer.append(secureId);

			deviceId = md5(buffer.toString());
		} catch (Exception e) {
			// Logger
			// e.printStackTrace();
		}

		return deviceId;
	}

	/**
	 * Get the unique device identify. This method require the permission
	 * android.permission.ACCESS_WIFI_STATE and
	 * android.permission.READ_PHONE_STATE
	 * 
	 * @param context
	 *            The context of activity or application
	 * @param salt
	 *            The salt of calling method or null
	 * @return The string contain the device identify or null
	 */
	public static String getDeviceId(Context context, String salt) {
		String deviceId = null;

		try {
			String macAddress = getMacAddress(context);
			String telDevId = getTelephoneDeviceId(context);
			String secureId = getAndroidSecureId(context);

			StringBuffer buffer = new StringBuffer();
			if (salt != null)
				buffer.append(salt);
			if (macAddress != null)
				buffer.append(macAddress);
			if (telDevId != null)
				buffer.append(telDevId);
			if (secureId != null)
				buffer.append(secureId);

			deviceId = md5(buffer.toString());
		} catch (Exception e) {
			// Logger
			// e.printStackTrace();
		}

		return deviceId;
	}

	/**
	 * Get WiFi MAC address. This method require the permission
	 * android.permission.ACCESS_WIFI_STATE
	 * 
	 * @param context
	 *            The context of activity or application
	 * @return The string contain the MAC address or null
	 */
	public static String getMacAddress(Context context) {
		String macAddress = null;

		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifiManager.getConnectionInfo();
			macAddress = info.getMacAddress();
		} catch (Exception e) {
			// Logger
			// e.printStackTrace();
		}

		return macAddress;
	}

	/**
	 * Get telephony device identify. This method require the permission
	 * android.permission.READ_PHONE_STATE
	 * 
	 * @param context
	 *            The context of activity or application
	 * @return The string contain the telephony device identify or null
	 */
	public static String getTelephoneDeviceId(Context context) {
		String telDeviceId = null;

		try {
			TelephonyManager telManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			telDeviceId = telManager.getDeviceId();
		} catch (Exception e) {
			// Logger
			// e.printStackTrace();
		}

		return telDeviceId;
	}

	/**
	 * Get Android secure identify
	 * 
	 * @param context
	 *            The context of activity or application
	 * @return The string contain the Android secure identify or null
	 */
	public static String getAndroidSecureId(Context context) {
		String secureId = null;

		try {
			secureId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
		} catch (Exception e) {
			// Logger
			// e.printStackTrace();
		}

		return secureId;
	}

	/**
	 * Get the MD5 hash
	 * 
	 * @param data
	 *            The string need to be hashed
	 * @return The string contain MD5 hashed or null
	 */
	public static final String md5(final String data) {
		String md5 = null;

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(data.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			md5 = hexString.toString();

		} catch (Exception e) {
			// Logger
			// e.printStackTrace();
		}
		return md5;
	}
}
