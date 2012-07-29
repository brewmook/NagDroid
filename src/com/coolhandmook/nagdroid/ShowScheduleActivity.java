package com.coolhandmook.nagdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class ShowScheduleActivity extends Activity {

    private ScheduledViewAdapter viewAdapter;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_schedule);
        
        Database database = new Database(this);
        
        viewAdapter = new ScheduledViewAdapter(this, R.id.scheduledApplicationName,
        									   database.findScheduledApplications(Long.MAX_VALUE));
        
        ListView list = (ListView) findViewById(R.id.scheduleList);
        list.setAdapter(viewAdapter);
        
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_schedule, menu);
        return true;
    }
}
