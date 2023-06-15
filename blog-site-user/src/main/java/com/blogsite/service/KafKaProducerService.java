package com.blogsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafKaProducerService 
{
	
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void userActions(String message,String topic) 
	{
		this.kafkaTemplate.send(topic, message);
	}
	
}
