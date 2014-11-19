package com.simpletaskmanager.activity.tasks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filter;
import android.widget.Filterable;

import com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoAdapter;
import com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoAdapter.DeleteItemCallback;
import com.simpletaskmanager.activity.tasks.CategoryFilterSpinnerAdapter.CategoryFilter;
import com.simpletaskmanager.data.Task;
import com.simpletaskmanager.data.Task.Category;
import com.simpletaskmanager.data.Task.TaskStatus;
import com.simpletaskmanager.data.TaskList;
import com.simpletaskmanager.R;

public class TasksFragment extends ListFragment implements Filterable {
	private TaskAdapter m_taskListAdapter;
	private TasksFragmentListener m_listener;
	private TaskFilter m_filter = new TaskFilter();
	
	private class TaskFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults result = new FilterResults();
			List<Task> filteredTasks = new ArrayList<Task>();

			String[] filters = constraint.toString().split("\t");
			
			String category = filters[0];
			if (category.length() <= 0) {
				return result;
			}
			
			boolean isCompleted = Boolean.parseBoolean(filters[1]);
			
			List<Task> tasks = TaskList.getInstance().getTasksForTaskStatus(isCompleted ? TaskStatus.COMPLETED : TaskStatus.PENDING);
			
			Log.i("Info", "Filtering for: " + category);
			
			if (category.equalsIgnoreCase(CategoryFilter.ALL.name())) {
				for (Task task : tasks) {
					filteredTasks.add(task);
				}
			}
			else {
				for (Task task : tasks) {
					if (task.getCategory() == Category.valueOf(category)) {
						filteredTasks.add(task);
					}
				}
			}
			
			result.values = filteredTasks;
			result.count = filteredTasks.size();
			
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			m_taskListAdapter.clear();

			if (results.count <= 0) {
				m_taskListAdapter.notifyDataSetChanged();
			}
			else {
				@SuppressWarnings("unchecked")
				List<Task> filteredTasks = (List<Task>) results.values;

				m_taskListAdapter.addAll(filteredTasks);
				m_taskListAdapter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		m_taskListAdapter = new TaskAdapter(getActivity(), new ArrayList<Task>());
		
		ContextualUndoAdapter undoAdapter = new ContextualUndoAdapter(m_taskListAdapter, R.layout.layout_undo, R.id.btnUndo, 2000);
		undoAdapter.setAbsListView(getListView());
		undoAdapter.setDeleteItemCallback(new DeleteItemCallback() {
			@Override
			public void deleteItem(int position) {
		    	Task task  = m_taskListAdapter.getItem(position);
	    		m_taskListAdapter.remove(position);
	    		m_taskListAdapter.notifyDataSetChanged();
	    		
		    	m_listener.onTaskDismissed(task.getId());
			}
		});
		
		setListAdapter(undoAdapter);
	
//		SwipeDismissAdapter swipeAdapter = new SwipeDismissAdapter(m_taskListAdapter, new OnDismissCallback() {
//			
//			@Override
//			public void onDismiss(AbsListView arg0, int[] arg1) {
//				for (int position : arg1) {
//			    	Task task  = m_taskListAdapter.getItem(position);
//		    		m_taskListAdapter.remove(position);
//		    		m_taskListAdapter.notifyDataSetChanged();
//		    		
//			    	m_listener.onTaskDismissed(task.getId());
//				}
//			}
//		});
//		
//		swipeAdapter.setAbsListView(getListView());
//		setListAdapter(swipeAdapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				Task task = m_taskListAdapter.getItem(index);
				
				m_listener.onTaskSelected(task.getId());
			}
		});
		
		setEmptyText(getResources().getString(R.string.no_task));
	}
	
	@Override
	public void onResume() {		
		super.onResume();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		m_listener = (TasksFragmentListener) activity;
	}
	
	public void showTasksForStatus(TaskStatus status) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		sharedPref.edit().putBoolean(getResources().getString(R.string.settings_key_show_completed), 
										status == TaskStatus.COMPLETED ? true : false).commit();
	}
	
	@Override
	public Filter getFilter() {
		return m_filter;
	}
}
