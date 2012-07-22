package com.coolhandmook.nagdroid;

import android.content.Intent;

public class Application
{
	public final String label;
	public final Intent openIntent;

	public Application(String label, Intent openIntent)
	{
		this.label = label;
		this.openIntent = openIntent;
	}
	
	@Override
	public String toString() {
		return label;
	}
}