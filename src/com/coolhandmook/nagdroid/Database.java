package com.coolhandmook.nagdroid;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {
	
	public static final String NAME = "nagdroid";
	public static final int VERSION = 1;

	public static final String SCHEDULED_TABLE = "scheduled";
	public static final String SCHEDULED_TIME = "time";
	public static final String SCHEDULED_PACKAGE = "package";

	private SQLiteDatabase db;
	
	public Database(Context context)
	{
		DatabaseOpenHelper dbOpener = new DatabaseOpenHelper(context);
    	db = dbOpener.getWritableDatabase();
	}
	
	public void addSchedule(ScheduledLaunch launch)
	{
		ContentValues values = new ContentValues();
		values.put(SCHEDULED_TIME, launch.time);
		values.put(SCHEDULED_PACKAGE, launch.packageName);
		db.insert(SCHEDULED_TABLE, null, values);
	}
	
	public void removeSchedule(ScheduledLaunch launch)
	{
		db.delete(SCHEDULED_TABLE,
				  SCHEDULED_TIME + " = " + Long.toString(launch.time)
				  + " AND " + SCHEDULED_PACKAGE + " = '" + launch.packageName + "'",
				  null);
	}
	
	public ScheduledLaunch nextScheduledLaunch(long asOfThisTime)
	{
		ScheduledLaunch result = null;
		Cursor cursor = db.rawQuery(
				"SELECT * from " + SCHEDULED_TABLE
				+ " WHERE " + SCHEDULED_TIME + " >= " + Long.toString(asOfThisTime)
				+ " ORDER BY " + SCHEDULED_TIME + " ASC"
				+ " LIMIT 1",
				null);
		
		if (cursor.moveToFirst())
		{
			result = cursorToScheduledLaunch(cursor);
		}

		cursor.close();
		
		return result;
	}

	public List<ScheduledLaunch> findScheduledApplications(long latest)
	{
		List<ScheduledLaunch> result = new ArrayList<ScheduledLaunch>();
		Cursor cursor = db.rawQuery(
				"SELECT * from " + SCHEDULED_TABLE
				+ " WHERE " + SCHEDULED_TIME + " <= " + Long.toString(latest)
				+ " ORDER BY " + SCHEDULED_TIME + " ASC",
				null);
		
		if (cursor.moveToFirst())
		{
			do {
				result.add(cursorToScheduledLaunch(cursor));
			} while (cursor.moveToNext());
		}

		cursor.close();
		
		return result;
	}
	
	public void removeScheduledApplications(long latest)
	{
		db.delete(SCHEDULED_TABLE, SCHEDULED_TIME + " <= " + Long.toString(latest), null);
	}

	private ScheduledLaunch cursorToScheduledLaunch(Cursor cursor)
	{
		return new ScheduledLaunch(cursor.getLong(0), cursor.getString(1));
	}

	public void close()
	{
		db.close();
	}
	
}
