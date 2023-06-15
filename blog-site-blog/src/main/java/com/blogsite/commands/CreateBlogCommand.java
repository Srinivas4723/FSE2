package com.blogsite.commands;

import java.sql.Timestamp;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.blogsite.entity.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBlogCommand {
	
	@TargetAggregateIdentifier
	private String blogId;
	private String blogname;
	private String userid;
	private Category category;
	private String authorname;
	private String article;
	private Timestamp timestamp;
}
