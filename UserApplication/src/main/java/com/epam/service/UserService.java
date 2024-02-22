package com.epam.service;

import java.util.List;

import com.epam.dtos.UserDto;

public interface UserService {
	public List<UserDto> viewUsers();
	public UserDto getUserByUsername(String username);
	public UserDto addUser(UserDto userDto);
	public void deleteByUsername(String username);
	public UserDto update(String username, UserDto userDto);
	
}
