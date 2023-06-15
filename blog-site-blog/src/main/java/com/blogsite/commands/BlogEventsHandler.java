package com.blogsite.commands;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blogsite.entity.Blogs;
import com.blogsite.repository.BlogRepository;

@Component
public class BlogEventsHandler {
	
	@Autowired BlogRepository blogRepository;
	@EventHandler
	public void on(BlogCreatedEvent blogCreatedEvent) {
		Blogs blogs = new Blogs();
		BeanUtils.copyProperties(blogCreatedEvent, blogs);
		blogRepository.save(blogs);
		
	}
}
