package com.epam.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Trainer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@OneToOne
	User user;
	
	@OneToMany(mappedBy="trainer",cascade=CascadeType.ALL)
	List<Training> trainingsList=new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name="specialization")
	TrainingType trainingType;
	
	@ManyToMany(mappedBy="trainersList")
	List<Trainee> traineesList = new ArrayList<>();

	public void setId(int id) {
		this.id = id;
	}

	public void setUser(User user) {
		user.setTrainer(this);
		this.user = user;
	}

	public void setTrainingsList(List<Training> trainingsList) {
		this.trainingsList = trainingsList;
	}

	public void setTrainingType(TrainingType trainingType) {
		trainingType.getTrainersList().add(this);
		this.trainingType = trainingType;
	}

	public void setTraineesList(List<Trainee> traineesList) {
		this.traineesList = traineesList;
	}
	
	
}
