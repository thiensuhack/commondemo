package com.orange.studio.bobo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.orange.studio.bobo.pushnotification.CommonUtilities;
import com.orange.studio.bobo.pushnotification.ServerUtilities;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = GCMIntentService.class.getSimpleName();

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}   

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "onRegistered: regId = " + registrationId);
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "onUnregistered: regId = " + registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String message = intent.getExtras().getString("message");
		Log.i(TAG, "onMessage: message = " + message);
		generateNotification(context, message);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
//		String message = getString(R.string.gcm_deleted, total);
//		Log.i(TAG, "onDeletedMessages: message = " + message);
		//generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "onError: errorId = " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.i(TAG, "onRecoverableError: errorId = " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	private static void generateNotification(Context context, String message) {
//		try { 
//			int icon = R.drawable.push;
//			String title = context.getString(R.string.app_name);
//			// long when = System.currentTimeMillis();
//			Bitmap largeIcon = null;
//			NotificationDTO notify = new NotificationDTO();
//			
//			//{type:"123",message:"<data here>"}
//			JSONObject jb=new JSONObject(message);
//			notify.type=jb.getString("type");
//			notify.message=jb.getString("msg");
//			
//			Intent notificationIntent = null;
//			int pushID = 1111;
//			notificationIntent = new Intent(context,
//					SplashScreenActivity.class);
//			title = context.getString(R.string.app_name);
//			message = notify.message;
//			pushID = 1112;
//			
//
//			NotificationManager notificationManager = (NotificationManager) context
//					.getSystemService(Context.NOTIFICATION_SERVICE);
//			// set intent so it does not start a new activity
//			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			PendingIntent intent = PendingIntent.getActivity(context, 0,
//					notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//					context)
//					// Set small icon
//					.setSmallIcon(icon)
//					// set large icon
//					.setLargeIcon(largeIcon)
//					//set Ticker
//					.setTicker(message)
//					// Set Title
//					.setContentTitle(title)
//					// Set Text
//					.setContentText(message)
//					//set Style
//					.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//					// Set PendingIntent into Notification
//					.setContentIntent(intent)
//					// Dismiss Notification
//					.setAutoCancel(true)
//					
//					.setDefaults(Notification.DEFAULT_LIGHTS);
//			
//
//			AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//
//		    switch (am.getRingerMode()) 
//		    {
//		        case AudioManager.RINGER_MODE_VIBRATE:
//		        	mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
//		            break;
//		        case AudioManager.RINGER_MODE_NORMAL:
//		        	mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//		            break;
//		        default:
//		        	mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//		     }
//  
//			notificationManager.notify(pushID, mBuilder.build());
//		} catch (Exception e) {
//			return;
//		}
	}

}
