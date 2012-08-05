package com.coolhandmook.nagdroid;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NagService extends IntentService
{
	public final static String UPDATE = "com.coolhandmook.nagdroid.UPDATE";
	public final static String TRIGGER = "com.coolhandmook.nagdroid.TRIGGER";

	private Database database;
	
	public NagService()
	{
		super("NagIntentService");
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();

    	database = new Database(this);
	}
	
	@Override
	public void onDestroy()
	{
		database.close();
		super.onDestroy();
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		List<Nag> nags = database.allNagsSortedByTime();
		if (intent.getAction() == TRIGGER)
		{
			launchApplicationsDueNow(nags);
		}
		updateAlarm(nags);
	}
	
	private void launchApplicationsDueNow(List<Nag> nags)
	{
		Calendar calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		Iterator<Nag> iterator = nags.iterator();
		while (iterator.hasNext())
		{
			Nag nag = iterator.next();
			if (hour == nag.hour && minute == nag.minute)
			{
				launchApplication(nag.packageName);
			}
		}
	}
	
	private void launchApplication(String packageName)
	{
		Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
    	startActivity(intent);
	}

	private void updateAlarm(List<Nag> nags)
	{
		if (nags != null)
		{
			Nag next = findNextNag(nags);
			if (next != null)
			{
				Intent trigger = new Intent(this, NagService.class);
				trigger.setAction(TRIGGER);
				AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				PendingIntent pendingIntent = PendingIntent.getService(this, 0, trigger, 0);
				alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime(next), pendingIntent);
			}
		}
	}
	
	private long alarmTime(Nag nag)
	{
		Calendar calendar = new GregorianCalendar();
		long currentTime = calendar.getTimeInMillis();

		calendar.set(Calendar.HOUR_OF_DAY, nag.hour);
		calendar.set(Calendar.MINUTE, nag.minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		if (currentTime > calendar.getTimeInMillis())
		{
			calendar.roll(Calendar.DAY_OF_YEAR, 1);
		}
		
		return calendar.getTimeInMillis();
	}
	
	private Nag findNextNag(List<Nag> nags)
	{
		Nag result = null;

		if (nags != null)
		{
			Calendar calendar = new GregorianCalendar();
			int clockTime = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE);

			Iterator<Nag> iterator = nags.iterator();
			while (iterator.hasNext())
			{
				Nag nag = iterator.next();
				int nagTime = (nag.hour * 60) + nag.minute;
				if (nagTime > clockTime)
				{
					result = nag;
					break;
				}
			}
			
			if (result == null && !nags.isEmpty())
			{
				result = nags.get(0);
			}
		}

		return result;
	}
}
