package com.coolhandmook.nagdroid;

public class Application
{
	public final String label;

	public Application(String label)
	{
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
}