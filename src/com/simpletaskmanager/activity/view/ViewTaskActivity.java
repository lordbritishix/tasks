package com.simpletaskmanager.activity.view;

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.simpletaskmanager.activity.tasks.TasksActivity;
import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.data.TaskList;
import com.simpletaskmanager.R;

public class ViewTaskActivity extends Activity {
	private Task m_task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);		
	    
		Intent intent = getIntent();
		
		UUID selectedId = (UUID)intent.getSerializableExtra(TasksActivity.PARAM_SELECTED_TASK);
		m_task = TaskList.getInstance().getTaskForUUID(selectedId);
		
		ViewTaskFragment fragment = (ViewTaskFragment)getFragmentManager().findFragmentById(R.id.viewTaskFragment);
		if (fragment != null) {
			fragment.setTask(m_task);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_activity, menu);
		
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
