package com.epam.dtos.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingDto {

	@NotEmpty(message="The username should not be empty or null")
	private String username;
	private LocalDate periodFrom;
	private LocalDate periodTo;
	private String trainerName;
	private String trainingType;
}
