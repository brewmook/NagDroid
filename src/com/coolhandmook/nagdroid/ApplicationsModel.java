package com.coolhandmook.nagdroid;

import java.util.List;
import java.util.Vector;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class ApplicationsModel {
	
	private final PackageManager packageManager;

	ApplicationsModel(PackageManager packageManager)
	{
		this.packageManager = packageManager;
	}
	
	List<Application> getApplications()
	{
		List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);

		List<Application> applications = new Vector<Application>();
		for (int i = 0; i < apps.size(); ++i)
		{
			CharSequence label = packageManager.getApplicationLabel(apps.get(i));
			applications.add(new Application(label));
		}
		
		return applications;
	}

}
