package com.coolhandmook.nagdroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class ScheduledViewAdapter extends ArrayAdapter<ScheduledLaunch> {

	private Activity activity;
	private DateFormat dateFormatter;

	public ScheduledViewAdapter(Activity activity,
								int textViewResourceId,
								List<ScheduledLaunch> schedule)
	{
		super(activity, textViewResourceId, schedule);
		this.activity = activity;
		dateFormatter = SimpleDateFormat.getDateTimeInstance();
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
			row = activity.getLayoutInflater().inflate(R.layout.schedule_app_row, parent, false);
		}

		ApplicationInfo app = null;
		ScheduledLaunch launch = getItem(position);
		PackageManager packageManager = activity.getPackageManager();
		ImageView icon = (ImageView) row.findViewById(R.id.applicationIcon);
		
		try {
			icon.setImageDrawable(packageManager.getApplicationIcon(launch.packageName));
			app = packageManager.getApplicationInfo(launch.packageName, 0);
		} catch (NameNotFoundException e) {
			icon.setImageResource(R.drawable.ic_launcher);
			app = null;
		}

		TextView applicationName = (TextView) row.findViewById(R.id.scheduledApplicationName);
		applicationName.setText(packageManager.getApplicationLabel(app).toString());

		TextView time = (TextView) row.findViewById(R.id.scheduledTime);
		time.setText(dateFormatter.format(new Date(launch.time)));

		return row;
	}
}