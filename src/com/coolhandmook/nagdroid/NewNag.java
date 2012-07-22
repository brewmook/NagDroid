package com.coolhandmook.nagdroid;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewNag extends Activity {

    private ApplicationsModel applicationsModel;
	private List<Application> applications;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nag);
        
        applicationsModel = new ApplicationsModel(getPackageManager());
        applications = applicationsModel.getApplications();
        
        Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
        spinner.setAdapter(new ApplicationViewAdapter(this, R.id.applicationName, applications));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }
    
    public void onApplicationOpen(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
    	int selected = spinner.getLastVisiblePosition();
    	
    	Intent intent = getPackageManager().getLaunchIntentForPackage(applications.get(selected).packageName);
    	if (intent != null)
    	{
    		startActivity(intent);
    	}
    }
    
}
