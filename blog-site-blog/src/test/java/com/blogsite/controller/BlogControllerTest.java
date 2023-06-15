package com.blogsite.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.blogsite.Utils.JwtUtils;
import com.blogsite.entity.Blogs;
import com.blogsite.entity.Category;
import com.blogsite.repository.BlogRepository;


@ExtendWith(MockitoExtension.class)
public class BlogControllerTest {
	@InjectMocks BlogController blogController;
	@Mock BlogRepository blogRepository;
	@Mock JwtUtils jwtUtils;
	MockHttpServletRequest request = new MockHttpServletRequest();
	String jwtToken="qazwxxedcfr";
	Blogs blog = sampleBlog();
	public Blogs sampleBlog() {
	    Blogs blog = new Blogs();
	    blog.setBlogname("blogname");
		blog.setCategory(Category.FOOD);
		blog.setAuthorname("authorname");
		blog.setArticle("article");
		blog.setUserid("1");
		request.addHeader("Authorization", jwtToken);
		return blog;		
	}
		
	public List<Blogs> getBlogs(Blogs blog){
		List<Blogs> blogs = new ArrayList<Blogs>(); 
		blogs.add(blog);
		return blogs;
	}
		
	@Test
	public void testAddBlogSuccess() {
		request.addHeader("Authorization", jwtToken);
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.empty());
		when(jwtUtils.validateJwtToken(request.getHeader("Authorization"))).thenReturn(true);
		when(jwtUtils.getUserIdFromJwtToken(jwtToken)).thenReturn(blog.getUserid());
		assertEquals(new ResponseEntity<Object>("Blog added Success", HttpStatus.OK),blogController.addBlog(blog,request));
	}
	
	@Test
	public void testAddBlogFail1() {
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(false);
		assertEquals(new ResponseEntity<Object>("UnAuthorised",HttpStatus.UNAUTHORIZED),
				blogController.addBlog(blog, request));
	}

	@Test
	public void testAddBlogFail2() {
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.ofNullable(blog));
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
		assertEquals(ResponseEntity.badRequest().body("Blog Name already Exist"),
				blogController.addBlog(blog, request));
	}

	@Test
	void testDeleteBlogSuccess() {
		
		when(blogRepository.findByBlognameAndUserid(blog.getBlogname(),blog.getUserid())).thenReturn(Optional.ofNullable(blog));
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
		when(jwtUtils.getUserIdFromJwtToken(jwtToken)).thenReturn(blog.getUserid());
		assertEquals(new ResponseEntity<Object>("Blog Deleted Success", HttpStatus.OK),
				blogController.deleteBlog(blog.getBlogname(), request));
	}

	@Test
	void testDeleteBlogFail1() {
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(false);
		assertEquals(new ResponseEntity<Object>("UnAuthorised",HttpStatus.UNAUTHORIZED),
				blogController.deleteBlog(blog.getBlogname(), request));
	}

	@Test
	void testDeleteBlogFail2() {
		//when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.ofNullable(blog));
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
		assertEquals(new ResponseEntity<Object>("Blog already deleted / not exists", HttpStatus.BAD_REQUEST),
				blogController.deleteBlog(blog.getBlogname(), request));
	}

	@Test
	void testGetMyBlogs() {
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
		when(jwtUtils.getUserIdFromJwtToken(jwtToken)).thenReturn(blog.getUserid());
		when(blogRepository.findAllByUserid(blog.getUserid())).thenReturn(getBlogs(blog));
		assertEquals(new ResponseEntity<Object>(getBlogs(blog), HttpStatus.OK),
				blogController.getMyBlogs(request));
	}
	
	@Test
	void testGetMyBlogsFail1() {
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(false);
		assertEquals(new ResponseEntity<Object>("UnAuthorised",HttpStatus.UNAUTHORIZED),
				blogController.getMyBlogs(request));
	}
	
	@Test
	void testGetAllBlogs() {
		when(blogRepository.findAll()).thenReturn(getBlogs(blog));
		assertEquals(new ResponseEntity<Object>(getBlogs(blog), HttpStatus.OK), blogController.getAllBlogs());
	}

}
