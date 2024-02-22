package com.epam.dtos.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeTrainingResponseDto {

	private String trainingName;
	private LocalDate trainingDate;
	private String trainingType;
	private Long trainingDuration;
	private String trainerName;

}
