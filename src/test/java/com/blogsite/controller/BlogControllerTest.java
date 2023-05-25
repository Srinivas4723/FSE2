package com.blogsite.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.blogsite.entity.Blogs;
import com.blogsite.entity.Category;
import com.blogsite.entity.User;
import com.blogsite.repository.BlogRepository;
import com.blogsite.repository.UserRepository;
import com.blogsite.springjwt.security.services.UserDetailsImpl;


@ExtendWith(MockitoExtension.class)
public class BlogControllerTest {
	@InjectMocks BlogController blogController;
	@Mock BlogRepository blogRepository;
	@Mock UserRepository userRepository;
	Blogs blog = new Blogs();
	User user = new User();
	UserDetailsImpl userdetails = new UserDetailsImpl(1L,"","","",new ArrayList<>());
	@Before
	public void setup() {
	    
	    blog.setBlogname("blogname");
		blog.setCategory(Category.FOOD);
		blog.setAuthorname("authorname");
		blog.setArticle("article");
		user.setId(1L);
		user.setUsername("username");
		user.setEmail("email@gmail.com");
		user.setPassword("password");
		user.setBlogs(getBlogs(blog));
	}
		
	public List<Blogs> getBlogs(Blogs blog){
		List<Blogs> blogs = new ArrayList<Blogs>(); 
		blogs.add(blog);
		return blogs;
	}
		
	@Test
	public void testAddBlogSuccess() {
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.empty());
		when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
		assertEquals(new ResponseEntity<Object>("Blog added Success", HttpStatus.OK),blogController.addBlog(blog, userdetails));
	}
	@Test
	public void testAddBlogFail1() {
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.ofNullable(blog));
		assertEquals(ResponseEntity.badRequest().body("Blog Name already Exist"),blogController.addBlog(blog, userdetails));
	}
	@Test
	public void testAddBlogFail2() {
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.empty());
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		assertEquals(new ResponseEntity<Object>("Unknown Error Occured", HttpStatus.BAD_REQUEST),blogController.addBlog(blog, userdetails));
	}
	@Test
	void testDeleteBlogSuccess() {
		user.getBlogs().add(blog);
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.ofNullable(blog));
		when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
		assertEquals(new ResponseEntity<Object>("Blog Deleted Success", HttpStatus.OK),blogController.deleteBlog(blog.getBlogname(), userdetails));
	}
	@Test
	void testDeleteBlogFail1() {
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.ofNullable(blog));
		when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
		assertEquals(new ResponseEntity<Object>("Blog already deleted / not exists", HttpStatus.BAD_REQUEST),blogController.deleteBlog(blog.getBlogname(), userdetails));
	}
	@Test
	void testDeleteBlogFail2() {
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.ofNullable(blog));
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		assertEquals(new ResponseEntity<Object>("Unknown Error Occured", HttpStatus.BAD_REQUEST),blogController.deleteBlog(blog.getBlogname(), userdetails));
	}
	@Test
	void testDeleteBlogFail3() {
		when(blogRepository.findByBlogname(blog.getBlogname())).thenReturn(Optional.empty());
		assertEquals(new ResponseEntity<Object>("Blog already deleted / not exists", HttpStatus.BAD_REQUEST),blogController.deleteBlog(blog.getBlogname(), userdetails));
	}
	@Test
	void testGetMyBlogs() {
		when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
		assertEquals(new ResponseEntity<Object>(user.getBlogs(),HttpStatus.OK),blogController.getMyBlogs(userdetails));
	}

	@Test
	void testGetAllBlogs() {
		when(blogRepository.findAll()).thenReturn(user.getBlogs());
		assertEquals(new ResponseEntity<Object>(user.getBlogs(),HttpStatus.OK),blogController.getAllBlogs());
	}

}
