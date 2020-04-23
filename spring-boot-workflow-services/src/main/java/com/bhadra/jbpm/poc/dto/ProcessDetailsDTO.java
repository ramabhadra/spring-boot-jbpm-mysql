package com.bhadra.jbpm.poc.dto;

import java.io.Serializable;

public class ProcessDetailsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long pid;

	private String processId;

	private TaskDetailsDTO taskDetailsDto;

	public ProcessDetailsDTO() {

	}

	public ProcessDetailsDTO(long pid, String processId) {
		super();
		this.pid = pid;
		this.processId = processId;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public TaskDetailsDTO getTaskDetailsDto() {
		return taskDetailsDto;
	}

	public void setTaskDetailsDto(TaskDetailsDTO taskDetailsDto) {
		this.taskDetailsDto = taskDetailsDto;
	}

}
