package com.coolhandmook.nagdroid;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {
	
	public static final String NAME = "nagdroid";
	public static final int VERSION = 2;

	public static final String SCHEDULED_TABLE = "scheduled";
	public static final String SCHEDULED_HOUR = "hour";
	public static final String SCHEDULED_MINUTE = "minute";
	public static final String SCHEDULED_PACKAGE = "package";

	private SQLiteDatabase db;
	
	public Database(Context context)
	{
		DatabaseOpenHelper dbOpener = new DatabaseOpenHelper(context);
    	db = dbOpener.getWritableDatabase();
	}
	
	public void addSchedule(Nag launch)
	{
		ContentValues values = new ContentValues();
		values.put(SCHEDULED_HOUR, launch.hour);
		values.put(SCHEDULED_MINUTE, launch.minute);
		values.put(SCHEDULED_PACKAGE, launch.packageName);
		db.insert(SCHEDULED_TABLE, null, values);
	}
	
	public void removeSchedule(Nag launch)
	{
		db.delete(SCHEDULED_TABLE,
				  SCHEDULED_HOUR + " = " + Integer.toString(launch.hour)
				  + " AND " + SCHEDULED_MINUTE + " = " + Integer.toString(launch.minute)
				  + " AND " + SCHEDULED_PACKAGE + " = '" + launch.packageName + "'",
				  null);
	}
	
	public List<Nag> allNagsSortedByTime()
	{
		Cursor cursor = db.rawQuery(
				"SELECT * from " + SCHEDULED_TABLE
				+ " ORDER BY "
				+   SCHEDULED_HOUR + " ASC, "
				+   SCHEDULED_MINUTE + " ASC ",
				null);
		
		List<Nag> result = cursorToNags(cursor);
		cursor.close();
		
		return result;
	}

	private List<Nag> cursorToNags(Cursor cursor)
	{
		List<Nag> result = new ArrayList<Nag>();
		if (cursor.moveToFirst())
		{
			do {
				result.add(cursorToNag(cursor));
			} while (cursor.moveToNext());
		}
		return result;
	}

	private Nag cursorToNag(Cursor cursor)
	{
		return new Nag(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
	}

	public void close()
	{
		db.close();
	}
	
}
