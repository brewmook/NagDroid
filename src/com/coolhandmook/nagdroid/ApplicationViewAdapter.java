package com.coolhandmook.nagdroid;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

public class ApplicationViewAdapter implements ListAdapter
{
	private final PackageManager packageManager;
	private final List<ApplicationInfo> apps;
	private final Context applicationContext;

	public ApplicationViewAdapter(Context applicationContext) {
		this.applicationContext = applicationContext;
		this.packageManager = applicationContext.getPackageManager();
		this.apps = packageManager.getInstalledApplications(0);
	}
	
	public int getCount() {
		return apps.size();
	}

	public Object getItem(int position) {
		return apps.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Button view = (Button) convertView;
		if (view == null)
			view = new Button(applicationContext);
		
		view.setText(this.packageManager.getApplicationLabel(apps.get(position)));
		return view;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return apps.isEmpty();
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}

	public boolean areAllItemsEnabled() {
		return true;
	}

	public boolean isEnabled(int position) {
		return true;
	}
}