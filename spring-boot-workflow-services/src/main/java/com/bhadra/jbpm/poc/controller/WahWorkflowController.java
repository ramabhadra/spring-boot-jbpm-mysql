package com.bhadra.jbpm.poc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bhadra.jbpm.poc.dto.ProcessDetailsDTO;
import com.bhadra.jbpm.poc.services.WahWorkflowServices;

@RestController
@RequestMapping("/wah-workflow-services")
public class WahWorkflowController {

	@Autowired
	private WahWorkflowServices wahServices;

	@RequestMapping(value = "/start-process", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ProcessDetailsDTO startProcess(@RequestParam(value = "processId", required = true) String processId,
			@RequestParam(value = "userName", required = true) String userName) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", userName);

		return wahServices.startProcess(processId, params);
	}

	@RequestMapping(value = "/complete-task", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ProcessDetailsDTO completeTask(
			@RequestParam(value = "processInstanceId", required = true) long processInstanceId,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "taskName", required = true) String taskName) {

		ProcessDetailsDTO dto = wahServices.completeTask(processInstanceId, userName, taskName);

		return dto;
	}

}
