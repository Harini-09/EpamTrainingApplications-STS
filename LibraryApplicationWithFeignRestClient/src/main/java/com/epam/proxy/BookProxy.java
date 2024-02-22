package com.epam.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.dtos.BookDto;

import jakarta.validation.Valid;

@FeignClient(name="book",url="http://localhost:8080/books")
public interface BookProxy {
	
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<BookDto>> getBooks();
	
	@GetMapping(value="/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> getBookById(@PathVariable("book_id") int id);
	
	@PostMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> save(@RequestBody @Valid BookDto bookDto);

	@DeleteMapping(value="/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("book_id") int id);
	
	@PutMapping(value="/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> updateBook(@PathVariable("book_id") int id, @RequestBody @Valid BookDto bookDto);

}
