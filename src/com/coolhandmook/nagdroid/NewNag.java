package com.coolhandmook.nagdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewNag extends Activity {

    private ApplicationsModel applicationsModel;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nag);
        
        applicationsModel = new ApplicationsModel(getApplicationContext().getPackageManager());
        
        Spinner spinner = (Spinner) findViewById(R.id.applicationNameSpinner);
        
        ArrayAdapter<Application> adapter = new ArrayAdapter<Application>(this, android.R.layout.simple_spinner_item, applicationsModel.getApplications());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }

    
}
