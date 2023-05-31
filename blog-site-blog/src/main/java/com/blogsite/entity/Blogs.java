package com.blogsite.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(	name = "blogs")
@Getter
@Setter
public class Blogs {//not a spring bean
	
	@Id
	@NotNull(message = "Blog Name cannot be blank#######")
	@Size(min=10, message="Minimum 10 Characters")
	private String blogname;
	private String userid;
	private Category category;
	@NotNull(message = "Author Name cannot be blank#######")
	@Size(min=3, message="Minimum 3 Characters")
	private String authorname;
	@NotNull(message = "Article cannot be blank#######")
	@Size(min=10, message="Minimum 10 Characters")
	private String article;
	private Timestamp timestamp;
	
}