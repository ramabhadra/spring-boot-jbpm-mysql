package com.bhadra.jbpm.poc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.jbpm.process.workitem.bpmn2.ServiceTaskHandler;
import org.jbpm.services.api.ProcessService;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.task.api.UserGroupCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhadra.jbpm.poc.dto.ProcessDetailsDTO;
import com.bhadra.jbpm.poc.dto.TaskDetailsDTO;

@Service
public class WahWorkflowServices {

	private KieSession kieSession;

	@Autowired
	private TaskService taskService;

	private RuntimeManager runtimeManager;

	private RuntimeEnvironment runtimeEnvironment;

	@Autowired
	private ProcessService processService;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private UserGroupCallback userGroupCallback;

	public KieSession getKieSession() {
		return kieSession;
	}

	public void setKieSession(KieSession kieSession) {
		this.kieSession = kieSession;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public RuntimeManager getRuntimeManager() {
		return runtimeManager;
	}

	public void setRuntimeManager(RuntimeManager runtimeManager) {
		this.runtimeManager = runtimeManager;
	}

	public RuntimeEnvironment getRuntimeEnvironment() {
		return runtimeEnvironment;
	}

	public void setRuntimeEnvironment(RuntimeEnvironment runtimeEnvironment) {
		this.runtimeEnvironment = runtimeEnvironment;
	}

	public ProcessService getProcessService() {
		return processService;
	}

	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public UserGroupCallback getUserGroupCallback() {
		return userGroupCallback;
	}

	public void setUserGroupCallback(UserGroupCallback userGroupCallback) {
		this.userGroupCallback = userGroupCallback;
	}

	private RuntimeManager buidRuntimeManager() {
		RuntimeEnvironment env = RuntimeEnvironmentBuilder.Factory.get().newClasspathKmoduleDefaultBuilder()
				.entityManagerFactory(this.entityManagerFactory).userGroupCallback(this.userGroupCallback)
				.persistence(true).get();

		System.out.println("RuntimeEnvironment :: " + env);
		return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(env);
	}

	private RuntimeEngine getRuntimeEngine() {
		return this.runtimeManager.getRuntimeEngine(EmptyContext.get());
	}

	private KieSession getkiesession() {
		if (this.runtimeManager == null) {
			this.runtimeManager = this.buidRuntimeManager();
			this.kieSession = this.getRuntimeEngine().getKieSession();

			WorkItemManager wim = this.kieSession.getWorkItemManager();
			wim.registerWorkItemHandler("Service Task", new ServiceTaskHandler());
		}
		return this.kieSession;
	}

	public ProcessDetailsDTO startProcess(String processId, Map<String, Object> params) {
		this.kieSession = this.getkiesession();

		ProcessInstance pi = this.kieSession.startProcess(processId, params);

		ProcessDetailsDTO dto = new ProcessDetailsDTO(pi.getId(), pi.getProcessId());

		return dto;
	}

	public ProcessDetailsDTO completeTask(long processInstanceId, String userId, String taskName) {

		List<Long> taskIds = taskService.getTasksByProcessInstanceId(processInstanceId);

		Task task = null;
		for (long taskid : taskIds) {
			task = completeTask(taskid, userId, taskName, new HashMap<String, Object>());
			if (task != null)
				break;
		}

		ProcessDetailsDTO processDetailsDto = new ProcessDetailsDTO();
		processDetailsDto.setPid(processInstanceId);

		if (task != null) {
			TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
			taskDetailsDto.setTaskId(task.getId());
			taskDetailsDto.setTaskName(task.getName());
			taskDetailsDto.setTaskStatus(task.getTaskData().getStatus().name());

			processDetailsDto.setTaskDetailsDto(taskDetailsDto);
		}

		return processDetailsDto;
	}

	public Task completeTask(long taskId, String userId, String taskName, Map<String, Object> data) {

		this.kieSession = this.getkiesession();

		Task returnTask = null;
		Task task = taskService.getTaskById(taskId);
		if ("Ready".equalsIgnoreCase(task.getTaskData().getStatus().name())
				&& taskName.equalsIgnoreCase(task.getName())) {
			this.taskService.claim(taskId, userId);
			this.taskService.start(taskId, userId);
			this.taskService.complete(taskId, userId, data);

			returnTask = taskService.getTaskById(taskId);
		}

		return returnTask;
	}

	public List<TaskSummary> getTasks(long processInstanceId) {

		this.kieSession = this.getkiesession();
		this.taskService = this.getRuntimeEngine().getTaskService();
		List<Status> statusLt = new ArrayList<Status>();
		List<TaskSummary> taskSummaryLt = this.taskService.getTasksByStatusByProcessInstanceId(processInstanceId,
				statusLt, "en-UK");

		return taskSummaryLt;
	}

}
