package com.epam.restcontrollers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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
import com.epam.proxy.BookProxy;
import com.epam.proxy.UserProxy;
import com.epam.service.LibraryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epam.*;

@SpringBootTest
@ContextConfiguration(classes=LibraryApplication.class)
@AutoConfigureMockMvc
//@WebMvcTest(LibraryRestController.class)
class LibraryRestControllerWithFeignClientTest {

	@MockBean
	RestTemplate restTemplate;
	
	@MockBean
	LibraryServiceImpl libraryService;

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	BookProxy bookProxy;
	
	@MockBean
	UserProxy userProxy;
	
	@MockBean
	HttpHeaders headers;	
	
	private BookDto bookDto;
	
	private UserDto userDto;
	
	private LibraryDto libraryDto;
	
	@Test  
	void viewBooksTest() throws Exception {
		ResponseEntity<List<BookDto>> entity = new ResponseEntity<>(Collections.emptyList(),HttpStatus.OK);
		Mockito.when(bookProxy.getBooks()).thenReturn(entity);
		MvcResult mvcResult = mockMvc.perform(get("/library/books"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(mvcResult);  
	}
	
	@Test
	void getBookByIdTest() throws Exception {
		ResponseEntity<BookDto> entity = new ResponseEntity<>(bookDto,HttpStatus.OK);
		Mockito.when(bookProxy.getBookById(1)).thenReturn(entity);
		MvcResult mvcResult = mockMvc.perform(get("/library/books/{book_id}",1))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test 
	void saveBookTest() throws Exception {
		bookDto = new BookDto("Java","charles","david",2,null,null,null);
		ResponseEntity<BookDto> entity = new ResponseEntity<>(bookDto,HttpStatus.CREATED);
		Mockito.when(bookProxy.save(bookDto)).thenReturn(entity);
		mockMvc.perform(post("/library/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(bookDto)))
				.andExpect(status().isCreated());
	}  
	 
	@Test
	void deleteBookTest() throws Exception {
		doNothing().when(bookProxy).delete(1);
		MvcResult mvcResult = mockMvc.perform(delete("/library/books/{book_id}",1))
				.andExpect(status().isNoContent())
				.andReturn();
		assertNotNull(mvcResult);
	}
	 
	@Test
	void modifyBookTest() throws Exception {
		bookDto = new BookDto("Java","charles","david",2,null,null,null);
		ResponseEntity<BookDto> entity = new ResponseEntity<>(bookDto,HttpStatus.OK);
		Mockito.when(bookProxy.updateBook(1, bookDto)).thenReturn(entity);
		mockMvc.perform(put("/library/books/{book_id}",1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(bookDto)))
				.andExpect(status().isOk());
	} 
	 
	@Test
	void viewUsersTest() throws Exception {
		ResponseEntity<List<UserDto>> entity = new ResponseEntity<>(Collections.emptyList(),HttpStatus.OK);
		Mockito.when(userProxy.getUsers()).thenReturn(entity);
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
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon",null,null,null);
		ResponseEntity<UserDto> entity = new ResponseEntity<>(userDto,HttpStatus.CREATED);
		Mockito.when(userProxy.save(userDto)).thenReturn(entity);
		mockMvc.perform(post("/library/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto)))
				.andExpect(status().isCreated());
	} 
 
	@Test
	void deleteUserTest() throws Exception {
		doNothing().when(userProxy).delete("abc");
		MvcResult mvcResult = mockMvc.perform(delete("/library/users/{username}","abc"))
				.andExpect(status().isNoContent())
				.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test
	void modifyUserTest() throws Exception {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon",null,null,null);
		ResponseEntity<UserDto> entity = new ResponseEntity<>(userDto,HttpStatus.OK);
		Mockito.when(userProxy.updateUser("mike1101", userDto)).thenReturn(entity);
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
