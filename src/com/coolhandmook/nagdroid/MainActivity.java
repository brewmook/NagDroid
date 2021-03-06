package com.coolhandmook.nagdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
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
        
        ListView list = (ListView) findViewById(R.id.scheduleList);
        list.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        	{
        		Intent intent = new Intent(MainActivity.this, NewNagActivity.class);
            	intent.setAction(NewNagActivity.ACTION_EDIT);
            	Nag nag = (Nag)parent.getItemAtPosition(position);
            	intent.putExtra(NewNagActivity.EXTRA_EDIT_ID, nag.rowId);
            	startActivity(intent);
        	}
		});
        registerForContextMenu(list);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
        viewAdapter = new ScheduledViewAdapter(this, R.id.scheduledApplicationName,
        									   database.allNagsSortedByTime());
        
        ListView list = (ListView) findViewById(R.id.scheduleList);
        list.setAdapter(viewAdapter);
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

    		database.removeNag(launch);
    		
    		Intent intent = new Intent(this, NagService.class);
    		intent.setAction(NagService.UPDATE);
    		startService(intent);

    		viewAdapter.remove(launch);
    		
    		result = true;
    	}

    	return result;
    }

    public void onCreateNewNagClick(View view)
    {
    	Intent intent = new Intent(this, NewNagActivity.class);
    	intent.setAction(NewNagActivity.ACTION_NEW);
    	startActivity(intent);
    }

}
