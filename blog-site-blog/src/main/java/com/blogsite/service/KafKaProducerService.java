package com.blogsite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.blogsite.entity.Blogs;
import com.blogsite.entity.MongoBlogs;

@Service
public class KafKaProducerService 
{
	
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void addDelBlog(Blogs blog,String topic) 
	{
		this.kafkaTemplate.send(topic, blog);
	}
	
	public void findBlogs(List<MongoBlogs> blogs,String topic) 
	{
		this.kafkaTemplate.send(topic, blogs);
	}

	public void searchBlogs(List<MongoBlogs> blogs, String topic) {
		this.kafkaTemplate.send(topic, blogs);		
	}
	
}
