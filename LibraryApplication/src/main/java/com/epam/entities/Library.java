package com.epam.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="library")
public class Library {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int libraryid;
	private String username;
	private int bookid;
	public Library(String username, int bookid) {
		super();
		this.username = username;
		this.bookid = bookid;
	}
	
	
	
		
}
