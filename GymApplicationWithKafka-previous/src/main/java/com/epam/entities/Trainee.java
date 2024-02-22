package com.epam.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Trainee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Nullable
	private LocalDate dateOfBirth;
	@Nullable
	private String address;

	@OneToOne
	User user;

	@OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
	List<Training> trainingsList = new ArrayList<>();

	@ManyToMany
	@JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = {"trainees_list_id","trainers_list_id"}))
	List<Trainer> trainersList = new ArrayList<>();

	public void setId(int id) {
		this.id = id;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setUser(User user) {
		user.setTrainee(this);
		this.user = user;
	}

	public void setTrainingsList(List<Training> trainingsList) {
		this.trainingsList = trainingsList;
	}

	public void setTrainersList(List<Trainer> trainersList) {
		for (Trainer trainer : trainersList) {
			trainer.getTraineesList().add(this);
		}		
		if (this.trainersList == null) {
			this.trainersList = trainersList;
		} else {
			trainersList.forEach(trainer -> this.trainersList.add(trainer));
		}
	}
 
}