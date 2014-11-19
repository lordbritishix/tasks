package com.simpletaskmanager.activity.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.simpletaskmanager.R;
import com.simpletaskmanager.activity.add.AddTaskActivity;
import com.simpletaskmanager.activity.edit.EditTaskActivity;
import com.simpletaskmanager.activity.settings.SettingsActivity;
import com.simpletaskmanager.activity.tasks.CategoryFilterSpinnerAdapter.CategoryFilter;
import com.simpletaskmanager.activity.view.ViewTaskActivity;
import com.simpletaskmanager.calendar.CalendarManager;
import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.data.Task.TaskStatus;
import com.simpletaskmanager.data.TaskList;

public class TasksActivity extends Activity implements TasksFragmentListener {
	private static final int MAX_TITLE_CHAR = 40;
	public static final String PARAM_SELECTED_TASK = "com.simpletaskmanager.SelectedTask";
	public static final String PARAM_TASK_TITLE = "com.simpletaskmanager.Task.Title";
	public static final String PARAM_TASK_DESCRIPTION = "com.simpletaskmanager.Task.Description";

	private static final String KEY_IMAGE_URI = "image_uri";
	private static final String KEY_FILTER_CATEGORY = "filter_category";
	private static final String KEY_FILTER_COMPLETED = "filter_completed";

	public static final int INTENT_RESULT_FOR_VOICE = 0;

	private String m_filterCategorySelected = "";
	private String m_imgUri = null;
	private boolean m_filterIsCompleted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tasks);
		
		TaskList.initialize(getApplication());
		setupActionBar();
		
	    final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
	    String deviceid = tm.getDeviceId();
	    
	    AdRequest adRequest = new AdRequest.Builder()
        	.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        	.addTestDevice(deviceid)
        	.build();

	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    adView.loadAd(adRequest);
	}
	
	@Override
	protected void onResume() {
		applyFilter();

		super.onResume();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		m_filterCategorySelected = savedInstanceState.getString(KEY_FILTER_CATEGORY, "");
		m_filterIsCompleted = savedInstanceState.getBoolean(KEY_FILTER_COMPLETED, false);
		m_imgUri = savedInstanceState.getString(KEY_IMAGE_URI);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString(KEY_FILTER_CATEGORY, m_filterCategorySelected);
		outState.putBoolean(KEY_FILTER_COMPLETED, m_filterIsCompleted);
		outState.putString(KEY_IMAGE_URI, m_imgUri);
	}
	
	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.layout_action_bar_tasks);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		
		final Spinner category = (Spinner) actionBar.getCustomView().findViewById(R.id.spnActionBarCategory);
		final CategoryFilterSpinnerAdapter categoryAdapter = new CategoryFilterSpinnerAdapter(this);
		category.setAdapter(categoryAdapter);
		
		if (m_filterCategorySelected.length() <= 0) {
			category.setSelection(0);
		}
		else {
			category.setSelection(categoryAdapter.getPosition(CategoryFilter.valueOf(m_filterCategorySelected)));
		}
		
		category.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (m_filterCategorySelected != categoryAdapter.getItem(position).name()) {
					m_filterCategorySelected = categoryAdapter.getItem(position).name();
					applyFilter();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		final CheckBox chkCompleted = (CheckBox) actionBar.getCustomView().findViewById(R.id.chkActionBarCompleted);
		chkCompleted.setChecked(m_filterIsCompleted);
		chkCompleted.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (m_filterIsCompleted != isChecked) {
					m_filterIsCompleted = isChecked;
					applyFilter();
				}
			}
		});
	}
	
	private void applyFilter() {
		TasksFragment tasks = getTasksFragment();
		
		if (tasks != null) {
			tasks.getFilter().filter(String.format("%s\t%s", m_filterCategorySelected, String.valueOf(m_filterIsCompleted)));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tasks_activity, menu);
		
		return true;
	}
	
	private TasksFragment getTasksFragment() {
		TasksFragment tasks = null;
		tasks = (TasksFragment) TasksActivity.this.getFragmentManager().findFragmentById(R.id.tasksFragmentPortrait);
		
		return tasks;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean ret = false;
		switch(item.getItemId()) {
		case R.id.action_add: {
			ret  = true;
			Intent intent = new Intent(this, AddTaskActivity.class);
			startActivity(intent);
			
			break;
		}
		
		case R.id.action_settings: {
			ret = true;	
			startActivity(new Intent(this, SettingsActivity.class));
			
			break;
		}
		
		case R.id.action_voice: {
			try {
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
				
				startActivityForResult(intent, INTENT_RESULT_FOR_VOICE);
			}
			catch (ActivityNotFoundException e) {
				Toast.makeText(this, getResources().getString(R.string.voice_not_supported), Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
		
		default:
			ret = super.onMenuItemSelected(featureId, item);
			break;
		}
		return ret;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case INTENT_RESULT_FOR_VOICE:
			processVoice(resultCode, data);
			break;
			
		default:
			
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}
	
	private void processVoice(int resultCode, Intent data) {
		if ((resultCode == RESULT_OK) && (data != null))  {
			ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			if (text.size() > 0) {
				String match = text.get(0);
				Intent intent = new Intent(this, AddTaskActivity.class);
				intent.putExtra(PARAM_TASK_TITLE, match.length() <= MAX_TITLE_CHAR ? match : match.substring(0, 20) + "...");
				intent.putExtra(PARAM_TASK_DESCRIPTION, match);
				
				startActivity(intent);
			}
		}
	}
	
	@Override
	public void onTaskSelected(UUID taskId) {
		Task task = TaskList.getInstance().getTaskForUUID(taskId);
		showTaskOnActivity(task);
	}
		
	private void showTaskOnActivity(Task task) {
		Intent intent = null;
		switch(task.getStatus()) {
		case PENDING:
			intent = new Intent(this, EditTaskActivity.class);
			break;
			
		case COMPLETED:
			intent = new Intent(this, ViewTaskActivity.class);
			break;
		}
		
		if (intent != null) {
			intent.putExtra(PARAM_SELECTED_TASK, task.getId());
			startActivity(intent);
		}
	}

	@Override
	public void onTaskDismissed(UUID taskId) {
		Task task = TaskList.getInstance().getTaskForUUID(taskId);

		if (task != null) { 
			switch (task.getStatus()) {
			case PENDING:
				task.setStatus(TaskStatus.COMPLETED);
		    	task.setCompletedDate(new Date(System.currentTimeMillis()));
		    	TaskList.getInstance().updateTask(task);
		    	
		    	if (task.getEventId() != -1L) {
		    		CalendarManager.getInstance(this).updateTaskOnCalendar(task);
		    	}
		    	
				break;
				
			case COMPLETED:
				TaskList.getInstance().deleteTask(task);
				break;
			}
		}
	}

	@Override
	public void onTaskListReloaded(TaskAdapter adapter) {
	}

}
