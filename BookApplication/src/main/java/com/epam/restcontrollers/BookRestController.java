package com.epam.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.epam.customexceptions.BookException;
import com.epam.dtos.BookDto;
import com.epam.service.BookServiceImpl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/books")
public class BookRestController {

	@Autowired
	BookServiceImpl bookService;
	
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<BookDto>> getBooks(){
		log.info("Received GET request to retrieve all the books");
		return new ResponseEntity<>(bookService.viewBooks(),HttpStatus.OK);
	}
	
	@GetMapping(value="/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> getBookById(@PathVariable("book_id") int id) throws BookException{
		log.info("Received GET request to get a book with book id - {}",id);
		return new ResponseEntity<>(bookService.getBookById(id),HttpStatus.OK);
	}
	
	@PostMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> save(@RequestBody @Valid BookDto bookDto){
		log.info("Received POST request to save a book with book details - {}",bookDto);
		return new ResponseEntity<>(bookService.addBook(bookDto),HttpStatus.CREATED);
	}
	
	@DeleteMapping(value="/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("book_id") int id) {
		log.info("Received DELETE request to delete a book with book id - {}",id);
		bookService.remove(id);
	}
	
	@PutMapping(value="/{book_id}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<BookDto> updateBook(@PathVariable("book_id") int id, @RequestBody @Valid BookDto bookDto) {
		log.info("Received PUT request to update a book with book id - {}, with the updated details - {}",id,bookDto);
		return new ResponseEntity<>(bookService.update(id, bookDto),HttpStatus.OK);
	}
	
}

