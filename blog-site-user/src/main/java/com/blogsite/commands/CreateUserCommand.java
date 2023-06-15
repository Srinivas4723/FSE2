package com.blogsite.commands;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.blogsite.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserCommand {
	
	@TargetAggregateIdentifier
	private Long id;
	private String username;
	private String email;
	private String password;
	private Set<Role> roles;
}
