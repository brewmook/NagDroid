package com.coolhandmook.nagdroid;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }
    
    public void onScheduleNagClick(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
    	int selected = spinner.getLastVisiblePosition();
    	
    	ApplicationInfo application = viewAdapter.getItem(selected);
		Intent intent = getPackageManager().getLaunchIntentForPackage(application.packageName);
    	if (intent != null)
    	{
    		Intent scheduleNew = new Intent(this, NagService.class);
    		scheduleNew.setAction(NagService.SCHEDULE_NEW);
    		scheduleNew.putExtra(NagService.ARG_TIME, timeInMilliseconds());
    		scheduleNew.putExtra(NagService.ARG_PACKAGE, application.packageName);
    		startService(scheduleNew);
    	}
    }
    
    private long timeInMilliseconds()
    {
    	TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
    	Calendar calendar = new GregorianCalendar();
    	calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
    	calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
    	calendar.set(Calendar.SECOND, 0);
    	return calendar.getTimeInMillis();
    }
    
}
