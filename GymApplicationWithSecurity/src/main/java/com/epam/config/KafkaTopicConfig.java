package com.epam.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic getReportTopic() {
		return TopicBuilder.name("report-topic")
						   .build();
	}
	
	@Bean
	public NewTopic getNotificationTopic() {
		return TopicBuilder.name("notificationtopic")
						   .build();
	}
	
}