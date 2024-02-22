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

import com.epam.customexceptions.BookException;
import com.epam.dtos.BookDto;
import com.epam.entities.Book;
import com.epam.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

	@Mock
	private BookRepository bookRepo;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	BookServiceImpl service;
	
	private Book book;
	
	private BookDto bookDto;
	
	private Optional<Book> optional;
			
	private String message = "Book doesn't exist with this Book id : ";
	
	@BeforeEach
	void setUp() {
		book = new Book(1,"Java","charles","david",2);
		bookDto = new BookDto("Java","charles","david",2,"","",null);
		optional = Optional.of(book);
	}
	
	@Test
	void saveTest() {
		Mockito.when(modelMapper.map(bookDto,Book.class)).thenReturn(book);
		Mockito.when(bookRepo.save(book)).thenReturn(book);
		Mockito.when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);
		BookDto returnedBookDto = service.addBook(bookDto);
		assertEquals(bookDto,returnedBookDto);	
	}
	
	@Test
	void updateTestSuccess() throws BookException {
		Mockito.when(bookRepo.findById(1)).thenReturn(optional);
		Mockito.when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);
		BookDto returnedBookDto = service.update(1,bookDto);
		assertEquals(bookDto,returnedBookDto);
	}
	 
	@Test
	void updateTestFailure() throws BookException {
		BookDto newBookDto = new BookDto(null,null,null,0,new Date().toString(),message+5,HttpStatus.OK);
		Mockito.when(bookRepo.findById(5)).thenReturn(Optional.empty());
		assertEquals(newBookDto,service.update(5, bookDto));
	}
	 
	@Test  
	void deleteTest() throws BookException {
		doNothing().when(bookRepo).deleteById(1);
		service.remove(1);
		Mockito.verify(bookRepo).deleteById(1);
	}
	  
	@Test
	void viewAllTest() {
		List<BookDto> bookDtos = List.of(bookDto);
		List<Book> books = List.of(book);
		Mockito.when(bookRepo.findAll()).thenReturn(books);
		Mockito.when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);
		List<BookDto> returnedbooks = service.viewBooks();
		assertEquals(bookDtos,returnedbooks);
	}
	
	@Test 
	void viewBookTestSuccess() throws BookException {
		BookDto expectedDto = new BookDto("Java","charles","david",2,null,null,null);
		Mockito.when(bookRepo.findById(1)).thenReturn(optional);
		BookDto returnedBookDto = service.getBookById(1);
		assertEquals(expectedDto,returnedBookDto);
	} 
	
	@Test
	void viewBookTestFailure() throws BookException {
		BookDto newBookDto = new BookDto(null,null,null,0,new Date().toString(),message+5,HttpStatus.OK);
		Mockito.when(bookRepo.findById(5)).thenReturn(Optional.empty());
		assertEquals(newBookDto,service.getBookById(5));
	}
 
 
}
