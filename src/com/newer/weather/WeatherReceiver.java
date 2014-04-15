package com.newer.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class WeatherReceiver extends BroadcastReceiver {

	private static final String TAG = "WeatherReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		String rate = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("update_rate", "0");

		AlarmManager manager = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);

		Intent updateService = new Intent(context, UpdateService.class);
		//传进参数
		updateService.putExtra("RATE", rate);

		PendingIntent operation = PendingIntent.getService(context, 0,
				updateService, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), 1000 * Integer.parseInt(rate),
				operation);
		Log.d(TAG, "onReceive");
	}

}
