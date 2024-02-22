package com.epam.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Training {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String trainingName;
	private LocalDate trainingDate;
	private Long trainingDuration;
	
	@ManyToOne
	Trainee trainee;
	
	@ManyToOne
	Trainer trainer;
	
	@ManyToOne
	TrainingType trainingType;

	public void setId(int id) {
		this.id = id;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	public void setTrainingDate(LocalDate trainingDate) {
		this.trainingDate = trainingDate;
	}

	public void setTrainingDuration(Long trainingDuration) {
		this.trainingDuration = trainingDuration;
	}

	public void setTrainee(Trainee trainee) {
		trainee.getTrainingsList().add(this);
		this.trainee = trainee;
	}

	public void setTrainer(Trainer trainer) {
		trainer.getTrainingsList().add(this);
		this.trainer = trainer;
	}

	public void setTrainingType(TrainingType trainingType) {
		trainingType.getTrainingsList().add(this);
		this.trainingType = trainingType;
	}
	
	

}
