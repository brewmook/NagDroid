package com.coolhandmook.nagdroid;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    	
    	Intent intent = getPackageManager().getLaunchIntentForPackage(viewAdapter.getItem(selected).packageName);
    	if (intent != null)
    	{
    		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    		alarmManager.set(AlarmManager.RTC, timeInMilliseconds(), pendingIntent);
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
