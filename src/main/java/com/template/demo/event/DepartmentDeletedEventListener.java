package com.template.demo.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.demo.messaging.DeletedDepartmentMessage;
import com.template.demo.messaging.MQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class DepartmentDeletedEventListener {

	private final MQSender mqSender;
	private final ObjectMapper mapper;

	@Autowired
	public DepartmentDeletedEventListener(final MQSender mqSender, final ObjectMapper mapper) {
		this.mqSender = mqSender;
		this.mapper = mapper;
	}

	@TransactionalEventListener
	public void handleTransactionCommitted(final DepartmentDeletedEvent event) {
		log.info("Received a department deleted event={}", event);

		final Integer eventDepartmentId = event.getDepartmentId();
		final DeletedDepartmentMessage deletedDepartmentMessage = DeletedDepartmentMessage.builder()
				.departmentId(eventDepartmentId).build();

		sendSbMessage(deletedDepartmentMessage);
	}

	private void sendSbMessage(final DeletedDepartmentMessage deletedDepartmentMessage) {
		log.info("Sending Message with departmentId={}", deletedDepartmentMessage.getDepartmentId());

		try {
			final String json = mapper.writeValueAsString(deletedDepartmentMessage);
			mqSender.sendMessage(json);
		} catch (JsonProcessingException e) {
			log.error("Failed to send message", e);
		}
	}
}
