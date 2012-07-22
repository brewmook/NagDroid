package com.coolhandmook.nagdroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class NewNag extends Activity {

    private ApplicationsModel applicationsModel;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nag);
        
        applicationsModel = new ApplicationsModel(getApplicationContext().getPackageManager());
        
        ListView listView = (ListView) findViewById(R.id.applicationsList);
        listView.setAdapter(new ApplicationViewAdapter(getApplicationContext(), applicationsModel.getApplications()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }

    
}
