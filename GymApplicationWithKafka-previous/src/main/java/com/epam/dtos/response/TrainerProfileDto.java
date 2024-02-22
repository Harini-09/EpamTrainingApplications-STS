package com.epam.dtos.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerProfileDto {

	private String firstName;
	private String lastName;
	private String trainingTypeName;
	private boolean isActive;
	List<TraineeDto> traineesList = new ArrayList<>();

}
