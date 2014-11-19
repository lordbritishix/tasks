package com.simpletaskmanager.activity.tasks;

import java.util.UUID;

public interface TasksFragmentListener {
	public void onTaskSelected(UUID taskId);
	public void onTaskDismissed(UUID taskId);
	public void onTaskListReloaded(TaskAdapter adapter);
}
