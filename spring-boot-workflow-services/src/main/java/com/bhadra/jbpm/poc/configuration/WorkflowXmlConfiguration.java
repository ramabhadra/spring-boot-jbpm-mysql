package com.bhadra.jbpm.poc.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = { "classpath:config/jee-tx-context.xml", "classpath:config/jpa-context.xml",
		"classpath:config/jbpm-context.xml" })
public class WorkflowXmlConfiguration {
}