package com.blogsite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.blogsite.common.AppConstants;
import com.blogsite.entity.Blogs;
import com.blogsite.entity.MongoBlogs;
import com.blogsite.repository.MongoBlogRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafKaConsumerService 
{
	@Autowired MongoBlogRepository mongoRepo;
	
	@KafkaListener(topics = AppConstants.TOPIC_ADD_BLOG, groupId = AppConstants.GROUP_ID)
	public void consume1(Blogs blogs) {
		log.info(String.format(AppConstants.TOPIC_ADD_BLOG+"%s", blogs));
		MongoBlogs mongoBlogs = new MongoBlogs();
		BeanUtils.copyProperties(blogs, mongoBlogs);
		mongoRepo.save(mongoBlogs);
		
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_DELETE_BLOG, groupId = AppConstants.GROUP_ID)
	public void consume2(Blogs blogs) {
		log.info(String.format(AppConstants.TOPIC_DELETE_BLOG+"%s", blogs));
		Optional<MongoBlogs> mongoBlog = mongoRepo.findByBlogname(blogs.getBlogname());
		mongoRepo.delete(mongoBlog.get());
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_FINDALL_BLOGS, groupId = AppConstants.GROUP_ID)
	public void consume3(List<Blogs> blogs) {
		log.info(String.format(AppConstants.TOPIC_FINDALL_BLOGS+"%s", blogs));
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_FINDMY_BLOGS, groupId = AppConstants.GROUP_ID)
	public void consume4(List<Blogs> blogs) {
		log.info(String.format(AppConstants.TOPIC_FINDMY_BLOGS+"%s", blogs));
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_SEARCH_BLOG_CAT, groupId = AppConstants.GROUP_ID)
	public void consume5(List<Blogs> blogs) {
		log.info(String.format(AppConstants.TOPIC_SEARCH_BLOG_CAT+"%s", blogs));
	}
	
	@KafkaListener(topics = AppConstants.TOPIC_SEARCH_BLOG_CAT_RANGE, groupId = AppConstants.GROUP_ID)
	public void consume6(List<Blogs> blogs) {
		log.info(String.format(AppConstants.TOPIC_SEARCH_BLOG_CAT_RANGE+"%s", blogs));
	}
}
