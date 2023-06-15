package com.blogsite.commands;

import java.sql.Timestamp;

import com.blogsite.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreatedEvent {
	private String blogId;
	private String blogname;
	private String userid;
	private Category category;
	private String authorname;
	private String article;
	private Timestamp timestamp;
}
