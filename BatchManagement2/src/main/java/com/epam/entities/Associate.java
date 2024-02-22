package com.epam.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Associate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String associateName;
	String email;
	String gender;
	String college;
	String status;
	 
	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	Batch batch;

	public void setId(int id) {
		this.id = id;
	}

	public void setBatch(Batch batch) {
		batch.setAssociates(List.of(this));
		this.batch=batch;
	}
	
	
}
