package com.simpletaskmanager.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;

import com.simpletaskmanager.data.Task.Category;
import com.simpletaskmanager.data.Task.TaskPriority;
import com.simpletaskmanager.data.Task.TaskStatus;
import com.simpletaskmanager.taskwriter.TaskWriter;
import com.simpletaskmanager.utils.RandomString;
import com.simpletaskmanager.utils.Utils;

public class TaskList {
	private static TaskList m_instance = new TaskList();
	private static TaskDataSource m_dataSource;

	private TaskList() {
	}
	
	public static void initialize(Context context) {
		if (m_dataSource == null) {
			m_dataSource = new TaskDataSource(context); 
			m_dataSource.open();
		}
	}
	
	public static TaskList getInstance() {
		return m_dataSource != null ? m_instance : null;
	}

	public static boolean exportToFile() {
		ArrayList<Task> ret = TaskList.getInstance().getTasks();
		Task[] tasks = new Task[ret.size()];

		return TaskWriter.getInstance().exportTasksToFile(TaskList.getInstance().getTasks().toArray(tasks));
	}
	
	public void generateTestDataForCount(int count) {
		RandomString randomTitle = new RandomString(50);
		RandomString randomDescription = new RandomString(300);
		for (int x = 0; x < count; ++x) {
			Task newTask = new Task();
			newTask.setTitle(randomTitle.nextString());
			newTask.setDescription(randomDescription.nextString());
			
			int priority = (int)(Math.random() * 3);
			newTask.setPriority(TaskPriority.values()[priority]);			
			
			int completed = (int)(Math.random() * 2);
			
			newTask.setStatus(completed > 0 ? TaskStatus.COMPLETED : TaskStatus.PENDING);
			
			int category = (int)(Math.random() * 3);
			newTask.setCategory(Category.values()[category]);			
			
			Date from = Utils.getDateAfter(-1);
			Date to = Utils.getDateAfter(10);
			newTask.setDueDate(Utils.getRandomDate(new Timestamp(from.getTime()), new Timestamp(to.getTime())));
						
			addTask(newTask, false);
		}
	}
	
	public Task getTaskForUUID(UUID key) {
		if (key == null) {
			return null;
		}
		
		return m_dataSource.getTaskForUid(key);
	}
	
	public boolean isSavedForUUID(UUID key) {
		return m_dataSource.getTaskForUid(key) != null;
	}
	
	public ArrayList<Task> getTasksForTaskStatus(TaskStatus status) {	
		ArrayList<Task> tasks = m_dataSource.getTasksForStatus(status);
		
		return tasks;
	}
	
	public ArrayList<Task> getTasks() {
		ArrayList<Task> tasks = m_dataSource.getTasks();

		return tasks;
	}

	
	public UUID addTask(Task task, boolean updateIfExisting) {
		if (updateIfExisting) {
			if (isSavedForUUID(task.getId())) {
				updateTask(task);
			}
			else {
				m_dataSource.addTask(task);
			}
		}
		else {
			m_dataSource.addTask(task);
		}
		
		return task.getId();
	}
	
	public long deleteTask(Task task) {
		return m_dataSource.deleteTask(task);
	}
	
	public long updateTask(Task task) {
		return m_dataSource.updateTask(task);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		
		ArrayList<Task> tasks = getTasks();
		
		for (Task task : tasks) {
			ret.append(task.toString());
			ret.append("\n");
		}
		
		return ret.toString();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
