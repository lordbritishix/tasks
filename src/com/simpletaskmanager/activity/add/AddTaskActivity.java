package com.simpletaskmanager.activity.add;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView.BufferType;
import android.widget.TimePicker;
import android.widget.Toast;

import com.simpletaskmanager.activity.tasks.TasksActivity;
import com.simpletaskmanager.calendar.CalendarManager;
import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.data.TaskList;
import com.simpletaskmanager.data.Task.Category;
import com.simpletaskmanager.datetimepicker.DatePickerFragment;
import com.simpletaskmanager.datetimepicker.TimePickerFragment;
import com.simpletaskmanager.R;

public class AddTaskActivity extends Activity {
	protected Task m_task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    initializeTask();
	}

	protected void initializeTask() {
		Task t = new Task();
		
		String taskName = getIntent().getStringExtra(TasksActivity.PARAM_TASK_TITLE);
		String taskDescription = getIntent().getStringExtra(TasksActivity.PARAM_TASK_DESCRIPTION);

		t.setTitle(taskName != null ? taskName : "");
		t.setDescription(taskDescription != null ? taskDescription : "");
		
	    setTask(t);
	}
	
	protected void setTask(Task task) {
		m_task = task;
		
		EditText txtTaskTitle = (EditText) findViewById(R.id.txtAddTaskTitle);
		EditText txtTaskDescription = (EditText) findViewById(R.id.txtAddTaskDescription);
		final Button btnTaskDueDate = (Button) findViewById(R.id.btnAddTaskDueDate);
		final Button btnTaskDueTime = (Button) findViewById(R.id.btnAddTaskDueTime);

		txtTaskTitle.setText(m_task.getTitle(), BufferType.EDITABLE);
		txtTaskDescription.setText(m_task.getDescription(), BufferType.EDITABLE);
		btnTaskDueDate.setText(SimpleDateFormat.getDateInstance().format(m_task.getDueDate()));
		btnTaskDueDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    DatePickerFragment newFragment = new DatePickerFragment();
			    newFragment.setDate(m_task.getDueDate());
			    newFragment.setOnDateSetListener(new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						Calendar c = Calendar.getInstance();
						c.setTime(m_task.getDueDate());
						c.set(Calendar.YEAR, year);
						c.set(Calendar.MONTH, monthOfYear);
						c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

						if (c.getTime().before(new Date(System.currentTimeMillis()))) {
							Toast.makeText(AddTaskActivity.this, R.string.invalid_due_date, Toast.LENGTH_SHORT).show();
						}
						else {
							m_task.setDueDate(c.getTime());
							btnTaskDueDate.setText(SimpleDateFormat.getDateInstance().format(c.getTime()));
						}
					}
				});
			    
			    newFragment.show(getFragmentManager(), "datePicker");
		    }
		});
		
		btnTaskDueTime.setText(SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(m_task.getDueDate()));
		btnTaskDueTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    TimePickerFragment newFragment = new TimePickerFragment();
			    newFragment.setTime(m_task.getDueDate());
			    newFragment.setOnTimeSetListener(new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						Calendar c = Calendar.getInstance();
						c.setTime(m_task.getDueDate());
						c.set(Calendar.HOUR_OF_DAY, hourOfDay);
						c.set(Calendar.MINUTE, minute);

						if (c.getTime().before(new Date(System.currentTimeMillis()))) {
							Toast.makeText(AddTaskActivity.this, R.string.invalid_due_date, Toast.LENGTH_SHORT).show();
						}
						else {
							m_task.setDueDate(c.getTime());
							btnTaskDueTime.setText(SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(c.getTime()));
						}
					}
				});
			    newFragment.show(getFragmentManager(), "timePicker");
			}
		});

		Spinner spnPriority = (Spinner) findViewById(R.id.spnAddTaskPriority);
		
		final PriorityAdapter priority = new PriorityAdapter(this); 
		spnPriority.setAdapter(priority);
		spnPriority.setSelection(priority.getPosition(m_task.getPriority()));
		spnPriority.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				m_task.setPriority(priority.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		Spinner spnCategory = (Spinner) findViewById(R.id.spnAddTaskCategory);
		final ArrayAdapter<Category> category = new ArrayAdapter<Category>(
				this, android.R.layout.simple_list_item_1, Category.values());

		spnCategory.setAdapter(category);
		spnCategory.setSelection(category.getPosition(m_task.getCategory()));
		spnCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				m_task.setCategory(category.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_activity, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = false;
		switch(item.getItemId()) {
		case R.id.action_save: {
			ret = true;
			
			EditText txtTaskTitle = (EditText) findViewById(R.id.txtAddTaskTitle);
			if (txtTaskTitle.length() <= 0) {
				Toast.makeText(this, R.string.invalid_task, Toast.LENGTH_SHORT).show();
			}
			else {
				save();
				finish();
			}
			
			break;
		}
			
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		}
		
		return ret;
	}
	
	protected void save() {
		EditText txtTaskTitle = (EditText) findViewById(R.id.txtAddTaskTitle);
		EditText txtTaskDescription = (EditText) findViewById(R.id.txtAddTaskDescription);
		
		m_task.setTitle(txtTaskTitle.getText().toString());
		m_task.setDescription(txtTaskDescription.getText().toString());

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if (prefs.getBoolean(getResources().getString(R.string.settings_key_add_to_calendar), false)) {
			long id = CalendarManager.getInstance(this).addTaskToCalendar(m_task);
			if (id != -1) {
				m_task.setEventId(id);
			}
		}		
		
		TaskList.getInstance().addTask(m_task, true);
		Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
	}
}
