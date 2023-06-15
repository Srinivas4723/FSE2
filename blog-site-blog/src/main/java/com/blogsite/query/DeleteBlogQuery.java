package com.blogsite.query;

import com.blogsite.entity.Blogs;

import lombok.Data;

@Data
public class DeleteBlogQuery {
	private Blogs blog;
}
