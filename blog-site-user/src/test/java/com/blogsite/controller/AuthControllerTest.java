package com.blogsite.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blogsite.Request.JwtResponse;
import com.blogsite.Request.LoginRequest;
import com.blogsite.entity.ERole;
import com.blogsite.entity.Role;
import com.blogsite.entity.User;
import com.blogsite.repository.RoleRepository;
import com.blogsite.repository.UserRepository;
import com.blogsite.springjwt.security.jwt.JwtUtils;
import com.blogsite.springjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
	@InjectMocks AuthController authController;
	@Mock UserRepository userRepository;
	@Mock RoleRepository roleRepository;
	@Mock PasswordEncoder encoder;
	@Mock AuthenticationManager authenticationManager;
	@Mock JwtUtils jwtUtils;
	LoginRequest loginRequest = sampleLoginRequest();
	User user = sampleUser();
	Role role = sampleRole();	
	@Mock Authentication authentication;
	
	public LoginRequest sampleLoginRequest(){
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("username");
		loginRequest.setPassword("password");
		return loginRequest;
	}
	public User sampleUser() {
		User user = new User();
		user.setId(1L);
		user.setUsername("username");
		user.setEmail("email@gmail.com");
		user.setPassword("password");
		Set<Role> roles= new HashSet<Role>();
		roles.add(role);
		user.setRoles(roles);
		return user;
	}
	public Role sampleRole() {
		Role role = new Role();
		role.setId(1);
		role.setName(ERole.ROLE_USER);
		return role;
	}
	
	@Test
	void testAuthenticateUser() {
		List<GrantedAuthority> authorities =  new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.getName().name()));
		List<String> roles = new ArrayList<>();
		roles.add(role.getName().name());
		UserDetailsImpl userdetails = new UserDetailsImpl(1L,"username","email@gmail.com","password",authorities);
		userdetails.getPassword();userdetails.isAccountNonExpired();userdetails.isAccountNonLocked();userdetails.isCredentialsNonExpired();userdetails.isEnabled();
		when(authentication.getPrincipal()).thenReturn(userdetails);
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()))).thenReturn(authentication);
		when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");
		JwtResponse responseEntity = (JwtResponse)authController.authenticateUser(loginRequest).getBody();
		JwtResponse jwtResponse = new JwtResponse("jwt", role.getId().longValue(), userdetails.getUsername(), userdetails.getEmail(),roles);
		assertEquals(jwtResponse.getId()+jwtResponse.getUsername()+jwtResponse.getEmail()+jwtResponse.getToken()+jwtResponse.getRoles()+jwtResponse.getType(),
				responseEntity.getId()+responseEntity.getUsername()+responseEntity.getEmail()+responseEntity.getToken()+responseEntity.getRoles()+responseEntity.getType());
	}
	
	@Test
	void testUserDetails() {
		List<GrantedAuthority> authorities =  new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.getName().name()));
		UserDetailsImpl userdetails = new UserDetailsImpl(1L,"username","email@gmail.com","password",authorities);
		userdetails.getPassword();userdetails.isAccountNonExpired();userdetails.isAccountNonLocked();userdetails.isCredentialsNonExpired();userdetails.isEnabled();
		assertTrue(userdetails.equals(userdetails));
		assertFalse(userdetails.equals(null));
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
	void testRegisterUserException() {
		when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
		when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());
		when(encoder.encode(user.getPassword())).thenReturn("aqzwsxedc");
		assertThrows(RuntimeException.class,()->authController.registerUser(user));
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
