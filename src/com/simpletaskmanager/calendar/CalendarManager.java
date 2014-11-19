package com.simpletaskmanager.calendar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;

import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.data.Task.TaskStatus;
import com.simpletaskmanager.R;

public class CalendarManager {
	public Context m_context;
	public static CalendarManager m_instance = new CalendarManager();
	
	private CalendarManager() {
		
	}
	
	public static CalendarManager getInstance(Context context) {
		m_instance.m_context = context;
		return m_instance;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public long addTaskToCalendar(Task task) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(m_context);
	    long id = Long.parseLong(prefs.getString(m_context.getResources().getString(R.string.key_calendar), "-1"));
	    
	    if (id <= 0L) {
	    	return id;
	    }

		Calendar beginTime = Calendar.getInstance();
		beginTime.setTime(task.getDueDate());
		
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(task.getDueDate());
		endTime.add(Calendar.MINUTE, 10);

	    ContentResolver contentResolver = m_context.getContentResolver();
	    
	    ContentValues calEvent = new ContentValues();
	    calEvent.put(Events.CALENDAR_ID, id);
	    calEvent.put(Events.TITLE, task.getTitle());
	    calEvent.put(Events.DESCRIPTION, task.getDescription());
	    calEvent.put(Events.DTSTART, beginTime.getTimeInMillis());
	    calEvent.put(Events.DTEND, endTime.getTimeInMillis());
	    calEvent.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

	    Uri uri = contentResolver.insert(Events.CONTENT_URI, calEvent);
	        
	    // The returned Uri contains the content-retriever URI for 
	    // the newly-inserted event, including its id
	    long eventId = Long.parseLong(uri.getLastPathSegment());
	    
	    ContentValues calReminder = new ContentValues();
	    calReminder.put(Reminders.EVENT_ID, eventId);
	    calReminder.put(Reminders.MINUTES, 10);
	    calReminder.put(Reminders.METHOD, Reminders.METHOD_ALERT);
	    contentResolver.insert(Reminders.CONTENT_URI, calReminder);
	    
	    return eventId;
	}
	
	public long updateTaskOnCalendar(Task task) {
		long id = task.getEventId();
		
		if (id == -1L) {
			return id;
		}
		
		if (task.getStatus() == TaskStatus.PENDING) {
			Calendar beginTime = Calendar.getInstance();
			beginTime.setTime(task.getDueDate());
			
			Calendar endTime = Calendar.getInstance();
			endTime.setTime(task.getDueDate());
			endTime.add(Calendar.MINUTE, 10);			
			
		    ContentResolver contentResolver = m_context.getContentResolver();
		    
		    ContentValues calEvent = new ContentValues();
		    calEvent.put(Events.TITLE, task.getTitle());
		    calEvent.put(Events.DESCRIPTION, task.getDescription());
		    calEvent.put(Events.DTSTART, beginTime.getTimeInMillis());
		    calEvent.put(Events.DTEND, endTime.getTimeInMillis());
		    calEvent.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());		
			String[] selArgs = new String[]{Long.toString(id)};
			id = contentResolver.update(Events.CONTENT_URI, calEvent, Events._ID + " =? ", selArgs);
		}
		else {
			String[] selArgs = new String[]{Long.toString(id)};
			id = m_context.getContentResolver().delete(Events.CONTENT_URI, Events._ID + " =? ", selArgs);
		}
		
		return id;
	}
	
	public HashMap<Long, String> getCalendars() {
		HashMap<Long, String> data = new HashMap<Long, String>();
		
		String[] projection = new String[] { Calendars._ID, Calendars.NAME, Calendars.ACCOUNT_NAME, Calendars.ACCOUNT_TYPE };
		
		Cursor calCursor = m_context.getContentResolver().query(Calendars.CONTENT_URI, projection, Calendars.VISIBLE + " = 1", null, Calendars._ID + " ASC");
		if (calCursor.moveToFirst()) {
			do {
				long id = calCursor.getLong(0);
				String displayName = calCursor.getString(1);
				data.put(Long.valueOf(id), displayName);
			} while (calCursor.moveToNext());
		}

		calCursor.close();
		return data;
	}
}
