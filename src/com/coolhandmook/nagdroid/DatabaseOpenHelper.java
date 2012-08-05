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
			db.execSQL("DROP TABLE " + Database.OLD_NAGS_TABLE + ";");
			createScheduleTable(db);
		}
	}
	
	private void createScheduleTable(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + Database.NAGS_TABLE + " ("
				+ Database.NAGS_COLUMN_HOUR + " INTEGER,"
				+ Database.NAGS_COLUMN_MINUTE + " INTEGER,"
			    + Database.NAGS_COLUMN_PACKAGE + " TEXT);");
	}
}
