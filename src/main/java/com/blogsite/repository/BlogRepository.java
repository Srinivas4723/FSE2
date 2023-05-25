package com.blogsite.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blogsite.entity.Blogs;
import com.blogsite.entity.Category;

@Repository
public interface BlogRepository extends JpaRepository<Blogs,Long> {
	Optional<Blogs> findByBlogname(String blogName);
	List<Blogs> findAllByCategory(Category category);
	List<Blogs> findAllByCategoryAndTimestampGreaterThanEqualAndTimestampLessThanEqual(Category category,Date fromdate,Date todate);
}
