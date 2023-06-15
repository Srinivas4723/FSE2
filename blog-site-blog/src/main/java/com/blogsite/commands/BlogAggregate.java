package com.blogsite.commands;

import java.sql.Timestamp;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.blogsite.entity.Category;

@Aggregate
public class BlogAggregate {

	@AggregateIdentifier
	private String blogId;
	private String blogname;
	private String userid;
	private Category category;
	private String authorname;
	private String article;
	private Timestamp timestamp;
	
	@CommandHandler
	public BlogAggregate(CreateBlogCommand createBlogCommand) {
		BlogCreatedEvent blogCreatedEvent = new BlogCreatedEvent();
		BeanUtils.copyProperties(createBlogCommand, blogCreatedEvent);
		AggregateLifecycle.apply(blogCreatedEvent);
	}
	
	public BlogAggregate() {
		
	}
	
	@EventSourcingHandler
	public void on(BlogCreatedEvent blogCreatedEvent) {
		this.blogId= blogCreatedEvent.getBlogId();
		this.blogname=blogCreatedEvent.getBlogname();
		this.userid=blogCreatedEvent.getUserid();
		this.category=blogCreatedEvent.getCategory();
		this.authorname=blogCreatedEvent.getAuthorname();
		this.article=blogCreatedEvent.getArticle();
		this.timestamp=blogCreatedEvent.getTimestamp();
	}
}
