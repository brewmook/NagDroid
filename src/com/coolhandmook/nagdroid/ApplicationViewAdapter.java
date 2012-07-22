package com.coolhandmook.nagdroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationViewAdapter extends ArrayAdapter<ApplicationInfo> {

	private Activity activity;

	public ApplicationViewAdapter(Activity activity,
								  int textViewResourceId,
								  List<ApplicationInfo> applications)
	{
		super(activity, textViewResourceId, filterApplications(activity, applications));
		this.activity = activity;
	}
	
	static private List<ApplicationInfo> filterApplications(Activity activity,
															List<ApplicationInfo> applications)
	{
		final PackageManager pm = activity.getPackageManager();
		List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();

		for (int i = 0; i < applications.size(); ++i)
		{
			ApplicationInfo app = applications.get(i);
			if (app.enabled
			    && pm.getLaunchIntentForPackage(app.packageName) != null)
			{
				apps.add(app);
			}
		}

		Collections.sort(apps, new Comparator<ApplicationInfo>() {
			public int compare(ApplicationInfo o1, ApplicationInfo o2) {
				String labelOne = o1.loadLabel(pm).toString();
				String labelTwo = o2.loadLabel(pm).toString();
				return labelOne.compareTo(labelTwo);
			}           
		});

		return apps;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	private View getCustomView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		if (row == null)
		{
			row = activity.getLayoutInflater().inflate(R.layout.application_row, parent, false);
		}

		PackageManager packageManager = activity.getPackageManager();
		ApplicationInfo app = getItem(position);

		TextView label = (TextView) row.findViewById(R.id.applicationName);
		label.setText(packageManager.getApplicationLabel(app).toString());

		ImageView icon = (ImageView) row.findViewById(R.id.applicationIcon);
		try {
			icon.setImageDrawable(packageManager.getApplicationIcon(app.packageName));
		} catch (NameNotFoundException e) {
			icon.setImageResource(R.drawable.ic_launcher);
		}

		return row;
	}
}