package com.blogsite.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blogsite.entity.Blogs;
import com.blogsite.entity.MongoBlogs;
import com.blogsite.repository.BlogRepository;
import com.blogsite.repository.MongoBlogRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BlogQueryHandler {
	@Autowired MongoBlogRepository blogRepository;
	List<Blogs> blogs = new ArrayList<>();
	
	@QueryHandler
	public List<MongoBlogs> handle(GetAllBlogsQuery getAllBlogsQuery){
		
		//BeanUtils.copyProperties(,blogs);
			
			return blogRepository.findAll();
		
			//return blogRepository.findAllByUserid(getAllQuery.getUserid());
		
	}
	@QueryHandler
	public List<MongoBlogs> handle1(GetMyBlogsQuery getAllQuery){
		
			return blogRepository.findAll();
		//BeanUtils.copyProperties(blogRepository.findAllByUserid(getAllQuery.getUserid()),blogs);
		//return blogs;
		
	}
	@QueryHandler
	public String handle2(DeleteBlogQuery deleteBlogQuery){
		log.info("DELETE");
		//	Optional<MongoBlogs> mongoBlog = blogRepository.findByBlognameAndUserid(deleteBlogQuery.getBlog().getBlogname(),deleteBlogQuery.getBlog().getUserid());
			//blogRepository.delete(mongoBlog.get());
		blogRepository.deleteAllByBlogname(deleteBlogQuery.getBlog().getBlogname());
			return "success";
		
	}
	@QueryHandler
	public List<MongoBlogs> handle3(FindAllByCategoryQuery findAllByCategoryQuery){
		
		//BeanUtils.copyProperties(,blogs);
			return blogRepository.findAllByCategory(findAllByCategoryQuery.getCategory());
			
		
	}
	@QueryHandler
	public List<MongoBlogs> handle4(FindAllByCategoryAndRangeQuery findAllByCategoryAndRangeQuery){
		
		return blogRepository.findAllByCategory(findAllByCategoryAndRangeQuery.getCategory(),
					findAllByCategoryAndRangeQuery.getFromdate(),findAllByCategoryAndRangeQuery.getTodate());
			
		
	}
}
