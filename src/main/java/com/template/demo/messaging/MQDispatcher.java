package com.template.demo.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.demo.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQDispatcher {

	private final EmployeeRepository employeeRepository;
	private final ObjectMapper objectMapper;

	@Autowired
	public MQDispatcher(final EmployeeRepository employeeRepository, final ObjectMapper objectMapper) {
		this.employeeRepository = employeeRepository;
		this.objectMapper = objectMapper;
	}

	public void dispatchMessage(final String message) {
		try {
			final DeletedDepartmentMessage deletedDepartmentMessage = objectMapper.readValue(message,
					DeletedDepartmentMessage.class);
			final Integer departmentId = deletedDepartmentMessage.getDepartmentId();

			final Integer count = employeeRepository.deleteByDepartmentId(departmentId);
			log.info("Successfully deleted a total of {} employees", count);

		} catch (JsonProcessingException e) {
			log.error("Unable to read message", e);
		}
	}
}
