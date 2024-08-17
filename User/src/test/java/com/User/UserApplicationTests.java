package com.User;


import com.User.Entity.User;
import com.User.Repository.UserRepository;
import com.User.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserApplicationTests {
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testRegisterUser() {
		User user = new User();
		user.setPassword("password");
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(userRepository.save(user)).thenReturn(user);

		User result = userService.registerUser(user);
		assertEquals("encodedPassword", result.getPassword());
	}

	@Test
	public void testGetUserById() {
		User user = new User();
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		User result = userService.getUserById(1L);
		assertEquals(user, result);
	}

	@Test
	public void testUpdateUser() {
		User existingUser = new User();
		existingUser.setUsername("oldUsername");
		existingUser.setEmail("oldEmail");
		when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

		User updatedDetails = new User();
		updatedDetails.setUsername("newUsername");
		updatedDetails.setEmail("newEmail");

		when(userRepository.save(existingUser)).thenReturn(existingUser);

		User result = userService.updateUser(1L, updatedDetails);
		assertEquals("newUsername", result.getUsername());
		assertEquals("newEmail", result.getEmail());
	}
}
