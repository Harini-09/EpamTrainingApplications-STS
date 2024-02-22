package com.epam.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epam.dtos.BookDto;
import com.epam.entities.Book;
import com.epam.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService{

	@Autowired
	BookRepository bookRepo;

	@Autowired
	ModelMapper modelMapper;
	
	String message = "Book doesn't exist with this Book id : ";

	
	@Override
	public List<BookDto> viewBooks() {
		log.info("Entered into the Book Service - viewBooks() method to get all the books from the library");
		return ((List<Book>)(bookRepo.findAll())).stream().map(book->modelMapper.map(book,BookDto.class)).toList();
	}
 
	@Override
	public BookDto getBookById(int id) {
		log.info("Entered into the Book Service - getBookById() method to get a book with id : {}", id);
		return bookRepo.findById(id).map(book->{
					log.info("Book viewed successfully!!");
				   return BookDto.builder()
							.bookname(book.getBookname())
							.publisher(book.getPublisher())
							.quantity(book.getQuantity())
							.author(book.getAuthor()).build();
		}).orElseGet(()->{
			log.info(message+id);
			return BookDto.builder()
					.timeStamp(new Date().toString())
					.developerMessage(message+id)
					.httpStatus(HttpStatus.OK).build();
		});
		
	}

	@Override
	public BookDto addBook(BookDto bookDto) {
		log.info("Entered into the Book Service - save() method to save a book - {}", bookDto);
		Book book = modelMapper.map(bookDto, Book.class);
		bookRepo.save(book);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public void remove(int id) {
		log.info("Entered into the Book Service - remove() method to delete a book with id : {}", id);
			bookRepo.deleteById(id);
	}

	@Override
	public BookDto update(int id, BookDto bookDto) {
		log.info("Entered into the Book Service - update() method to update a book with id : {} with the updated details - {}",id, bookDto);
		return bookRepo.findById(id).map(book->{
			book.setAuthor(bookDto.getAuthor());
			book.setBookname(bookDto.getBookname());
			book.setPublisher(bookDto.getPublisher());
			book.setQuantity(bookDto.getQuantity());
			return modelMapper.map(book,BookDto.class);
		}).orElseGet(()->{
			log.info(message+id);
			return BookDto.builder()
					.timeStamp(new Date().toString())
					.developerMessage(message+id)
					.httpStatus(HttpStatus.OK).build();
		}); 
	}

} 
