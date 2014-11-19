package com.simpletaskmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "tasks.db";
	private static final int DATABASE_VERSION = 1;	
	public static final String TABLE_TASKS = "tasks";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_UUID = "uuid";	
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_STATUS = "status";	
	public static final String COLUMN_CREATION_DATE = "creationDate";
	public static final String COLUMN_DUE_DATE = "dueDate";
	public static final String COLUMN_DUE_COMPLETED_DATE = "completedDate";
	public static final String COLUMN_CATEGORY= "category";
	public static final String COLUMN_PRIORITY = "priority";
	public static final String COLUMN_EVENT_ID = "eventId";

	private static final String DATABASE_CREATE = 
			"create table " 
			+ TABLE_TASKS + " (" 
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_UUID + " text not null, "			
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_DESCRIPTION + " text, "
			+ COLUMN_STATUS + " text, "			
			+ COLUMN_CREATION_DATE + " integer, "
			+ COLUMN_DUE_DATE + " integer, "
			+ COLUMN_DUE_COMPLETED_DATE + " integer, "			
			+ COLUMN_CATEGORY + " text, "			
			+ COLUMN_PRIORITY + " text, "			
			+ COLUMN_EVENT_ID + " integer"			
					+ ");";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		db.execSQL(DATABASE_CREATE);
	}

}
