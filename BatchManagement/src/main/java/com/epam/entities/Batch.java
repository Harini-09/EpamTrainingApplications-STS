package com.epam.entities;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
public class Batch { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@NotBlank(message = "The batch name should not be blank.Please enter a valid branch name.")
	String batchName;
	@NotBlank(message = "The practise should not be blank.Please enter a valid practise.")
	String practise;
	@DateTimeFormat
	Date startDate;
	@DateTimeFormat
	Date endDate;
	
	@JsonIgnore
	@OneToMany(mappedBy="batch")
	List<Associate> associates;
	 
	

	public Batch(int id, String batchName, String practise, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.batchName = batchName;
		this.practise = practise;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}