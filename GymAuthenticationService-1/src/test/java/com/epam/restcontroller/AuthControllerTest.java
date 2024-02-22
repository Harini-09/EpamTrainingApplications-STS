package com.epam.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.Authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.epam.AuthRequest;
import com.epam.controller.AuthController;
import com.epam.service.AuthService;

public class AuthControllerTest {
	@InjectMocks
	private AuthController authController;

	@Mock
	private AuthService authService;

	@Mock
	private AuthenticationManager authenticationManager;

	@BeforeEach
	public void setUp() {
	    MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetToken_ValidCredentials() {
	    String username = "testUser";
	    String password = "testPassword";
	    String token = "testToken";
	    AuthRequest mockAuthRequest = new AuthRequest();
	    mockAuthRequest.setUserName(username);
	    mockAuthRequest.setPassword(password);
	    Authentication mockAuthentication = mock(Authentication.class);
	    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
	            .thenReturn(mockAuthentication);
	    when(mockAuthentication.isAuthenticated()).thenReturn(true);
	    when(authService.generateToken(username)).thenReturn(token);
	    String resultToken = authController.getToken(mockAuthRequest);
	    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
	    verify(authService).generateToken(username);
	    assertEquals(token, resultToken);
	}

	@Test
	public void testGetToken_InvalidCredentials() {
	    String username = "testUser";
	    String password = "testPassword";
	    AuthRequest mockAuthRequest = new AuthRequest();
	    mockAuthRequest.setUserName(username);
	    mockAuthRequest.setPassword(password);
	    Authentication mockAuthentication = mock(Authentication.class);
	    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
	            .thenReturn(mockAuthentication);
	    when(mockAuthentication.isAuthenticated()).thenReturn(false);
	    assertThrows(RuntimeException.class, () -> authController.getToken(mockAuthRequest));
	    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
	} 

	@Test
	public void testValidateToken_ValidToken() {
	    String token = "testToken";
	    doNothing().when(authService).validateToken(token);
	    String result = authController.validateToken(token);
	    verify(authService).validateToken(token);
	    assertEquals("Token is valid", result);
	}
}
