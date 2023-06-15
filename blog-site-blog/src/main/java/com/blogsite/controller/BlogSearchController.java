package com.blogsite.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsite.common.AppConstants;
import com.blogsite.entity.Blogs;
import com.blogsite.entity.Category;
import com.blogsite.entity.MongoBlogs;
import com.blogsite.query.FindAllByCategoryAndRangeQuery;
import com.blogsite.query.FindAllByCategoryQuery;
import com.blogsite.repository.BlogRepository;
import com.blogsite.service.KafKaProducerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController // spring bean
@RequestMapping("/blogsite/blogs")
public class BlogSearchController extends ErrorController {
	@Autowired
	BlogRepository blogRepository;
	@Autowired KafKaProducerService kafkaProducerService;
	@Autowired QueryGateway queryGateway;
	/**
	 * Search By Category
	 * 
	 * @param category
	 * @return
	 */
	@GetMapping("/info/{category}")
	public ResponseEntity<Object> searchByCategory(@PathVariable Category category) {
		//List<Blogs> blogs = blogRepository.findAllByCategory(category);
		FindAllByCategoryQuery findAllByCategoryQuery = new FindAllByCategoryQuery();
		findAllByCategoryQuery.setCategory(category);
		List<MongoBlogs> blogs = queryGateway.query(findAllByCategoryQuery, ResponseTypes.multipleInstancesOf(MongoBlogs.class)).join();
		kafkaProducerService.searchBlogs(blogs,AppConstants.TOPIC_SEARCH_BLOG_CAT);
		if (blogs.isEmpty()) {
			return new ResponseEntity<Object>("No Blogs Found", HttpStatus.OK);
		}
		return new ResponseEntity<Object>(blogs, HttpStatus.OK);
	}

	/**
	 * Search By Category and Duration Range
	 * 
	 * @param category
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	@GetMapping("/info/{category}/{fromdate}/{todate}")
	public ResponseEntity<Object> searchByCategoryAndRange(@PathVariable Category category,
			@PathVariable String fromdate, @PathVariable String todate) {
		List<MongoBlogs> blogs = new ArrayList<>();
		LocalDate toDate = LocalDate.parse(todate).plusDays(1);
		//blogs = blogRepository.findAllByCategoryAndTimestampGreaterThanEqualAndTimestampLessThanEqual(category,
			//	Date.valueOf(fromdate), Date.valueOf(toDate));
		FindAllByCategoryAndRangeQuery findAllByCategoryAndRangeQuery = new FindAllByCategoryAndRangeQuery();
		findAllByCategoryAndRangeQuery.setCategory(category);
		findAllByCategoryAndRangeQuery.setFromdate(Date.valueOf(fromdate));
		findAllByCategoryAndRangeQuery.setTodate(Date.valueOf(toDate));
		blogs =  queryGateway.query(findAllByCategoryAndRangeQuery, ResponseTypes.multipleInstancesOf(MongoBlogs.class)).join();
		kafkaProducerService.searchBlogs(blogs,AppConstants.TOPIC_SEARCH_BLOG_CAT_RANGE);														// blogs ;//= blogRepository.findAll(.)();
		if (blogs.isEmpty()) {
			return new ResponseEntity<Object>("No Blogs Found", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(blogs, HttpStatus.OK);
		}

	}

}
