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

	public static final String ACTION_NEW = "ACTION_NEW";
	public static final String ACTION_EDIT = "ACTION_EDIT";

	public static final String EXTRA_EDIT_ID = "EXTRA_EDIT_ID";

	private ApplicationViewAdapter viewAdapter;
	private Database database;
	private int editId = 0;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nag);
        
        database = new Database(this);
        viewAdapter = new ApplicationViewAdapter(this, R.id.applicationName, getPackageManager().getInstalledApplications(0));
        Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
        spinner.setAdapter(viewAdapter);
        
        Calendar calendar = new GregorianCalendar();
        
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        
		Intent intent = getIntent();
        if (intent.getAction() == ACTION_EDIT)
        {
        	editId = intent.getIntExtra(EXTRA_EDIT_ID, 0);
        	Nag nag = database.findNagByRowId(editId);
        	if (nag != null)
        	{
        		timePicker.setCurrentHour(nag.hour);
        		timePicker.setCurrentMinute(nag.minute);
        		spinner.setSelection(findItemIndex(nag.packageName));
        	}
        }

        if (editId == 0)
        {
        	timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        }
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }
    
    public void onNagSave(View view)
    {
    	Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
    	int selected = spinner.getLastVisiblePosition();
    	
    	ApplicationInfo application = viewAdapter.getItem(selected);
    	if (getPackageManager().getLaunchIntentForPackage(application.packageName) != null)
    	{
    		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        	
    		Nag nag = new Nag(timePicker.getCurrentHour(),
    						  timePicker.getCurrentMinute(),
    						  application.packageName,
    						  editId);

    		if (editId == 0)
    		{
    			database.addNag(nag);
    		}
    		else
    		{
    			database.updateNag(nag);
    		}
    		
    		Intent intent = new Intent(this, NagService.class);
    		intent.setAction(NagService.UPDATE);
    		startService(intent);
    	}
    	
    	finish();
    }

    public void onNagCancel(View view)
    {
    	finish();
    }
    
    private int findItemIndex(String packageName)
    {
    	int result = 0;
    	for (int i = 0; i < viewAdapter.getCount(); ++i)
		{
			if (viewAdapter.getItem(i).packageName.equals(packageName))
			{
				result = i;
				break;
			}
		}
    	return result;
    }
    
}
