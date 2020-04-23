package com.bhadra.jbpm.poc.dto;

import java.io.Serializable;

public class TaskDetailsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long taskId;

	private String taskName;

	private String taskStatus;

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

}
