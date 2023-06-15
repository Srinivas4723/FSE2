package com.blogsite.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blogsite.entity.Blogs;
import com.blogsite.repository.BlogRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BlogQueryHandler {
	@Autowired BlogRepository blogRepository;
	
	
	@QueryHandler
	public List<Blogs> handle(GetAllBlogsQuery getAllBlogsQuery){
		log.info("Srini");
		
			return blogRepository.findAll();
		
			//return blogRepository.findAllByUserid(getAllQuery.getUserid());
		
	}
	@QueryHandler
	public List<Blogs> handle1(GetMyBlogsQuery getAllQuery){
		
			//return blogRepository.findAll();
		
			return blogRepository.findAllByUserid(getAllQuery.getUserid());
		
	}
	@QueryHandler
	public String handle2(DeleteBlogQuery deleteBlogQuery){
		
			
			blogRepository.delete(deleteBlogQuery.getBlog());
			return "success";
		
	}
	@QueryHandler
	public List<Blogs> handle3(FindAllByCategoryQuery findAllByCategoryQuery){
		
			
			return blogRepository.findAllByCategory(findAllByCategoryQuery.getCategory());
			
		
	}
	@QueryHandler
	public List<Blogs> handle4(FindAllByCategoryAndRangeQuery findAllByCategoryAndRangeQuery){
		
			
			return blogRepository.findAllByCategoryAndTimestampGreaterThanEqualAndTimestampLessThanEqual(findAllByCategoryAndRangeQuery.getCategory(),
					findAllByCategoryAndRangeQuery.getFromdate(),findAllByCategoryAndRangeQuery.getTodate());
			
		
	}
}
