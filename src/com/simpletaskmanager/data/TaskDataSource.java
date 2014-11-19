package com.simpletaskmanager.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.simpletaskmanager.data.Task.Category;
import com.simpletaskmanager.data.Task.TaskPriority;
import com.simpletaskmanager.data.Task.TaskStatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskDataSource {
	private SQLiteDatabase m_database;
	private Database m_dbHelper;
	private String[] m_allColumns = { Database.COLUMN_ID, 
										Database.COLUMN_UUID,
										Database.COLUMN_TITLE,
										Database.COLUMN_DESCRIPTION,
										Database.COLUMN_STATUS,
										Database.COLUMN_CREATION_DATE,
										Database.COLUMN_DUE_DATE,
										Database.COLUMN_DUE_COMPLETED_DATE,
										Database.COLUMN_CATEGORY,
										Database.COLUMN_PRIORITY,
										Database.COLUMN_EVENT_ID
	};
	
	public TaskDataSource(Context context) {
		m_dbHelper = new Database(context);
	}

	public void open() {
		m_database = m_dbHelper.getWritableDatabase();		
	}
	
	public void close() {
		m_dbHelper.close();
	}
	
	public long addTask(Task task) {
	    ContentValues values = new ContentValues();
	    values.put(Database.COLUMN_CATEGORY, task.getCategory().name());
	    values.put(Database.COLUMN_UUID, task.getId().toString());
	    values.put(Database.COLUMN_CREATION_DATE, task.getCreationTime().getTime());
	    values.put(Database.COLUMN_DESCRIPTION, task.getDescription());
	    values.put(Database.COLUMN_STATUS, task.getStatus().name());
	    values.put(Database.COLUMN_DUE_COMPLETED_DATE, task.getDueDate().getTime());
	    values.put(Database.COLUMN_DUE_DATE, task.getDueDate().getTime());
	    values.put(Database.COLUMN_EVENT_ID, task.getEventId());
	    values.put(Database.COLUMN_PRIORITY, task.getPriority().name());
	    values.put(Database.COLUMN_TITLE, task.getTitle());
	    
	    long insertId = m_database.insert(Database.TABLE_TASKS, null, values);
		
	    return insertId;
	}
	
	public long updateTask(Task task) {
	    ContentValues values = new ContentValues();
	    values.put(Database.COLUMN_CATEGORY, task.getCategory().name());
	    values.put(Database.COLUMN_UUID, task.getId().toString());
	    values.put(Database.COLUMN_CREATION_DATE, task.getCreationTime().getTime());
	    values.put(Database.COLUMN_DESCRIPTION, task.getDescription());
	    values.put(Database.COLUMN_STATUS, task.getStatus().name());
	    values.put(Database.COLUMN_DUE_COMPLETED_DATE, task.getDueDate().getTime());
	    values.put(Database.COLUMN_DUE_DATE, task.getDueDate().getTime());
	    values.put(Database.COLUMN_EVENT_ID, task.getEventId());
	    values.put(Database.COLUMN_PRIORITY, task.getPriority().name());
	    values.put(Database.COLUMN_TITLE, task.getTitle());

	    long updateId = m_database.update(Database.TABLE_TASKS, values, Database.COLUMN_ID + "='" + task.getDatabaseId() + "'", null);
	    
	    return updateId;
	}
	
	public long deleteTask(Task task) {
		long deleteId = m_database.delete(Database.TABLE_TASKS, Database.COLUMN_ID + "='" + task.getDatabaseId() + "'", null);
		
		return deleteId;
	}

	public ArrayList<Task> getTasksForStatus(TaskStatus status) {
		ArrayList<Task> tasks = new ArrayList<Task>();

		Cursor cursor = m_database.query(Database.TABLE_TASKS, m_allColumns,
				Database.COLUMN_STATUS + "='" + status.name() + "'", null, null, null, Database.COLUMN_DUE_DATE + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			tasks.add(task);
			cursor.moveToNext();
		}
		
		cursor.close();
		return tasks;
	}
	
	public Task getTaskForUid(UUID key) {
		Task task = null;

		Cursor cursor = m_database.query(Database.TABLE_TASKS, m_allColumns,
				Database.COLUMN_UUID + "='" + key.toString() + "'", null, null, null, null);

		if (cursor.moveToFirst()) {
			task = cursorToTask(cursor);	
		}
		
		
		cursor.close();
		return task;
	}	
	
	public ArrayList<Task> getTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();

		Cursor cursor = m_database.query(Database.TABLE_TASKS, m_allColumns,
				null, null, null, null, Database.COLUMN_DUE_DATE + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			tasks.add(task);
			cursor.moveToNext();
		}
		
		cursor.close();
		return tasks;
	}
	
	private Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setDatabaseId(cursor.getLong(0));
		task.setId(UUID.fromString(cursor.getString(1)));
		task.setTitle(cursor.getString(2));
		task.setDescription(cursor.getString(3));
		task.setStatus(TaskStatus.valueOf(cursor.getString(4)));
		task.setCreationDate(new Date(cursor.getLong(5)));
		task.setDueDate(new Date(cursor.getLong(6)));
		task.setCompletedDate(new Date(cursor.getLong(7)));
		task.setCategory(Category.valueOf(cursor.getString(8)));
		task.setPriority(TaskPriority.valueOf(cursor.getString(9)));
		task.setEventId(cursor.getLong(10));
		
		return task;
	}

}
