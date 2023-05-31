package com.blogsite.controller;

import java.sql.Timestamp;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsite.Utils.JwtUtils;
import com.blogsite.entity.Blogs;
import com.blogsite.repository.BlogRepository;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/blogsite/user/blogs")
public class BlogController extends ErrorController {
	@Autowired
	BlogRepository blogRepository;
	@Autowired
	JwtUtils jwtUtils;

	/**
	 * Creating new Blog
	 * 
	 * @param blog
	 * @param userDetails
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<Object> addBlog(@Valid @RequestBody Blogs blog, HttpServletRequest http) {
		
		String jwtToken =""+http.getHeader("Authorization");
		if(jwtUtils.validateJwtToken(jwtToken)) {
			Optional<Blogs> blog1 = blogRepository.findByBlogname(blog.getBlogname());
			if (!blog1.isPresent()) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				blog.setTimestamp(timestamp);
				blog.setUserid(jwtUtils.getUserIdFromJwtToken(jwtToken));		
				blogRepository.save(blog);				  
				return new ResponseEntity<Object>("Blog added Success", HttpStatus.OK);
			}
			return ResponseEntity.badRequest().body("Blog Name already Exist");
		}
		return new ResponseEntity<Object>("UnAuthorised",HttpStatus.UNAUTHORIZED);

	}
	/**
	 * Delete User Blog
	 * 
	 * @param blogName
	 * @param userDetails
	 * @return
	 */
	@DeleteMapping("/delete/{blogName}")
	public ResponseEntity<Object> deleteBlog(@PathVariable String blogName, HttpServletRequest http) {
		
		String jwtToken =""+http.getHeader("Authorization");
		if(jwtUtils.validateJwtToken(jwtToken)) {
			String userid= jwtUtils.getUserIdFromJwtToken(jwtToken);
			Optional<Blogs> blog = blogRepository.findByBlognameAndUserid(blogName,userid);
			if (blog.isPresent()) {
				blogRepository.delete(blog.get());
				return new ResponseEntity<Object>("Blog Deleted Success", HttpStatus.OK);
			}
			return new ResponseEntity<Object>("Blog already deleted / not exists", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>("UnAuthorised", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Get Specific user Blogs
	 * 
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/getMyBlogs")
	public ResponseEntity<Object> getMyBlogs(HttpServletRequest http) {
		String jwtToken =""+http.getHeader("Authorization");
		if(jwtUtils.validateJwtToken(jwtToken)) {
			String userid= jwtUtils.getUserIdFromJwtToken(jwtToken);
			return new ResponseEntity<Object>(blogRepository.findAllByUserid(userid), HttpStatus.OK);
		}
		return new ResponseEntity<Object>("UnAuthorised", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * List All Blogs
	 * 
	 * @return
	 */
	@GetMapping("/getall")
	public ResponseEntity<Object> getAllBlogs() {
		return new ResponseEntity<Object>(blogRepository.findAll(), HttpStatus.OK);
	}
}
