package com.coolhandmook.nagdroid;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;

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
    
    public void onApplicationOpen(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
    	int selected = spinner.getLastVisiblePosition();
    	
    	Intent intent = getPackageManager().getLaunchIntentForPackage(viewAdapter.getItem(selected).packageName);
    	if (intent != null)
    	{
    		startActivity(intent);
    	}
    }
    
}
