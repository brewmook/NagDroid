package com.coolhandmook.nagdroid;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationViewAdapter extends ArrayAdapter<Application> {

	private Activity activity;

	public ApplicationViewAdapter(Activity activity,
								  int textViewResourceId,
								  List<Application> applications)
	{
		super(activity, textViewResourceId, applications);
		this.activity = activity;
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

		Application app = getItem(position);
		TextView label = (TextView) row.findViewById(R.id.applicationName);
		label.setText(app.label + " (" + position + " of " + getCount() + ")");

		ImageView icon = (ImageView) row.findViewById(R.id.applicationIcon);
		try {
			icon.setImageDrawable(activity.getPackageManager().getApplicationIcon(app.packageName));
		} catch (NameNotFoundException e) {
			icon.setImageResource(R.drawable.ic_launcher);
		}

		return row;
	}
}