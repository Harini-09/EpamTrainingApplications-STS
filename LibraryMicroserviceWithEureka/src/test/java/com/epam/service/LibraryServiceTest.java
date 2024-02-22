package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import com.epam.LibraryApplication;
import com.epam.customexceptions.LibraryException;
import com.epam.dtos.BookDto;
import com.epam.dtos.LibraryDto;
import com.epam.dtos.UserDto;
import com.epam.entities.Library;
import com.epam.repository.LibraryRepository;

@SpringBootTest
@ContextConfiguration(classes=LibraryApplication.class)
@AutoConfigureMockMvc
class LibraryServiceTest {
	  
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	LibraryRepository libraryRepo;
	
	Optional<UserDto> optionalUser;
	
	Optional<BookDto> optionalBook;
	 
	Optional<Library> optionalLibrary;
	
	@InjectMocks
	private LibraryServiceImpl libraryService;
	
	private BookDto bookDto;
	
	private UserDto userDto;
	
	private LibraryDto libraryDto;
	
	private Library library;
	
	@BeforeEach
	void setup() {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon","","",null);
		bookDto = new BookDto("Java","charles","david",2,"","",null);
		optionalUser = Optional.of(userDto);
		library = new Library("mike1101",1);
		libraryDto = new LibraryDto(userDto,List.of(bookDto));
		optionalLibrary = Optional.of(library);
	}
	
	   
	@Test
	void issueBooksTest() throws LibraryException {
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(BookDto.class))).thenReturn(bookDto);
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(UserDto.class))).thenReturn(userDto);
		Mockito.when(libraryRepo.countByUsername("mike1101")).thenReturn(1L);
		Mockito.when(libraryRepo.save(library)).thenReturn(library);
		assertEquals(libraryDto,libraryService.issueBooks("mike1101", 1)); 
	}  
	  
	@Test 
	void issueBooksTestWithException1() {
		//bookDto = new BookDto("Java","charles","david",2);
		userDto = new UserDto(null,null,null,null,null,null);
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(BookDto.class))).thenReturn(bookDto);
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(UserDto.class))).thenReturn(userDto);
		assertThrows(LibraryException.class,()->libraryService.issueBooks("mike",1));
	}
	
	@Test
	void issueBooksTestWithException2() {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon","","",null);
		bookDto = new BookDto(null,null,null,0,"","",null);
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(BookDto.class))).thenReturn(bookDto);
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(UserDto.class))).thenReturn(userDto);
		assertThrows(LibraryException.class,()->libraryService.issueBooks("mike1101",12));
	}  
	
	@Test
	void issueBooksTestWithException3() {
		userDto = new UserDto("mike1101","mike@gmail.com","Mike Tandon","","",null);
		bookDto = new BookDto("Java","charles","david",2,"","",null);
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(BookDto.class))).thenReturn(bookDto);
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), eq(UserDto.class))).thenReturn(userDto);
		Mockito.when(libraryRepo.countByUsername("mike1101")).thenReturn(3l);
		assertThrows(LibraryException.class,()->libraryService.issueBooks("mike1101",0));
	} 
	    
	@Test    
	void removeBookTest() {
		Mockito.when(libraryRepo.findByUsernameAndBookid("mike1101", 1)).thenReturn(optionalLibrary);
		doNothing().when(libraryRepo).delete(library);	
		libraryService.removeBook("mike1101", 1);
		Mockito.verify(libraryRepo).delete(library);
	} 
	
	@Test
	void getBooksAssociatedWithUser() {
		Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.eq(HttpMethod.GET), ArgumentMatchers.any(),ArgumentMatchers.eq( UserDto.class))).thenReturn(new ResponseEntity<>(userDto,HttpStatus.OK));
		Mockito.when(libraryRepo.findAllByUsername("mike1101")).thenReturn(List.of(library));
		Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.eq(HttpMethod.GET), ArgumentMatchers.any(),ArgumentMatchers.eq( BookDto.class))).thenReturn(new ResponseEntity<>(bookDto,HttpStatus.OK));
		assertEquals(libraryDto,libraryService.getBooksAssociatedWithUser("mike1101"));
	}
	 
	@Test    
	void deleteBookFromLibraryTest() {
		Mockito.when(libraryRepo.findAllByBookid(1)).thenReturn(List.of(library));
		doNothing().when(libraryRepo).delete(library);
		libraryService.deleteBookFromLibrary(1);
		Mockito.verify(libraryRepo).delete(library);
	} 
	
	@Test 
	void deleteUserFromLibraryTest() {
		Mockito.when(libraryRepo.findAllByUsername("mike1101")).thenReturn(List.of(library));
		doNothing().when(libraryRepo).delete(library);	
		libraryService.deleteUserFromLibrary("mike1101");
		Mockito.verify(libraryRepo).delete(library);
	} 
	 
	 
} 
