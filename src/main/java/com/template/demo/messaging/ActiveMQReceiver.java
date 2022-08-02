package com.template.demo.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ActiveMQReceiver {

	private final MQDispatcher mqDispatcher;

	public ActiveMQReceiver(final MQDispatcher mqDispatcher) {
		this.mqDispatcher = mqDispatcher;
	}

	@JmsListener(destination = "${activemq.receiver.topic}", containerFactory = "topicJmsListenerContainerFactory")
	public void receiveMessage(final Message message) throws JMSException {
		log.info("Received message from ActiveMQ messageId={}:", message.getJMSMessageID());
		try {
			final String content = extractPayload(message);
			log.info("Parsed message content={} for messageId={}", content, message.getJMSMessageID());
			mqDispatcher.dispatchMessage(content);
		} catch (JMSException ex) {
			log.error(String.format("Failed parsing ActiveMQ messageId=%s", message.getJMSMessageID()), ex);
		}
	}

	private String extractPayload(final Message message) throws JMSException {
		if (message instanceof ActiveMQBytesMessage) {

			final ActiveMQBytesMessage byteMessage = (ActiveMQBytesMessage) message;
			final byte[] byteData = new byte[(int) byteMessage.getBodyLength()];
			byteMessage.readBytes(byteData);
			byteMessage.reset();
			return new String(byteData, StandardCharsets.UTF_8);

		} else if (message instanceof ActiveMQTextMessage) {
			final ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
			return textMessage.getText();

		} else {
			throw new JMSException(String.format("Unknown ActiveMQ message type=%s", message.getJMSType()));
		}
	}
}
