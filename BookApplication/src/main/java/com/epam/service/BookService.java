package com.epam.service;

import java.util.List;

import com.epam.dtos.BookDto;

public interface BookService {
	public List<BookDto> viewBooks();
	public BookDto getBookById(int id);
	public BookDto addBook(BookDto bookDto);
	public void remove(int id);
	public BookDto update(int id, BookDto bookDto);
}
