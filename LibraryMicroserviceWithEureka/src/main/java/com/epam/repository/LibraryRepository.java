package com.epam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.epam.entities.Library;

public interface LibraryRepository extends CrudRepository<Library,Integer>{

	public Optional<Library> findByUsernameAndBookid(String username,int bookid);
	public Long countByUsername(String username);
	public List<Library> findAllByUsername(String username);
	public List<Library> findAllByBookid(int bookid);
}
