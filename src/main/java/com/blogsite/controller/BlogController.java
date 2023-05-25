package com.blogsite.controller;

import java.sql.Timestamp;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsite.entity.Blogs;
import com.blogsite.entity.User;
import com.blogsite.repository.BlogRepository;
import com.blogsite.repository.UserRepository;
import com.blogsite.springjwt.security.jwt.JwtUtils;
import com.blogsite.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/blogsite/user/blogs")
public class BlogController extends ErrorController {
	@Autowired
	BlogRepository blogRepository;
	@Autowired
	UserRepository userRepository;
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
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Object> addBlog(@Valid @RequestBody Blogs blog,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Optional<Blogs> blog1 = blogRepository.findByBlogname(blog.getBlogname());
		if (blog1.isEmpty()) {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			blog.setTimestamp(timestamp);

			Optional<User> user = userRepository.findById(userDetails.getId());
			if (user.isPresent()) {
				user.get().getBlogs().add(blog);
				userRepository.save(user.get());

				return new ResponseEntity<Object>("Blog added Success", HttpStatus.OK);
			}
			return new ResponseEntity<Object>("Unknown Error Occured", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.badRequest().body("Blog Name already Exist");
	}

	/**
	 * Delete User Blog
	 * 
	 * @param blogName
	 * @param userDetails
	 * @return
	 */
	@DeleteMapping("/delete/{blogName}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Object> deleteBlog(@PathVariable String blogName,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Optional<Blogs> blog = blogRepository.findByBlogname(blogName);
		if (blog.isPresent()) {

			Optional<User> user = userRepository.findById(userDetails.getId());
			if (user.isPresent()) {
				if (user.get().getBlogs().isEmpty()) {
					return new ResponseEntity<Object>("Blog already deleted / not exists", HttpStatus.BAD_REQUEST);
				}
				user.get().getBlogs().remove(blog.get());
				userRepository.save(user.get());
				blogRepository.delete(blog.get());
				return new ResponseEntity<Object>("Blog Deleted Success", HttpStatus.OK);
			}

			return new ResponseEntity<Object>("Unknown Error Occured", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>("Blog already deleted / not exists", HttpStatus.BAD_REQUEST);

	}

	/**
	 * Get Specific user Blogs
	 * 
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/getMyBlogs")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Object> getMyBlogs(@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Optional<User> user = userRepository.findById(userDetails.getId());
		return new ResponseEntity<Object>(user.get().getBlogs(), HttpStatus.OK);
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
