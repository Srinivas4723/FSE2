package com.blogsite.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsite.Request.JwtResponse;
import com.blogsite.Request.LoginRequest;
import com.blogsite.commands.CreateUserCommand;
import com.blogsite.common.AppConstants;
import com.blogsite.entity.ERole;
import com.blogsite.entity.Role;
import com.blogsite.entity.User;
import com.blogsite.repository.RoleRepository;
import com.blogsite.repository.UserRepository;
import com.blogsite.service.KafKaProducerService;
import com.blogsite.springjwt.security.jwt.JwtUtils;
import com.blogsite.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/blogsite/user")
public class AuthController extends ErrorController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired KafKaProducerService kafkaProducerService; 
	
	private CommandGateway commandGateway;
	
	public AuthController(CommandGateway commandGateway) {
		this.commandGateway=commandGateway;
	}

	/**
	 * Authenticate User Credentials
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		kafkaProducerService.userActions(loginRequest.getUsername(), AppConstants.TOPIC_USER_LOGIN);
		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	/**
	 * 
	 * Register new user
	 * 
	 * @param signUpRequest
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			kafkaProducerService.userActions("Error: Username is already taken!"+signUpRequest.getUsername(), AppConstants.TOPIC_USER_REGISTER_FAIL);
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			kafkaProducerService.userActions("Error: Email is already taken!"+signUpRequest.getEmail(), AppConstants.TOPIC_USER_REGISTER_FAIL);
			return ResponseEntity.badRequest().body("Error: Email is already in use!");
		}

		signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));

		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);

		signUpRequest.setRoles(roles);
		//userRepository.save(signUpRequest);
		kafkaProducerService.userActions(signUpRequest.getUsername(), AppConstants.TOPIC_USER_REGISTER);
		CreateUserCommand createBlogCommand = CreateUserCommand.builder()
				.id((long) (Math.random()*1000))
				.username(signUpRequest.getUsername())
				.email(signUpRequest.getEmail())
				.password(signUpRequest.getPassword())
				.roles(signUpRequest.getRoles())
				.build();
		commandGateway.sendAndWait(createBlogCommand);
		return ResponseEntity.ok("User registered successfully!");
	}
}
