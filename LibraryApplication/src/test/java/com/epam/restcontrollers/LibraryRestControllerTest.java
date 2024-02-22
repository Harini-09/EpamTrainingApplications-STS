package com.epam.restcontrollers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import com.epam.dtos.BookDto;
import com.epam.dtos.LibraryDto;
import com.epam.dtos.UserDto;
import com.epam.service.LibraryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epam.*;

@SpringBootTest
@ContextConfiguration(classes=LibraryApplication.class)
@AutoConfigureMockMvc
//@WebMvcTest(LibraryRestController.class)
class LibraryRestControllerTest {

	@MockBean
	RestTemplate restTemplate;
	
	@MockBean
	LibraryServiceImpl libraryService;

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	HttpHeaders headers;
	
	private BookDto bookDto;
	
	private UserDto userDto;
	
	private LibraryDto libraryDto;
	
	@Test  
	void viewBooksTest() throws Exception {
		Mockito.when(restTemplate.exchange(
				ArgumentMatchers.anyString(),
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<ParameterizedTypeReference<List<BookDto>>>any()))
				.thenReturn(new ResponseEntity<>(Collections.emptyList(),HttpStatus.OK));
		MvcResult mvcResult = mockMvc.perform(get("/library/books"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(mvcResult);  
	}
	
	@Test
	void getBookByIdTest() throws Exception {
		Mockito.when(restTemplate.exchange(
				ArgumentMatchers.anyString(),
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<ParameterizedTypeReference<BookDto>>any()))
				.thenReturn(new ResponseEntity<>(bookDto,HttpStatus.OK));
		MvcResult mvcResult = mockMvc.perform(get("/library/books/{book_id}",1))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test
	void saveBookTest() throws Exception {
		bookDto = new BookDto("Java","charles","david",2);
		doNothing().when(headers).setContentType(any());
		Mockito.when(restTemplate.exchange(
				ArgumentMatchers.anyString(),
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<ParameterizedTypeReference<BookDto>>any()))
				.thenReturn(new ResponseEntity<>(bookDto,HttpStatus.CREATED));
		mockMvc.perform(post("/library/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(bookDto)))
				.andExpect(status().isCreated());
	} 
	
	@Test
	void deleteBookTest() throws Exception {
		doNothing().when(libraryService).deleteBookFromLibrary(1);
		doNothing().when(restTemplate).delete(any());
		MvcResult mvcResult = mockMvc.perform(delete("/library/books/{book_id}",1))
				.andExpect(status().isNoContent())
				.andReturn();
		assertNotNull(mvcResult);
	}
	 
	@Test
	void modifyBookTest() throws Exception {
		bookDto = new BookDto("Java","charles","david",2);
		doNothing().when(headers).setContentType(any());
		Mockito.when(restTemplate.exchange(
				ArgumentMatchers.anyString(), 
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<ParameterizedTypeReference<BookDto>>any()))
				.thenReturn(new ResponseEntity<>(bookDto,HttpStatus.OK));
		mockMvc.perform(put("/library/books/{book_id}",1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(bookDto)))
				.andExpect(status().isOk());
	}
	 
	@Test
	void viewUsersTest() throws Exception {
		Mockito.when(restTemplate.exchange(
				ArgumentMatchers.anyString(),
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<ParameterizedTypeReference<List<UserDto>>>any()))
				.thenReturn(new ResponseEntity<>(Collections.emptyList(),HttpStatus.OK));
		MvcResult mvcResult = mockMvc.perform(get("/library/users"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(mvcResult);
	} 
	
	@Test
	void getUserByUsernameTest() throws Exception {
		Mockito.when(libraryService.getBooksAssociatedWithUser("abc")).thenReturn(libraryDto);
		MvcResult mvcResult = mockMvc.perform(get("/library/users/{username}","abc"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(mvcResult);
	}
	 
	@Test
	void saveUserTest() throws Exception {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon");
		doNothing().when(headers).setContentType(any());
		Mockito.when(restTemplate.exchange(
				ArgumentMatchers.anyString(),
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<ParameterizedTypeReference<UserDto>>any()))
				.thenReturn(new ResponseEntity<>(userDto,HttpStatus.CREATED));
		mockMvc.perform(post("/library/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void deleteUserTest() throws Exception {
		doNothing().when(libraryService).deleteUserFromLibrary("abc");
		doNothing().when(restTemplate).delete(any());
		MvcResult mvcResult = mockMvc.perform(delete("/library/users/{username}","abc"))
				.andExpect(status().isNoContent())
				.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test
	void modifyUserTest() throws Exception {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon");
		doNothing().when(headers).setContentType(any());
		Mockito.when(restTemplate.exchange(
				ArgumentMatchers.anyString(),
				ArgumentMatchers.eq(HttpMethod.PUT),
				ArgumentMatchers.any(),
				ArgumentMatchers.<ParameterizedTypeReference<UserDto>>any()))
				.thenReturn(new ResponseEntity<>(userDto,HttpStatus.OK));
		mockMvc.perform(put("/library/users/{username}","mike1101")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto)))
				.andExpect(status().isOk());
	} 
	
	@Test
	void issueBooksTest() throws Exception {
		Mockito.when(libraryService.issueBooks("abc",1)).thenReturn(libraryDto);
		MvcResult mvcResult = mockMvc.perform(post("/library/users/{username}/books/{book_id}","abc",1))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test
	void removeBookTest() throws Exception {
		doNothing().when(libraryService).removeBook("abc",1);
		MvcResult mvcResult = mockMvc.perform(delete("/library/users/{username}/books/{book_id}","abc",1))
				.andExpect(status().isNoContent())
				.andReturn();
		assertNotNull(mvcResult);
	}
}
