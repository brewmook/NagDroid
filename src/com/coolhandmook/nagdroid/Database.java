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
	
	private static final String SELECT_ALL_FROM_NAGS =
			"SELECT " + NAGS_COLUMN_HOUR + "," + NAGS_COLUMN_MINUTE + "," + NAGS_COLUMN_PACKAGE + ",ROWID"
			+ " FROM " + NAGS_TABLE;

	private SQLiteDatabase db;
	
	public Database(Context context)
	{
		DatabaseOpenHelper dbOpener = new DatabaseOpenHelper(context);
    	db = dbOpener.getWritableDatabase();
	}
	
	public void addNag(Nag launch)
	{
		db.insert(NAGS_TABLE, null, nagToContentValues(launch));
	}
	
	public void updateNag(Nag launch)
	{
		db.update(NAGS_TABLE, nagToContentValues(launch), "ROWID="+Integer.toString(launch.rowId), null);
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
				SELECT_ALL_FROM_NAGS
				+ " ORDER BY "
				+   NAGS_COLUMN_HOUR + " ASC, "
				+   NAGS_COLUMN_MINUTE + " ASC ",
				null);
		
		List<Nag> result = cursorToNags(cursor);
		cursor.close();
		
		return result;
	}

	public Nag findNagByRowId(int rowId)
	{
		Cursor cursor = db.rawQuery(
				SELECT_ALL_FROM_NAGS
				+ " WHERE ROWID=" + Integer.toString(rowId),
				null);
		
		List<Nag> matches = cursorToNags(cursor);
		cursor.close();

		Nag result = null;
		if (!matches.isEmpty())
		{
			result = matches.get(0);
		}
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

	private ContentValues nagToContentValues(Nag nag)
	{
		ContentValues values = new ContentValues();
		values.put(NAGS_COLUMN_HOUR, nag.hour);
		values.put(NAGS_COLUMN_MINUTE, nag.minute);
		values.put(NAGS_COLUMN_PACKAGE, nag.packageName);
		return values;
	}
	
}
