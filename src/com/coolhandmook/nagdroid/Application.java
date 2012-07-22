package com.coolhandmook.nagdroid;

import android.content.Intent;

public class Application
{
	public final String label;
	public final String packageName;

	public Application(String label, String packageName)
	{
		this.label = label;
		this.packageName = packageName;
	}
	
	@Override
	public String toString() {
		return label;
	}
}