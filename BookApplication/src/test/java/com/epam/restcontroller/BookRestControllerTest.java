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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.epam.dtos.BookDto;
import com.epam.restcontrollers.BookRestController;
import com.epam.service.BookServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

	@MockBean 
	private BookServiceImpl service;
	
	@Autowired
	private MockMvc mockMvc;
		
	private BookDto bookDto;
	
	private HttpStatus httpStatus = null;
	
	@BeforeEach
	void setUp() {
		bookDto = new BookDto("Java","charles","david",2,"","",httpStatus);
	}
	
	@Test
	void getBooksTest() throws Exception {
		List<BookDto> books = List.of(bookDto);
		Mockito.when(service.viewBooks()).thenReturn(books);
		MvcResult mvcResult = mockMvc.perform(get("/books"))
								.andExpect(status().isOk())
								.andExpect(jsonPath("$[0].bookname").value("Java"))
								.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test
	void getBookTest() throws Exception {
		Mockito.when(service.getBookById(1)).thenReturn(bookDto);
		MvcResult mvcResult = mockMvc.perform(get("/books/{book_id}",1))
								.andExpect(status().isOk())
								.andReturn();
		assertNotNull(mvcResult);
	}
	
	@Test
	void saveBookTest() throws Exception {
		Mockito.when(service.addBook(bookDto)).thenReturn(bookDto);
		mockMvc.perform(post("/books")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(bookDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.bookname").value("Java"));
	}
	//<HttpEntity<BookDto>>any(), BookDto.class, any()
	@Test
	void deleteBookTest() throws Exception {
		doNothing().when(service).remove(1);
		mockMvc.perform(delete("/books/{book_id}",1))
				.andExpect(status().isNoContent());
	}
	
	@Test
	void updateBookTest() throws Exception {
		Mockito.when(service.update(1,bookDto)).thenReturn(bookDto);
		mockMvc.perform(put("/books/{book_id}",1)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(bookDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.bookname").value("Java"));
	}

}
