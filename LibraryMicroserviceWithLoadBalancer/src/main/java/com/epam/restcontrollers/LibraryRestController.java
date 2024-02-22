package com.epam.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.*;

import com.epam.customexceptions.LibraryException;
import com.epam.dtos.BookDto;
import com.epam.dtos.LibraryDto;
import com.epam.dtos.UserDto;
import com.epam.proxy.BookProxy;
import com.epam.proxy.UserProxy;
import com.epam.service.LibraryServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/library")
public class LibraryRestController {
	
	@Autowired
	LibraryServiceImpl libraryService;
	
	@Autowired
	BookProxy bookProxy;
	
	@Autowired
	UserProxy userProxy; 
	  
	@GetMapping(value="/books",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<BookDto>> getBooks() {
		log.info("Received GET request to retrieve all the books"); 
		return bookProxy.getBooks();
	}     
	  
	@GetMapping(value="/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> getBookById(@PathVariable("book_id") @Valid @NotNull int id) {
		 return bookProxy.getBookById(id);
	}   
	 
	@PostMapping(value="/books",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> save(@RequestBody @Valid BookDto bookDto) {
		return bookProxy.save(bookDto);
    }  
	   
	@DeleteMapping(value="/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("book_id") @Valid @NotNull int id) {
		bookProxy.delete(id);
	}   
	  
	@PutMapping(value="/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> updateBook(@PathVariable("book_id") @Valid @NotNull int id, @RequestBody @Valid BookDto bookDto) {
		return bookProxy.updateBook(id, bookDto); 
	}
	  
	@GetMapping(value="/users",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<UserDto>> getUsers(){
		return userProxy.getUsers();
	} 
	
	@GetMapping(value="/users/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<LibraryDto> getUserByUsername(@PathVariable("username") @Valid @NotNull String username) {
		return new ResponseEntity<>(libraryService.getBooksAssociatedWithUser(username),HttpStatus.OK);
	}
	
	@PostMapping(value="/users",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> save(@RequestBody @Valid UserDto userDto){
		return userProxy.save(userDto);
	} 
	   
	@DeleteMapping(value="/users/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("username") @Valid @NotNull String username) {
		userProxy.delete(username);
	}
	 
	@PutMapping(value="/users/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> updateUser(@PathVariable("username") @Valid @NotNull String username,@RequestBody @Valid UserDto userDto) {
		return userProxy.updateUser(username, userDto);
	}
	
	@PostMapping(value="/users/{username}/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<LibraryDto> issueBooks(@PathVariable("username") @Valid @NotNull String username,@PathVariable("book_id") @Valid @NotNull int id) throws LibraryException{
		return new ResponseEntity<>(libraryService.issueBooks(username, id),HttpStatus.OK);
	}
	 
	@DeleteMapping(value="/users/{username}/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void removeBook(@PathVariable("username") @Valid @NotNull String username,@PathVariable("book_id") @Valid @NotNull int id){
		libraryService.removeBook(username, id);
	} 
	 
}
