package com.blogsite.service;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.blogsite.common.AppConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafKaConsumerService 
{
	
	@KafkaListener(topics = AppConstants.TOPIC_USER_REGISTER, groupId = AppConstants.GROUP_ID)
	public void consume1(String message) {
		log.info(String.format(AppConstants.TOPIC_USER_REGISTER+"User name is %s", message));
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_USER_LOGIN, groupId = AppConstants.GROUP_ID)
	public void consume2(String message) {
		log.info(String.format(AppConstants.TOPIC_USER_LOGIN+"User name is %s", message));
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_USER_REGISTER_FAIL, groupId = AppConstants.GROUP_ID)
	public void consume3(String message) {
		log.info(String.format(AppConstants.TOPIC_USER_REGISTER_FAIL+"%s", message));
	}
}
