package com.epam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.customexceptions.LibraryException;
import com.epam.dtos.BookDto;
import com.epam.dtos.LibraryDto;
import com.epam.dtos.UserDto;
import com.epam.entities.Library;
import com.epam.repository.LibraryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LibraryServiceImpl implements LibraryService {
	
	@Value("${book.url}")
	String bookUrl;
	
	@Value("${user.url}")
	String userUrl;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	LibraryRepository libraryRepo;

	@Override 
	public LibraryDto issueBooks(String username, int id) throws LibraryException {
		log.info("Entered into the Library Service - issueBooks() method to issue a book with id={} to a user = {}", id,
				username);
		BookDto bookDto = restTemplate.getForObject(bookUrl+"/" + id, BookDto.class);
		UserDto userDto = restTemplate.getForObject(userUrl+"/" + username, UserDto.class);
		if (Optional.of(userDto).get().getUsername() == null) {
			throw new LibraryException("Warning!! Invalid User Name");
		} else if (Optional.of(bookDto).get().getBookname() == null) {
			throw new LibraryException("Warning!! Invalid Book Id");
		} else if (libraryRepo.countByUsername(username) == 3) {
			throw new LibraryException("Warning!! The limit reached for the user. Book cannot be issued");
		} else { 
			Library library = new Library(username, id);
			libraryRepo.save(library);
			return LibraryDto.builder().user(userDto).books(List.of(bookDto)).build();
		}  
	}    
 
	@Override
	public void removeBook(String username, int id) {
		log.info("Entered into the Library Service - removeBook() method to remove a book with id={} to a user = {}",
				id, username);
		Optional<Library> library = libraryRepo.findByUsernameAndBookid(username, id);
		if (library.isPresent()) {
			libraryRepo.delete(library.get());
		}
	}

	@Override
	public LibraryDto getBooksAssociatedWithUser(String username) {
		log.info("Entered into the Library Service - getBooksAssociatedWithUser() method to get the books associated with a user = {}",username);
		UserDto userDto = restTemplate
				.exchange(userUrl+"/" + username, HttpMethod.GET, null, UserDto.class).getBody();
		List<Integer> bookIds = libraryRepo.findAllByUsername(username).stream().map(Library::getBookid).toList();
		List<BookDto> bookDtoList = bookIds.stream()
						.map(id -> restTemplate
						.exchange(bookUrl+"/" + id, HttpMethod.GET, null, BookDto.class).getBody())
						.toList();
		return LibraryDto.builder().user(userDto).books(bookDtoList).build();
	} 

	@Override 
	public void deleteBookFromLibrary(int bookid) {
		log.info("Entered into the Library Service - deleteBookFromLibrary() method to delete the book with book id ={} from the library",bookid);
		libraryRepo.findAllByBookid(bookid).stream().forEach(library -> libraryRepo.delete(library));
	}

	@Override
	public void deleteUserFromLibrary(String username) {
		log.info("Entered into the Library Service - deleteUserFromLibrary() method to delete the user with username ={} from the library",username);
		libraryRepo.findAllByUsername(username).stream().forEach(library -> libraryRepo.delete(library));
	}

}
