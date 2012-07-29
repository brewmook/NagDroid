package com.coolhandmook.nagdroid;

public class ScheduledLaunch {

	public final long time;
	public final String packageName;
	
	public ScheduledLaunch(long time, String packageName)
	{
		this.time = time;
		this.packageName = packageName;
	}
	
}
