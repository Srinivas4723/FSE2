package com.blogsite.commands;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blogsite.entity.User;
import com.blogsite.repository.UserRepository;

@Component
public class UserEventsHandler {
	
	@Autowired UserRepository userRepository;
	@EventHandler
	public void on(UserCreatedEvent blogCreatedEvent) {
		User user = new User();
		BeanUtils.copyProperties(blogCreatedEvent, user);
		userRepository.save(user);
		
	}
}
