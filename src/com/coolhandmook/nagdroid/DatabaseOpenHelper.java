package com.coolhandmook.nagdroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	public DatabaseOpenHelper(Context context) {
        super(context, Database.NAME, null, Database.VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createScheduleTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion != newVersion)
		{
			db.execSQL("DROP TABLE " + Database.SCHEDULED_TABLE + ";");
			createScheduleTable(db);
		}
	}
	
	private void createScheduleTable(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + Database.SCHEDULED_TABLE + " ("
				+ Database.SCHEDULED_HOUR + " INTEGER,"
				+ Database.SCHEDULED_MINUTE + " INTEGER,"
			    + Database.SCHEDULED_PACKAGE + " TEXT);");
	}
}
