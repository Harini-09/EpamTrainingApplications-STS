package com.epam.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private String firstName;
	@Column(nullable=false)
	private String lastName;
	@Column(nullable=false,unique=true)
	private String userName;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private boolean isActive;
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private final LocalDate createDate = LocalDate.now();
	
	@OneToOne(mappedBy="user",cascade=CascadeType.ALL)
	Trainee trainee;
	
	@OneToOne(mappedBy="user",cascade=CascadeType.ALL)
	Trainer trainer;

}
