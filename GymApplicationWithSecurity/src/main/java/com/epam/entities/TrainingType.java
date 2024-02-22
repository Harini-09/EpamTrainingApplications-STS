package com.epam.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class TrainingType {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	@Pattern(regexp="fitness|yoga|Zumba|stretching|resistance",message="Invalid Training Type")
	private String trainingTypeName;
	
	@OneToMany(mappedBy="trainingType")
	List<Training> trainingsList=new ArrayList<>();
	
	@OneToMany(mappedBy="trainingType")
	List<Trainer> trainersList=new ArrayList<>();

}
