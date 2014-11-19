package com.simpletaskmanager.activity.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haarman.listviewanimations.ArrayAdapter;
import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.data.Task.TaskStatus;
import com.simpletaskmanager.data.TaskList;
import com.simpletaskmanager.R;
import com.simpletaskmanager.utils.Utils;

public class TaskAdapter extends ArrayAdapter<Task> {
	private final Context m_context;
	
	public TaskAdapter(Context context, List<Task> objects) {
		super(objects);
		m_context = context;
		addAll(objects);
	}
	
	public void showTasksForStatus(TaskStatus status) {
		clear();
		addAll(TaskList.getInstance().getTasksForTaskStatus(status));
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout customRow = (RelativeLayout) convertView;

		if (customRow == null) {
			customRow = (RelativeLayout) LayoutInflater.from(m_context).inflate(R.layout.layout_task_row, parent, false);
		}
		
		final Task task = getItem(position);
		boolean overdue = false;

		TextView taskName = (TextView) customRow.findViewById(R.id.txtTaskRowTitle);
		TextView taskDeadline = (TextView) customRow.findViewById(R.id.txtTaskRowDeadline);
		TextView taskCategory = (TextView) customRow.findViewById(R.id.txtTaskRowCategory);
		TextView taskDescription = (TextView) customRow.findViewById(R.id.txtTaskRowDescriptions);

		View priority = (View) customRow.findViewById(R.id.shapePriority);
		taskName.setText(task.getTitle());
		taskCategory.setText("[ " + task.getCategory().toString() + " ]");
		
		switch(m_context.getResources().getConfiguration().orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			taskDescription.setVisibility(View.GONE);

			break;
			
		case Configuration.ORIENTATION_PORTRAIT:
			if (task.getDescription().length() > 0) {
				taskDescription.setVisibility(View.VISIBLE);
				taskDescription.setText(task.getDescription());
			}
			else {
				taskDescription.setVisibility(View.GONE);
				taskDescription.setText("");
			}

			break;
		}
		

		String dateText;
		Date tomorrow = Utils.getDateAfter(1);
		Date dayAfterTomorrow = Utils.getDateAfter(2);
		Date today = new Date(System.currentTimeMillis());
		
		switch(task.getCategory()) {
		case PERSONAL:
			taskCategory.setBackgroundColor(Color.CYAN);
			break;
			
		case WORK:
			taskCategory.setBackgroundColor(Color.YELLOW);
			break;
			
		case OTHERS:
			taskCategory.setBackgroundColor(Color.LTGRAY);
			break;
		}
		
		switch(task.getPriority()) {
		case LOW:
			((GradientDrawable)priority.getBackground()).setColor(m_context.getResources().getColor(R.color.priority_low));
			break;
		case MEDIUM:
			((GradientDrawable)priority.getBackground()).setColor(m_context.getResources().getColor(R.color.priority_medium));
			break;
		case HIGH:
			((GradientDrawable)priority.getBackground()).setColor(m_context.getResources().getColor(R.color.priority_high));		
			break;
		}
		
		switch(task.getStatus()) {
		case PENDING:
			if (task.getDueDate().before(today)) {
				dateText = m_context.getResources().getString(R.string.overdue);
				overdue = true;
			}
			else if (task.getDueDate().before(tomorrow) && (task.getDueDate().after(today))) {
				dateText = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(task.getDueDate());
			}
			else if ((task.getDueDate().after(tomorrow)) && (task.getDueDate().before(dayAfterTomorrow))) {
				dateText = m_context.getResources().getString(R.string.tomorrow);
			}
			else {
				dateText = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(task.getDueDate());
			}
					
			taskDeadline.setText(dateText);
			
			if (overdue) {
				taskName.setTextColor(Color.RED);
			}
			else {
				taskName.setTextColor(Color.BLACK);
			}			
			
			taskName.setPaintFlags(taskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
			
			break;
			
		case COMPLETED:
			taskDeadline.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(task.getCompletedDate()));
			taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

			taskName.setTextColor(Color.GREEN);
			taskDeadline.setTextColor(Color.GREEN);
			
			break;
		}
		
		return customRow;		
	}
	
	@Override
	public long getItemId(int position) {
		return getItem(position).getId().hashCode();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		for (int x = 0; x < getCount(); ++x) {
			buf.append("Index: " + x);
			buf.append(" Item: " + getItem(x).getTitle());
			buf.append("\n");
		}
		
		return buf.toString();
	}
}
