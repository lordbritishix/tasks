package com.simpletaskmanager.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.simpletaskmanager.utils.Utils;

public class Task {
	public enum TaskStatus {
		PENDING,
		COMPLETED
	}
	
	public enum TaskPriority {
		LOW,
		MEDIUM,
		HIGH
	}
	
	public enum Category {
		WORK,
		PERSONAL,
		OTHERS
	}
	
	private long m_databaseId;
	private UUID m_id;
	private String m_title;
	private String m_description;
	private TaskStatus m_status = TaskStatus.PENDING;
	private Date m_creationTime = new Date(System.currentTimeMillis());
	private Date m_dueDate = Utils.getDateAfterEndOfDay(1);
	private Date m_completedDate = Utils.getDateAfterEndOfDay(1);
	private Category m_category;
	private TaskPriority m_priority = TaskPriority.MEDIUM;
	private long m_eventId = -1L;
	
	public static String[] taskStatusToStringArray() {
		TaskStatus[] statuses = TaskStatus.values();
		String[] ret = new String[statuses.length];
		
		for (int x = 0; x < statuses.length; ++x) {
			ret[x] = statuses[x].toString();
		}
		
		return ret;
	}
	
	public Task() {
		m_id = UUID.randomUUID();
	}
	
	public Task(Task task) {
		setTask(task);
	}
	
	public void setDatabaseId(long id) {
		m_databaseId = id;
	}
	
	public long getDatabaseId() {
		return m_databaseId;
	}

	
	public void setEventId(long id) {
		m_eventId = id;
	}
	
	public long getEventId() {
		return m_eventId;
	}
	
	public void setTask(Task task) {
		m_id = task.m_id;
		m_title = task.m_title;
		m_description = task.m_description;
		m_status = task.m_status;
		m_dueDate = task.m_dueDate;
		m_category = task.m_category;
		m_priority = task.m_priority;

	}
	
	public void setId(UUID id) {
		m_id = id;
	}
	
	public UUID getId() {
		return m_id;
	}
	
	public void setCreationDate(Date date) {
		m_creationTime = date;
	}
	
	public Date getCreationDate() {
		return m_creationTime;
	}
	
	public String getTitle() {
		return m_title;
	}
	
	public void setTitle(String title) {
		m_title = title;
	}
	
	public String getDescription() {
		return m_description;
	}
	
	public void setDescription(String description) {
		m_description = description;
	}
	
	public Category getCategory() {
		return m_category;
	}
	
	public void setCategory(Category category) {
		m_category = category;
	}
	
	public TaskStatus getStatus() {
		return m_status;
	}
	
	public void setStatus(TaskStatus status) {
		m_status = status;
	}
	
	public Date getDueDate() {
		return m_dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		m_dueDate = dueDate;
	}
	
	public Date getCompletedDate() {
		return m_completedDate;
	}
	
	public void setCompletedDate(Date completedDate) {
		m_completedDate = completedDate;
	}

	public TaskPriority getPriority() {
		return m_priority;
	}
	
	public void setPriority(TaskPriority priority) {
		m_priority = priority;
	}
	
	public Date getCreationTime() {
		return m_creationTime;
	}
	
	@Override
	public String toString() {
		StringBuffer data = new StringBuffer();
		data.append(m_title);
		data.append("; ");
		data.append(m_description);
		data.append("; ");
		data.append(SimpleDateFormat.getInstance().format(m_dueDate));
		data.append("; ");
		data.append(m_status);
		data.append("; ");
		data.append(m_category);

		return data.toString();
	}
}
