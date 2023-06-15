package com.blogsite.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Document(collection = "blogs")
@Getter
@Setter
public class MongoBlogs {//not a spring bean
	
	@Id
	private String blogname;
	private String userid;
	private Category category;
	
	private String authorname;
	
	private String article;
	private Date timestamp;
	
}