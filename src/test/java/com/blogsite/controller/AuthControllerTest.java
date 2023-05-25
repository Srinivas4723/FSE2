package com.blogsite.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blogsite.Request.LoginRequest;
import com.blogsite.entity.ERole;
import com.blogsite.entity.Role;
import com.blogsite.entity.User;
import com.blogsite.repository.BlogRepository;
import com.blogsite.repository.RoleRepository;
import com.blogsite.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
	@InjectMocks AuthController authController;
	@Mock BlogRepository blogRepository;
	@Mock UserRepository userRepository;
	@Mock RoleRepository roleRepository;
	@Mock PasswordEncoder encoder;
	LoginRequest loginRequest = new LoginRequest();
	User user = new User();
	Role role = new Role();
	List<String> roles = new ArrayList<>();
	@Before
	void setUp() throws Exception {
		loginRequest.setUsername("username");
		loginRequest.setPassword("password");
		roles.add("ROLE_USSER");
		user.setId(1L);
		user.setUsername("username");
		user.setEmail("email@gmail.com");
		user.setPassword("password");
		role.setId(1);
		role.setName(ERole.ROLE_USER);
		//user.setBlogs(getBlogs(blog));
	}

	@Test
	void testAuthenticateUser() {
		//assertEquals(ResponseEntity.ok(new JwtResponse("jwt", 1L, "username", "password",roles )),authController.authenticateUser(loginRequest));
		loginRequest.setUsername("username");
		loginRequest.setPassword("password");
		assertThrows(Exception.class,()->authController.authenticateUser(loginRequest));
	}

	@Test
	void testRegisterUserSuccess() {
		when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
		when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.ofNullable(role));
		when(encoder.encode(user.getPassword())).thenReturn("aqzwsxedc");
		assertEquals(ResponseEntity.ok("User registered successfully!"),authController.registerUser(user));
	}
	
	@Test
	void testRegisterUserFail1() {
		when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
		//when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
		assertEquals(ResponseEntity.badRequest().body("Error: Username is already taken!"),authController.registerUser(user));
	}
	
	@Test
	void testRegisterUserFail2() {
		when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
		assertEquals(ResponseEntity.badRequest().body("Error: Email is already in use!"),authController.registerUser(user));
	}

}
