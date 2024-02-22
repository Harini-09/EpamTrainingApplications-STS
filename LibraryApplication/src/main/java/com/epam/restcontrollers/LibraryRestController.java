package com.epam.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import com.epam.customexceptions.LibraryException;
import com.epam.dtos.BookDto;
import com.epam.dtos.LibraryDto;
import com.epam.dtos.UserDto;
import com.epam.service.LibraryServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/library")
public class LibraryRestController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	LibraryServiceImpl libraryService;
	
	@Value("${book.url}")
	String bookUrl;
	
	@Value("${user.url}")
	String userUrl;
	  
	@GetMapping(value="/books",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<BookDto>> getBooks() {
		log.info("Received GET request to retrieve all the books");
		String url = bookUrl;
		return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<BookDto>>() {} ); 
	} 
	
	@GetMapping(value="/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> getBookById(@PathVariable("book_id") @Valid @NotNull int id) {
		 String url = bookUrl+ "/" + id;
	     return restTemplate.exchange(url,HttpMethod.GET,null,new ParameterizedTypeReference<BookDto>() {} ); 
	}  
	 
	@PostMapping(value="/books",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> save(@RequestBody @Valid BookDto bookDto) {
		String url = bookUrl; 
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookDto> requestEntity = new HttpEntity<>(bookDto, headers); 
        return restTemplate.exchange(url,HttpMethod.POST,requestEntity, new ParameterizedTypeReference<BookDto>() {} ); 
    }  
	   
	@DeleteMapping(value="/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("book_id") @Valid @NotNull int id) {
		libraryService.deleteBookFromLibrary(id);
		String url = bookUrl+"/"+id; 
		restTemplate.delete(url);
	}  
	 
	@PutMapping(value="/books/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> updateBook(@PathVariable("book_id") @Valid @NotNull int id, @RequestBody @Valid BookDto bookDto) {
		 String url = bookUrl+"/"+id;
	     HttpHeaders headers = new HttpHeaders();
	     headers.setContentType(MediaType.APPLICATION_JSON);
	     HttpEntity<BookDto> requestEntity = new HttpEntity<>(bookDto, headers); 
	     return restTemplate.exchange(url,HttpMethod.PUT,requestEntity, new ParameterizedTypeReference<BookDto>() {} ); 
	}
	  
	@GetMapping(value="/users",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<UserDto>> getUsers(){
		String url = userUrl;
		return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDto>>() {} ); 
	}
	
	@GetMapping(value="/users/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<LibraryDto> getUserByUsername(@PathVariable("username") @Valid @NotNull String username) {
		return new ResponseEntity<>(libraryService.getBooksAssociatedWithUser(username),HttpStatus.OK);
	}
	
	@PostMapping(value="/users",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> save(@RequestBody @Valid UserDto userDto){
		String url = userUrl;
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers); 
        return restTemplate.exchange(url,HttpMethod.POST,requestEntity, new ParameterizedTypeReference<UserDto>() {} ); 
	} 
	   
	@DeleteMapping(value="/users/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("username") @Valid @NotNull String username) {
		libraryService.deleteUserFromLibrary(username);
		String url = userUrl+"/"+username;
		restTemplate.delete(url);
	}
	 
	@PutMapping(value="/users/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> updateUser(@PathVariable("username") @Valid @NotNull String username,@RequestBody @Valid UserDto userDto) {
		String url = userUrl+"/"+username;
	     HttpHeaders headers = new HttpHeaders();
	     headers.setContentType(MediaType.APPLICATION_JSON);
	     HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers); 
	     return restTemplate.exchange(url,HttpMethod.PUT,requestEntity, new ParameterizedTypeReference<UserDto>() {} ); 
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
