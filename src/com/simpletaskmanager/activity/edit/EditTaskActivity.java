package com.simpletaskmanager.activity.edit;

import java.util.UUID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.simpletaskmanager.activity.add.AddTaskActivity;
import com.simpletaskmanager.activity.tasks.TasksActivity;
import com.simpletaskmanager.calendar.CalendarManager;
import com.simpletaskmanager.data.TaskList;
import com.simpletaskmanager.R;

public class EditTaskActivity extends AddTaskActivity {
	private UUID m_selectedUid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initializeTask() {
		m_selectedUid = (UUID) getIntent().getSerializableExtra(TasksActivity.PARAM_SELECTED_TASK);
		
	    if (m_selectedUid != null) {
	    	setTask(TaskList.getInstance().getTaskForUUID(m_selectedUid));
	    }
	}
	
	protected void save() {
		EditText txtTaskTitle = (EditText) findViewById(R.id.txtAddTaskTitle);
		EditText txtTaskDescription = (EditText) findViewById(R.id.txtAddTaskDescription);

		m_task.setTitle(txtTaskTitle.getText().toString());
		m_task.setDescription(txtTaskDescription.getText().toString());

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean(getResources().getString(R.string.settings_key_add_to_calendar), false)) {
			CalendarManager.getInstance(this).updateTaskOnCalendar(m_task);
		}		
		
		TaskList.getInstance().addTask(m_task, true);
		Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
		
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	
		MenuItem item = menu.findItem(R.id.action_email);
		item.setVisible(true);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = false;
		
		switch(item.getItemId()) {
		case R.id.action_email: {
			ret = true;
	
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, m_task.getTitle());
			intent.putExtra(Intent.EXTRA_TEXT, m_task.getDescription());

			startActivity(intent);
			
			break;
		}
			
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		}
		
		return ret;
	}
}
