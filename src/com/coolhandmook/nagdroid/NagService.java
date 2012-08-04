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
	public final static String SCHEDULE_NEW = "com.coolhandmook.nagdroid.SCHEDULE_NEW";
	public final static String SCHEDULE_REMOVE = "com.coolhandmook.nagdroid.SCHEDULE_REMOVE";
	public final static String TRIGGER_ALARM = "com.coolhandmook.nagdroid.TRIGGER";

	public final static String ARG_HOUR = "com.coolhandmook.nagdroid.HOUR";
	public final static String ARG_MINUTE = "com.coolhandmook.nagdroid.MINUTE";
	public final static String ARG_PACKAGE = "com.coolhandmook.nagdroid.PACKAGE";
	
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
		List<Nag> nags = null;
		if (intent.getAction() == SCHEDULE_NEW)
		{
			database.addSchedule(intentToNag(intent));
			nags = database.allNagsSortedByTime();
		}
		else if (intent.getAction() == SCHEDULE_REMOVE)
		{
			database.removeSchedule(intentToNag(intent));
			nags = database.allNagsSortedByTime();
		}
		else if (intent.getAction() == TRIGGER_ALARM)
		{
			nags = database.allNagsSortedByTime();
			launchApplicationsDueNow(nags);
		}
		updateAlarm(nags);
	}
	
	private Nag intentToNag(Intent intent)
	{
		return new Nag(intent.getIntExtra(ARG_HOUR, 0),
			   	       intent.getIntExtra(ARG_MINUTE, 0),
			   	       intent.getStringExtra(ARG_PACKAGE));
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
				trigger.setAction(TRIGGER_ALARM);
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
