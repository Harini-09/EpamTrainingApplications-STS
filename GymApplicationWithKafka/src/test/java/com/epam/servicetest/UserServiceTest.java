package com.epam.servicetest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.Credential;
import com.epam.entities.User;
import com.epam.repository.UserRepository;
import com.epam.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;
	
	@Test
	public void testLoginUser_ValidCredentials_Success() throws UserException {
		Credential validCredential = new Credential("validUsername", "validPassword");
		when(userRepository.findByUserNameAndPassword(validCredential.getUsername(), validCredential.getPassword()))
				.thenReturn(Optional.of(new User()));
		assertDoesNotThrow(() -> userService.loginUser(validCredential));
	}

	@Test
	public void testLoginUser_InvalidCredentials_ThrowsUserException() {
		Credential invalidCredential = new Credential("invalidUsername", "invalidPassword");
		when(userRepository.findByUserNameAndPassword(invalidCredential.getUsername(), invalidCredential.getPassword()))
				.thenReturn(Optional.empty());

		assertThrows(UserException.class, () -> userService.loginUser(invalidCredential));
	}

	@Test
	public void testUpdatePassword_ValidCredentials_Success() throws UserException {
		Credential validCredential = new Credential("validUsername", "validPassword");
		User user = new User();
		when(userRepository.findByUserNameAndPassword(validCredential.getUsername(), validCredential.getPassword()))
				.thenReturn(Optional.of(user));
		String newPassword = "newPassword";

		assertDoesNotThrow(() -> userService.updatePassword(validCredential, newPassword));

		verify(userRepository).save(user);
	}

	@Test
	public void testUpdatePassword_InvalidCredentials_ThrowsUserException() {
		Credential invalidCredential = new Credential("invalidUsername", "invalidPassword");
		String newPassword = "newPassword";
		when(userRepository.findByUserNameAndPassword(invalidCredential.getUsername(), invalidCredential.getPassword()))
				.thenReturn(Optional.empty());

		assertThrows(UserException.class, () -> userService.updatePassword(invalidCredential, newPassword));
	}
}
