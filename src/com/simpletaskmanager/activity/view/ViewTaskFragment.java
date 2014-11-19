package com.simpletaskmanager.activity.view;

import java.text.SimpleDateFormat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.R;

public class ViewTaskFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_view_task, container, false);
	}
	
	public void setTask(Task task) {
		if (task == null) {
			return;
		}
		
		TextView txtTaskTitle = (TextView) getView().findViewById(R.id.txtTaskTitle);
		TextView txtTaskDescription = (TextView) getView().findViewById(R.id.txtTaskDescription);
		TextView txtTaskStatus = (TextView) getView().findViewById(R.id.txtTaskStatus);
		TextView txtTaskPriority = (TextView) getView().findViewById(R.id.txtTaskPriority);
		TextView txtTaskDueDate = (TextView) getView().findViewById(R.id.txtTaskDueDate);
		TextView txtTaskCategory = (TextView) getView().findViewById(R.id.txtTaskCategory);

		txtTaskTitle.setText(task.getTitle());
		txtTaskDescription.setText(task.getDescription());
		txtTaskStatus.setText(task.getStatus().toString());
		txtTaskPriority.setText(task.getPriority().toString());
		txtTaskDueDate.setText(SimpleDateFormat.getDateInstance().format(task.getDueDate()) + " " + 
								SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(task.getDueDate()));
		txtTaskCategory.setText(task.getCategory().toString());
		
		getView().scrollTo(0, 0);
	}	
}
