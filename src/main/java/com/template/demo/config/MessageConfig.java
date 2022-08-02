package com.template.demo.config;

import com.template.demo.messaging.ActiveMQReceiver;
import com.template.demo.messaging.MQDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Slf4j
@Configuration
public class MessageConfig {

	@Bean
	public JmsTemplate jmsTemplate(final ConnectionFactory connectionFactory) {
		final JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	@Bean
	public ConnectionFactory connectionFactory(@Value("${activemq.broker.url}") final String brokerUrl,
			@Value("${activemq.username}") final String username,
			@Value("${activemq.password}") final String password) {

		final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerUrl);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);

		return connectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory topicJmsListenerContainerFactory(
			final ConnectionFactory connectionFactory, @Value("${activemq.clientId}") final String clientId,
			@Value("${activemq.durable}") final boolean durable) {

		final DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory);
		if (durable) {
			listenerContainerFactory.setSubscriptionDurable(Boolean.TRUE);
			listenerContainerFactory.setClientId(clientId);
		}

		return listenerContainerFactory;
	}

	@Bean
	public ActiveMQReceiver activeMQReceiver(final MQDispatcher mqDispatcher) {
		return new ActiveMQReceiver(mqDispatcher);
	}
}
