package com.epam.dtos.response;

import java.time.LocalDate;
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
public class TraineeProfileDto {

	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String address;
	private boolean isActive;
	private List<TrainerDto> trainersList = new ArrayList<>();
}
