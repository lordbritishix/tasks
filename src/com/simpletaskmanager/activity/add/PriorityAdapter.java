package com.simpletaskmanager.activity.add;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.data.Task.TaskPriority;
import com.simpletaskmanager.R;

public class PriorityAdapter extends ArrayAdapter<Task.TaskPriority> {

	public PriorityAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1, TaskPriority.values());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = (View) convertView;
		
		if (view == null) {
			view = (View) LayoutInflater.from(getContext()).inflate(R.layout.layout_priority, parent, false);
		}		
		
		TextView txtPriority = (TextView) view.findViewById(R.id.txtPriorityAdapter);
		View priority = (View) view.findViewById(R.id.shapePriorityAdapter);

		TaskPriority selected = TaskPriority.values()[position];
		txtPriority.setText(selected.toString());

		switch(selected) {
		case HIGH:
			((GradientDrawable)priority.getBackground()).setColor(getContext().getResources().getColor(R.color.priority_high));
			break;
			
		case MEDIUM:
			((GradientDrawable)priority.getBackground()).setColor(getContext().getResources().getColor(R.color.priority_medium));
			break;
			
		case LOW:
			((GradientDrawable)priority.getBackground()).setColor(getContext().getResources().getColor(R.color.priority_low));		
			break;
		}
		
		return view;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = (View) convertView;
		
		if (view == null) {
			view = (View) LayoutInflater.from(getContext()).inflate(R.layout.layout_priority, parent, false);
		}		
		
		TextView txtPriority = (TextView) view.findViewById(R.id.txtPriorityAdapter);
		View priority = (View) view.findViewById(R.id.shapePriorityAdapter);

		TaskPriority selected = TaskPriority.values()[position];
		txtPriority.setText(selected.toString());

		switch(selected) {
		case HIGH:
			((GradientDrawable)priority.getBackground()).setColor(getContext().getResources().getColor(R.color.priority_high));
			break;
			
		case MEDIUM:
			((GradientDrawable)priority.getBackground()).setColor(getContext().getResources().getColor(R.color.priority_medium));
			break;
			
		case LOW:
			((GradientDrawable)priority.getBackground()).setColor(getContext().getResources().getColor(R.color.priority_low));		
			break;
		}
		
		return view;
	}
	
}
