package com.coolhandmook.nagdroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class NewNag extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nag);
        
        ListView listView = (ListView) findViewById(R.id.applicationsList);
        listView.setAdapter(new ApplicationViewAdapter(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_nag, menu);
        return true;
    }

    
}
