package com.blogsite.commands;

import java.util.Set;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.blogsite.entity.Role;

@Aggregate
public class UserAggregate {

	@AggregateIdentifier
	private Long id;
	private String username;
	private String email;
	private String password;
	private Set<Role> roles;
	
	@CommandHandler
	public UserAggregate(CreateUserCommand createBlogCommand) {
		UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
		BeanUtils.copyProperties(createBlogCommand, userCreatedEvent);
		AggregateLifecycle.apply(userCreatedEvent);
	}
	
	public UserAggregate() {
		
	}
	
	@EventSourcingHandler
	public void on(UserCreatedEvent blogCreatedEvent) {
		this.id= blogCreatedEvent.getId();
		this.username=blogCreatedEvent.getUsername();
		this.email=blogCreatedEvent.getEmail();
		this.password=blogCreatedEvent.getPassword();
		this.roles=blogCreatedEvent.getRoles();
	}
}
