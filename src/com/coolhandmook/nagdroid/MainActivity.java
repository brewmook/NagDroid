package com.coolhandmook.nagdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ScheduledViewAdapter viewAdapter;
	private Database database;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        database = new Database(this);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
        viewAdapter = new ScheduledViewAdapter(this, R.id.scheduledApplicationName,
        									   database.allNagsSortedByTime());
        
        ListView list = (ListView) findViewById(R.id.scheduleList);
        list.setAdapter(viewAdapter);
        registerForContextMenu(list);
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
        database.close();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
    	super.onCreateContextMenu(menu, v, menuInfo);

    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.schedule_item_menu, menu);

    	TextView textView = (TextView) v.findViewById(R.id.scheduledApplicationName);
    	menu.setHeaderTitle(textView.getText());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
    	boolean result = false;

    	if (item.getItemId() == R.id.remove_schedule)
    	{
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    		Nag launch = viewAdapter.getItem(info.position);

    		Intent scheduleRemove = new Intent(this, NagService.class);
    		scheduleRemove.setAction(NagService.SCHEDULE_REMOVE);
    		scheduleRemove.putExtra(NagService.ARG_HOUR, launch.hour);
    		scheduleRemove.putExtra(NagService.ARG_MINUTE, launch.minute);
    		scheduleRemove.putExtra(NagService.ARG_PACKAGE, launch.packageName);
    		startService(scheduleRemove);

    		viewAdapter.remove(launch);
    		
    		result = true;
    	}

    	return result;
    }

    public void onCreateNewNagClick(View view)
    {
    	startActivity(new Intent(this, NewNagActivity.class));
    }

}
