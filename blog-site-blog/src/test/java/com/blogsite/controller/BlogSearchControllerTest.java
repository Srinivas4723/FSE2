package com.blogsite.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blogsite.entity.Blogs;
import com.blogsite.entity.Category;
import com.blogsite.repository.BlogRepository;

@ExtendWith(MockitoExtension.class)
public class BlogSearchControllerTest {
	@InjectMocks BlogSearchController blogSearchController;
	@Mock BlogRepository blogRepository;
	
	Blogs blog = sampleBlog();
	String fromdate="2023-05-12";
	String todate="2023-05-13";

	public Blogs sampleBlog() {	
		Blogs blog = new Blogs();
	    blog.setBlogname("blogname");
		blog.setCategory(Category.FOOD);
		blog.setAuthorname("authorname");
		blog.setArticle("article");
		blog.setTimestamp(new Timestamp(System.currentTimeMillis()));
		return blog;
	}
	public List<Blogs> getBlogs(Blogs blog){
		List<Blogs> blogs = new ArrayList<Blogs>(); 
		blogs.add(blog);
		return blogs;
	}
	@Test
	void testSearchByCategory() {
		when(blogRepository.findAllByCategory(Category.FOOD)).thenReturn(getBlogs(blog));
		assertEquals(new ResponseEntity<Object>(getBlogs(blog), HttpStatus.OK),blogSearchController.searchByCategory(Category.FOOD));
	}
	@Test
	void testSearchByCategory1() {
		when(blogRepository.findAllByCategory(Category.FOOD)).thenReturn(getBlogs(blog));
		 ResponseEntity<Object> responseEntity = blogSearchController.searchByCategory(Category.FOOD);
		 @SuppressWarnings("unchecked")
		List<Blogs> blogs = (List<Blogs>)responseEntity.getBody();
		 assertEquals(blogs.get(0).getArticle()+blogs.get(0).getAuthorname()+blogs.get(0).getCategory()+blogs.get(0).getTimestamp(),
				 blog.getArticle()+blog.getAuthorname()+blog.getCategory()+blog.getTimestamp());
	}
	@Test
	void testSearchByCategoryEmpty() {
		when(blogRepository.findAllByCategory(Category.FOOD)).thenReturn(new ArrayList<>());
		assertEquals(new ResponseEntity<Object>("No Blogs Found", HttpStatus.OK),blogSearchController.searchByCategory(Category.FOOD));
	}
	@Test
	void testSearchByCategoryAndRange() {
		getBlogs(blog).add(blog);		
		when(blogRepository.findAllByCategoryAndTimestampGreaterThanEqualAndTimestampLessThanEqual(Category.FOOD,Date.valueOf(fromdate),Date.valueOf(todate))).thenReturn(getBlogs(blog));
		assertEquals(new ResponseEntity<Object>(getBlogs(blog), HttpStatus.OK),blogSearchController.searchByCategoryAndRange(Category.FOOD,fromdate , fromdate));
	}
	@Test
	void testSearchByCategoryAndRangeEmpty() {
		//when(blogRepository.findAllByCategoryAndTimestampGreaterThanEqualAndTimestampLessThanEqual(Category.FOOD,Date.valueOf(date),Date.valueOf(date))).thenReturn(getBlogs(blog));
		assertEquals(new ResponseEntity<Object>("No Blogs Found", HttpStatus.OK),blogSearchController.searchByCategoryAndRange(Category.FOOD,fromdate , todate));
	}
}
