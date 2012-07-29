package com.coolhandmook.nagdroid;

import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NagService extends IntentService
{
	public final static String SCHEDULE_NEW = "com.coolhandmook.nagdroid.SCHEDULE_NEW";
	public final static String TRIGGER_ALARM = "com.coolhandmook.nagdroid.TRIGGER";

	public final static String ARG_TIME = "com.coolhandmook.nagdroid.TIME";
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
	protected void onHandleIntent(Intent intent)
	{
		if (intent.getAction() == SCHEDULE_NEW)
		{
			database.addSchedule(new ScheduledLaunch(intent.getLongExtra(ARG_TIME, 0),
													 intent.getStringExtra(ARG_PACKAGE)));
			checkSchedule();
		}
		else if (intent.getAction() == TRIGGER_ALARM)
		{
			checkSchedule();
		}
		updateAlarm();
	}
	
	private void checkSchedule()
	{
		long time = System.currentTimeMillis();
		long upperBound = time + 5000;

		List<ScheduledLaunch> schedule = database.findScheduledApplications(upperBound);
		for (int i = 0; i < schedule.size(); ++i)
		{
			launchApplication(schedule.get(i).packageName);
		}
		
		database.removeScheduledApplications(upperBound);
	}
	
	private void launchApplication(String packageName)
	{
//		Toast toast = Toast.makeText(this, "Faking app launch", Toast.LENGTH_SHORT);
//		toast.show();
		Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
    	startActivity(intent);
	}

	private void updateAlarm()
	{
		long currentTime = System.currentTimeMillis();
		ScheduledLaunch nextLaunch = database.nextScheduledLaunch(currentTime);
		if (nextLaunch != null)
		{
			//boolean blah = nextLaunch.time < currentTime;
			if (nextLaunch.time > 0)
			{
				Intent trigger = new Intent(this, NagService.class);
				trigger.setAction(TRIGGER_ALARM);
				AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				PendingIntent pendingIntent = PendingIntent.getService(this, 0, trigger, 0);
				alarmManager.set(AlarmManager.RTC, nextLaunch.time, pendingIntent);
			}
		}
	}
}
