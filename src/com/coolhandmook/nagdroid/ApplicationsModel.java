package com.coolhandmook.nagdroid;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
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
			ApplicationInfo app = apps.get(i);
			if (app.enabled)
			{
				CharSequence label = packageManager.getApplicationLabel(app);
				Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);
				if (intent != null)
				{
					applications.add(new Application(label.toString(), app.packageName));
				}
			}
		}
		
		Collections.sort(applications, new Comparator<Application>() {
	        public int compare(Application o1, Application o2) {
	            return o1.label.compareTo(o2.label);
	        }           
	    });
		
		return applications;
	}

}
