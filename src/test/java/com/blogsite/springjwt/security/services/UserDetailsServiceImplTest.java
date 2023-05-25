package com.blogsite.springjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.blogsite.entity.User;
import com.blogsite.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
	@InjectMocks UserDetailsServiceImpl userdetailsservice;
	@Mock UserRepository userRepository;
	@Test
	void test() {
		User user = new User();
		user.setId(1L);
		user.setUsername("username");
		user.setEmail("email@gmail.com");
		user.setPassword("password");
		when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
		assertEquals(UserDetailsImpl.build(user),userdetailsservice.loadUserByUsername(user.getUsername()));
		
		when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> userdetailsservice.loadUserByUsername(user.getUsername()));
	}
}
