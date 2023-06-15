package com.blogsite.query;

import com.blogsite.entity.Category;

import lombok.Data;

@Data
public class FindAllByCategoryQuery {
	private Category category;
}
