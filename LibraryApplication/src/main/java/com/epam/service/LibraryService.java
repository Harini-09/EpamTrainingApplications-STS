package com.epam.service;

import com.epam.customexceptions.LibraryException;
import com.epam.dtos.LibraryDto;

public interface LibraryService {
	public LibraryDto issueBooks(String username,int id) throws LibraryException ;
	public void removeBook(String username,int id);
	public LibraryDto getBooksAssociatedWithUser(String username);
	public void deleteBookFromLibrary(int bookid);
	public void deleteUserFromLibrary(String username);
}
