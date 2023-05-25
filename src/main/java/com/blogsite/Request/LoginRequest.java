package com.blogsite.Request;

import java.sql.Timestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.blogsite.entity.Category;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
