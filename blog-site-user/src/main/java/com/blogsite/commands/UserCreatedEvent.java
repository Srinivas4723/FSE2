package com.blogsite.commands;

import java.util.Set;

import com.blogsite.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {
	private Long id;
	private String username;
	private String email;
	private String password;
	private Set<Role> roles;
}
