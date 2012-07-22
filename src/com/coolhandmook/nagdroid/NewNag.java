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
        
        applicationsModel = new ApplicationsModel(getApplicationContext().getPackageManager());
        applications = applicationsModel.getApplications();
        
        Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
        
        ArrayAdapter<Application> adapter = new ArrayAdapter<Application>(this, android.R.layout.simple_spinner_item, applications);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }
    
    public void onApplicationOpen(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
    	int selected = spinner.getLastVisiblePosition();
    	Intent intent = applications.get(selected).openIntent;
    	startActivity(intent);
    }
    
}
