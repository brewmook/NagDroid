package com.coolhandmook.nagdroid;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {
	
	public static final String NAME = "nagdroid";
	public static final int VERSION = 3;

	public static final String OLD_NAGS_TABLE = "scheduled";
	public static final String NAGS_TABLE = "nags";
	public static final String NAGS_COLUMN_HOUR = "hour";
	public static final String NAGS_COLUMN_MINUTE = "minute";
	public static final String NAGS_COLUMN_PACKAGE = "package";

	private SQLiteDatabase db;
	
	public Database(Context context)
	{
		DatabaseOpenHelper dbOpener = new DatabaseOpenHelper(context);
    	db = dbOpener.getWritableDatabase();
	}
	
	public void addNag(Nag launch)
	{
		ContentValues values = new ContentValues();
		values.put(NAGS_COLUMN_HOUR, launch.hour);
		values.put(NAGS_COLUMN_MINUTE, launch.minute);
		values.put(NAGS_COLUMN_PACKAGE, launch.packageName);
		db.insert(NAGS_TABLE, null, values);
	}
	
	public void removeNag(Nag launch)
	{
		db.delete(NAGS_TABLE,
				  "ROWID= " + Integer.toString(launch.rowId),
				  null);
	}
	
	public List<Nag> allNagsSortedByTime()
	{
		Cursor cursor = db.rawQuery(
				"SELECT " + NAGS_COLUMN_HOUR + "," + NAGS_COLUMN_MINUTE + "," + NAGS_COLUMN_PACKAGE + ",ROWID"
				+ " FROM " + NAGS_TABLE
				+ " ORDER BY "
				+   NAGS_COLUMN_HOUR + " ASC, "
				+   NAGS_COLUMN_MINUTE + " ASC ",
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
		return new Nag(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3));
	}

	public void close()
	{
		db.close();
	}
	
}
