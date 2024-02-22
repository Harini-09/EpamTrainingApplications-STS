package com.epam.restcontroller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.epam.dtos.UserDto;
import com.epam.restcontrollers.UserRestController;
import com.epam.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

	@MockBean
	UserServiceImpl service;
	
	@Autowired
	private MockMvc mockMvc;
		
	private UserDto userDto;
	
	@BeforeEach
	void setUp() {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon","","",null);
	}
	
	@Test
	void getUsersTest() throws Exception {
		List<UserDto> users = List.of(userDto);
		Mockito.when(service.viewUsers()).thenReturn(users);
		MvcResult mvcResult = mockMvc.perform(get("/users"))
								.andExpect(status().isOk())
								.andExpect(jsonPath("$[0].username").value("mike1101"))
								.andReturn();
		assertNotNull(mvcResult);
	}

	@Test
	void getUserTest() throws Exception {
		Mockito.when(service.getUserByUsername("mike1101")).thenReturn(userDto);
		MvcResult mvcResult = mockMvc.perform(get("/users/{username}","mike1101"))
								.andExpect(status().isOk())
								.andExpect(jsonPath("$.username").value("mike1101"))
								.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test
	void saveUserTest() throws Exception {
		Mockito.when(service.addUser(userDto)).thenReturn(userDto);
		mockMvc.perform(post("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(userDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.username").value("mike1101"));
	}
	
	@Test
	void deleteUserTest() throws Exception {
		doNothing().when(service).deleteByUsername("mike1101");
		mockMvc.perform(delete("/users/{username}","mike1101"))
				.andExpect(status().isNoContent());
	}
	
	@Test
	void updateUserTest() throws Exception {
		Mockito.when(service.update("mike1101",userDto)).thenReturn(userDto);
		mockMvc.perform(put("/users/{username}","mike1101")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(userDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("mike1101"));
	}
	
}
