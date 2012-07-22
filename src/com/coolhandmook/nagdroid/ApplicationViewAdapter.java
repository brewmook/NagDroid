package com.coolhandmook.nagdroid;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

public class ApplicationViewAdapter implements ListAdapter
{
	private final Context applicationContext;
	private List<Application> applications;

	public ApplicationViewAdapter(Context applicationContext, List<Application> applications) {
		this.applicationContext = applicationContext;
		this.applications = applications;
	}
	
	public int getCount() {
		return applications.size();
	}

	public Object getItem(int position) {
		return applications.get(position);
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
		
		view.setText(applications.get(position).label);
		return view;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return applications.isEmpty();
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