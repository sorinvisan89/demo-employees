package com.template.demo.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;

@Slf4j
public class MQSender {

	private final JmsTemplate jmsTemplate;

	private final String senderTopic;

	public MQSender(final JmsTemplate jmsTemplate, final String senderTopic) {
		this.senderTopic = senderTopic;
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMessage(final String message) {
		log.info("Sending message to ActiveMQ with content={}", message);
		jmsTemplate.convertAndSend(senderTopic, message);
	}
}
