package com.coolhandmook.nagdroid;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;
import android.widget.TimePicker;

public class NewNagActivity extends Activity {

	private ApplicationViewAdapter viewAdapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nag);
        
        viewAdapter = new ApplicationViewAdapter(this, R.id.applicationName, getPackageManager().getInstalledApplications(0));
        Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
        spinner.setAdapter(viewAdapter);
        
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }
    
    public void onNagSave(View view)
    {
    	Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
    	int selected = spinner.getLastVisiblePosition();
    	
    	ApplicationInfo application = viewAdapter.getItem(selected);
		Intent intent = getPackageManager().getLaunchIntentForPackage(application.packageName);
    	if (intent != null)
    	{
    		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        	
    		Intent scheduleNew = new Intent(this, NagService.class);
    		scheduleNew.setAction(NagService.SCHEDULE_NEW);
    		scheduleNew.putExtra(NagService.ARG_HOUR, timePicker.getCurrentHour());
    		scheduleNew.putExtra(NagService.ARG_MINUTE, timePicker.getCurrentMinute());
    		scheduleNew.putExtra(NagService.ARG_PACKAGE, application.packageName);
    		startService(scheduleNew);
    	}
    	
    	finish();
    }

    public void onNagCancel(View view)
    {
    	finish();
    }
    
}
