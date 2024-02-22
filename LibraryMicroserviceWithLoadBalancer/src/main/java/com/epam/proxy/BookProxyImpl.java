package com.epam.proxy;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.epam.dtos.BookDto;

import jakarta.validation.Valid;

@Service
public class BookProxyImpl implements BookProxy {

	@Override
	public ResponseEntity<List<BookDto>> getBooks() {
		BookDto bookDto=new BookDto();
		bookDto.setAuthor(String.valueOf(0));
		bookDto.setBookname(String.valueOf(0));
		bookDto.setPublisher(String.valueOf(0));
		bookDto.setQuantity(0);
		return new ResponseEntity<>(List.of(bookDto),HttpStatus.OK);
		//book service is currently down
	}

	@Override
	public ResponseEntity<BookDto> getBookById(int id) {
		BookDto bookDto=new BookDto();
		bookDto.setAuthor(String.valueOf(0));
		bookDto.setBookname(String.valueOf(0));
		bookDto.setPublisher(String.valueOf(0));
		bookDto.setQuantity(0);
		return new ResponseEntity<>(bookDto,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BookDto> save(@Valid BookDto bookDto) {
		BookDto savedBookDto=new BookDto();
		savedBookDto.setAuthor(String.valueOf(0));
		savedBookDto.setBookname(String.valueOf(0));
		savedBookDto.setPublisher(String.valueOf(0));
		savedBookDto.setQuantity(0);
		return new ResponseEntity<>(savedBookDto,HttpStatus.OK);
	}

	@Override
	public void delete(int id) {
 }

	@Override
	public ResponseEntity<BookDto> updateBook(int id, @Valid BookDto bookDto) {
		BookDto savedBookDto=new BookDto();
		savedBookDto.setAuthor(String.valueOf(0));
		savedBookDto.setBookname(String.valueOf(0));
		savedBookDto.setPublisher(String.valueOf(0));
		savedBookDto.setQuantity(0);
		return new ResponseEntity<>(savedBookDto,HttpStatus.OK);
	}



}
