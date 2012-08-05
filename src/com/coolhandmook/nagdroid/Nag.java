package com.coolhandmook.nagdroid;

public class Nag
{
	public final int hour;
	public final int minute;
	public final String packageName;
	public final int rowId;
	
	public Nag(int hour, int minute, String packageName)
	{
		this.hour = hour;
		this.minute = minute;
		this.packageName = packageName;
		this.rowId = 0;
	}

	public Nag(int hour, int minute, String packageName, int rowId)
	{
		this.hour = hour;
		this.minute = minute;
		this.packageName = packageName;
		this.rowId = rowId;
	}
}
