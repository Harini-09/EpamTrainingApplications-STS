package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import com.epam.customexceptions.UserException;
import com.epam.dtos.UserDto;
import com.epam.entities.User;
import com.epam.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepo;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private UserServiceImpl service;
	
	private User user;
	
	private UserDto userDto;
	
	private Optional<User> optional;
	
	String message = "User doesn't exist with this Username : ";
	
	@BeforeEach
	void setUp() {
		user = new User(1,"mike1101","mike@gmail.com","Mike Tandon");
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon","","",null);
		optional = Optional.of(user);
	}
	
	@Test
	void saveTest() { 
		Mockito.when(modelMapper.map(userDto, User.class)).thenReturn(user);
		Mockito.when(userRepo.save(user)).thenReturn(user);
		Mockito.when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
		UserDto returnedUserDto = service.addUser(userDto);
		assertEquals(userDto,returnedUserDto);
	}
	
	@Test
	void deleteTestSuccess() throws UserException {
		Mockito.when(userRepo.findByUsername("mike1101")).thenReturn(optional);
		doNothing().when(userRepo).delete(user);
		String result = "true";
		assertEquals("true",result);
		service.deleteByUsername("mike1101");
	}
	
	@Test
	void updateTestSuccess() throws UserException {
		Mockito.when(userRepo.findByUsername("mike1101")).thenReturn(optional);
		Mockito.when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
		UserDto returnedUserDto = service.update("mike1101", userDto);
		assertEquals(userDto,returnedUserDto);
	}
	
	@Test
	void updateTestFailure() throws UserException {
		UserDto userDto = new UserDto(null,null,null,new Date().toString(),message+"abc",HttpStatus.OK);
		Mockito.when(userRepo.findByUsername("abc")).thenReturn(Optional.empty());
		assertEquals(userDto,service.update("abc", userDto));
	}
	  
	@Test
	void viewTest() {
		List<User> users = List.of(user);
		List<UserDto> userDtos = List.of(userDto);
		Mockito.when(userRepo.findAll()).thenReturn(users);
		Mockito.when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
		List<UserDto> returnedUsers = service.viewUsers();
		assertEquals(userDtos,returnedUsers);
	}
	
	@Test
	void viewUserTestSuccess() throws UserException {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon",null,null,null);
		Mockito.when(userRepo.findByUsername("mike1101")).thenReturn(optional);
		UserDto returnedUserDto = service.getUserByUsername("mike1101");
		assertEquals(userDto,returnedUserDto);
	} 
	
	@Test
	void viewUserTestFailure() throws UserException {
		UserDto userDto = new UserDto(null,null,null,new Date().toString(),message+"abc",HttpStatus.OK);
		Mockito.when(userRepo.findByUsername("abc")).thenReturn(Optional.empty());
		assertEquals(userDto,service.getUserByUsername("abc"));
	}
	
	
	
}
