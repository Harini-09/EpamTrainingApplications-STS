package com.epam.dtos.request;

import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDetailsDto {

	@NotEmpty(message="The Trainee User Name should not be empty")
	private String traineeUserName;
	@NotEmpty(message="The Trainer User Name should not be empty")
	private String trainerUserName;
	@NotEmpty(message="The Training Name should not be null or empty")
	@Size(min=5,max=50,message="The Training Name should have a minimum of 5 characters")	
	private String trainingName;
	@Nullable
	private LocalDate trainingDate;
	@NotEmpty(message="The Training Type Name should not be empty")
	private String trainingTypeName;
	@Positive(message="The Training Duration should be a positive valid number")
	private Long trainingDuration;
	
}
