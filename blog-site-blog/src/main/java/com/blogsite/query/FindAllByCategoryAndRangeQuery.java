package com.blogsite.query;

import java.sql.Date;

import com.blogsite.entity.Category;

import lombok.Data;

@Data
public class FindAllByCategoryAndRangeQuery {
	private Category category;
	private Date fromdate;
	private Date todate;
}
